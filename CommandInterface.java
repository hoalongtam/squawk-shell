import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommandInterface {
  
  private class ShellListener extends Thread {
    BufferedReader ear;
    CommandInterface controller;
    GUI viewer;
    
    @Override
    public void run() {
      try {
        String line;
        while ((line = ear.readLine()) != null && !shutdown) {
          if (line.length() > 6 && line.substring(0, 7).equals("#######")) {
            viewer.directoryChange(line.substring(8));
          } else {
            viewer.receiveResponse(line);
          }
        }
      } catch (java.io.IOException e) {}
    }
    
    public ShellListener(BufferedReader br, CommandInterface c, GUI v, boolean out) {
      ear = br;
      controller = c;
      viewer = v;
    }
    
  }
  
  private static HashMap<String, String> supportedShells;
  private OutputStream stdin = null;
  private InputStream stderr = null;
  private InputStream stdout = null;
  private final Process process;
  private static HashMap<String, String> aproposBuffer;
  
  private final BufferedReader br, er;
  static private GUI viewer;
  
  private CommandInterface child;
  public String title;
  private boolean master;
  private boolean shutdown;
  ShellListener sl, el;
  
  public CommandInterface(GUI v) throws java.io.IOException {
    supportedShells = new HashMap<String, String>();
    // Code to get supported shells
    supportedShells.put("python", "python -i");
    
    process = Runtime.getRuntime().exec("/bin/bash");
    title = "Bash";
    stdin = process.getOutputStream();
    stderr = process.getErrorStream();
    stdout = process.getInputStream();
    stdout = process.getInputStream();
    br = new BufferedReader(new InputStreamReader(stdout));
    er = new BufferedReader(new InputStreamReader(stderr));
    viewer = v;
    master = true;
    
    sl = new ShellListener(br, this, viewer, true);
    el = new ShellListener(er, this, viewer, false);
    sl.start();
    el.start();
  }
  
  public CommandInterface(String title) throws IOException {
    process = Runtime.getRuntime().exec(title);
    this.title = title;
    stdin = process.getOutputStream();
    stderr = process.getErrorStream();
    stdout = process.getInputStream();
    stdout = process.getInputStream();
    br = new BufferedReader(new InputStreamReader(stdout));
    er = new BufferedReader(new InputStreamReader(stderr));
    
    sl = new ShellListener(br, this, viewer, true);
    el = new ShellListener(er, this, viewer, false);
    sl.start();
    el.start();
  }
  
  public void sendCommand(String command) throws java.io.IOException {
    
    if (processShellCommand(command)) { return; }
    if (child != null && !child.isShutdown()) {
      child.sendCommand(command);
      return;
    } else if (child != null) {
      child = null;
    }
    String line;
    line = command;
    viewer.receiveResponse("bash # " + line);
    stdin.write(line.getBytes());
    stdin.flush();
    
    line = "\necho \"#######\" `pwd`\n";
    stdin.write(line.getBytes());
    stdin.flush();
    
  }
  
  public void terminate() {
    if (child != null) {
      child.terminate();
      return;
    }
    
    if (master) { return; }
    try {
      stdin.close();
      stderr.close();
    } catch (IOException e) {}
    shutdown = true;
  }
  
  public boolean isShutdown() {
    return !sl.isAlive() || !el.isAlive();
  }
  
  public boolean processShellCommand(String command) throws IOException {
    String altCmd = CommandInterface.supportedShells.get(command);
    
    if (altCmd != null) {
      child = new CommandInterface(altCmd);
      return true;
    }
    
    if (command.equals("process")) {
      if (child == null) {
        viewer.receiveResponse(title);
      } else {
        child.sendCommand("process");
      }
      return true;
    }
    if (command.length() > 4 && command.substring(0, 4).equals("man ")) {
      StringTokenizer st = new StringTokenizer(command.substring(4), " ", false);
      viewer.popupManPage((String)st.nextElement());
      return true;
    }
    
    return false;
  }
  
  public void apropos() throws InterruptedException, IOException {
    
    aproposBuffer = new HashMap<String, String>();
    Runtime r = Runtime.getRuntime();
    Process man = r.exec("apropos (1)");
    BufferedReader man_output = new BufferedReader(
                                                   new InputStreamReader(
                                                                         man.getInputStream()));
    // InputStream man_error = man.getErrorStream();
    /*
     * if (status != 0) { int error_msg_size = man_error.available(); for (int i
     * = 0; i < error_msg_size; i += 1) { System.err.printf("%c",
     * (char)man_error.read()); } return; }
     */

    Thread.sleep(1000);
    StringBuilder sb = new StringBuilder();
    String line = null;
    while ((line = man_output.readLine()) != null) {
      sb.append(line + "\n");
    }
    
    parseManBuffer(sb.toString());
  }
  
  public void parseManBuffer(String s) {
    Pattern p = Pattern.compile("(.+?)\\s+-\\s+(.+)");
    Matcher m = null, m2 = null;
    m = p.matcher(s);
    while (m.find()) {
      LinkedList<String> nameList = new LinkedList<String>();
      String names = m.group(1);
      String rhs = m.group(2);
      
      Pattern p2 = Pattern.compile("(.+)\\(1\\),?(.*)");
      m2 = p2.matcher(names);
      while (m2.find()) {
        nameList.add(m2.group(1));
      }
      
      for (String name : nameList) {
        aproposBuffer.put(name, rhs);
      }
      
    }
  }
  
  public String[] searchBuffer(String c) {
    LinkedList<String> items = new LinkedList<String>();
    for (String key : aproposBuffer.keySet()) {
      if (key.startsWith(c)) {
        items.add(key + " -- " + aproposBuffer.get(key) + "\n");
      }
    }
    String[] sarry = new String[items.size()];
    int i = 0;
    for (Object o : items) {
      sarry[i++] = (String)o;
    }
    return sarry;
  }
  
}
