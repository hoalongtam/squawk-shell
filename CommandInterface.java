import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.StringTokenizer;

public class CommandInterface {
  
  private class ShellListener extends Thread {
    BufferedReader ear;
    CommandInterface controller;
    GUI viewer;
    boolean isOut;
    
    @Override
    public void run() {
      try {
        String line;
        while ((line = ear.readLine()) != null && !shutdown) {
          if (dirSet && isOut) {
            viewer.directoryChange(line);
            dirSet = false;
            System.out.println(line);
          } else {
            dirSet = false;
            viewer.receiveResponse(line);
          }
        }
      } catch (java.io.IOException e) {}
    }
    
    public ShellListener(BufferedReader br, CommandInterface c, GUI v, boolean out) {
      ear = br;
      controller = c;
      viewer = v;
      isOut = out;
    }
    
  }
  
  private static HashMap<String, String> supportedShells;
  private OutputStream stdin = null;
  private InputStream stderr = null;
  private InputStream stdout = null;
  private final Process process;
  
  private final BufferedReader br, er;
  static private GUI viewer;
  
  private CommandInterface child;
  public String title;
  private boolean master;
  private boolean shutdown;
  boolean dirSet = true;
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
    viewer.receiveResponse("# " + line);
    stdin.write(line.getBytes());
    stdin.flush();
    
    dirSet = true;
    line = "\npwd\n";
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
    if (command.substring(0, 4).equals("man ")) {
      StringTokenizer st = new StringTokenizer(command.substring(4), " ", false);
      viewer.popupManPage((String)st.nextElement(), true);
      return true;
    }
    
    return false;
  }
}
