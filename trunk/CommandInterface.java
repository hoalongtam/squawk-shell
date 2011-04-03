package squawkshell;
import java.io.*;
import java.util.HashMap;
import java.util.HashSet;


public class CommandInterface {

	private class ShellListener extends Thread {
		BufferedReader ear;
		CommandInterface controller;
		GUI viewer;

		public void run(){
			try {
				String line;
				while ((line = ear.readLine ()) != null && !shutdown) {
					viewer.receiveResponse(line);
				}
			}catch (java.io.IOException e) {}
		}
		
		
		public ShellListener (BufferedReader br,CommandInterface c,GUI v) {
			ear = br;
			controller = c;
			viewer = v;
		}
		
	}


	//public GUI viewer;

	private static HashMap<String, String> supportedShells;
	private OutputStream stdin = null;
	private InputStream stderr = null;
	private InputStream stdout = null;
	private Process process; 

	private BufferedReader br,er; 
	static private GUI viewer;
	
	private CommandInterface child;
	public String title;
	private boolean master;
	private boolean shutdown;
	
	ShellListener sl, el;



	public CommandInterface(GUI v) throws java.io.IOException{
		supportedShells = new HashMap<String, String>();
		//Code to get supported shells
		supportedShells.put("python", "python -i");
		
		process = Runtime.getRuntime ().exec ("/bin/bash");
		title = "Bash";
		stdin = process.getOutputStream ();
		stderr = process.getErrorStream ();
		stdout = process.getInputStream ();
		stdout = process.getInputStream ();
		br = new BufferedReader (new InputStreamReader (stdout));
		er = new BufferedReader (new InputStreamReader(stderr));
		viewer = v;
		this.master = true;

		this.sl = new ShellListener(br,this,viewer);
		this.el = new ShellListener(er,this,viewer);
		sl.start();
		el.start();
	}
	
	public CommandInterface(String title) throws IOException{
		process = Runtime.getRuntime ().exec (title);
		this.title = title;
		stdin = process.getOutputStream ();
		stderr = process.getErrorStream ();
		stdout = process.getInputStream ();
		stdout = process.getInputStream ();
		br = new BufferedReader (new InputStreamReader (stdout));
		er = new BufferedReader (new InputStreamReader(stderr));

		this.sl = new ShellListener(br,this,viewer);
		this.el = new ShellListener(er,this,viewer);
		sl.start();
		el.start();
	}


	public void sendCommand(String command) throws java.io.IOException{
		
		if (processShellCommand(command))
			return;
		
		
		if (child != null && !child.isShutdown()){
			child.sendCommand(command);
			return;
		}
		else if (child != null) {
			this.child = null;
		}
		
		
		
		String line;
		line = command + "\n";   
		stdin.write(line.getBytes() );
		stdin.flush();
	}
	
	public void terminate(){
		
		if (child != null){
			child.terminate();
			return;
		}
			
		if (master)
			return;
		
		try {
			stdin.close();
			stderr.close();
		} catch (IOException e) {}
		this.shutdown = true;
	}

	public boolean isShutdown(){
		return !sl.isAlive() || !el.isAlive();
	}
	
	
	
	public boolean processShellCommand(String command) throws IOException{
		String altCmd = CommandInterface.supportedShells.get(command);
		
		if (altCmd != null){
			this.child = new CommandInterface(altCmd);
			return true;
		}
		
		
		if (command.equals("process")){
			if(this.child == null)
				viewer.receiveResponse(this.title);
			else
				this.child.sendCommand("process");
			return true ;
		}

		return false;
	}
	
	
	
}