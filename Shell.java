
public class Shell extends Thread {

	private final String GREETING_MESSAGE = "Welcome to my Shell!\n";

	private String name = "Not specified";

	public Shell() {
		
		SysLib.cout(GREETING_MESSAGE);
	}

	public Shell(String args[]) {
		
		name = args[1];
		SysLib.cout(GREETING_MESSAGE);
		SysLib.cout("Shell constructor running. Name:" + name + "\n");
	}


	public void run() {
		
		int count = 1;

		boolean runnable = true;
		while (runnable) {
			
			SysLib.cout("Shell["+ count +"]: ");
			
			StringBuffer stringBuffer = new StringBuffer();
			
			SysLib.cin(stringBuffer);

			String[] commands = SysLib.stringToArgs(stringBuffer.toString());
			if (commands.length < 1) {
				continue;	// Display Shell[i] again
			}

			
			runnable = checkExit(commands);
			if (!runnable) {		// If "exit" command
				break;
			}

			SysLib.cout("\n");

			count++;

			
			String[] cmdSplitSemiColon = stringBuffer.toString().split(";");
			String[] cmdSplitAmpersand = stringBuffer.toString().split("&");
			String cmdSplitMultiple = stringBuffer.toString();

			processCommands(cmdSplitSemiColon, cmdSplitAmpersand, cmdSplitMultiple);

			
		}
		
		SysLib.cout("Exiting...\n");
		SysLib.sync();
		SysLib.exit();
			
	}


	public boolean checkExit(String commands[]) {
		
		return !
			(commands[0].compareTo("exit") == 0);
	}
	
	
	public void processCommands(String[] cmdSplitSemiColon, String[] cmdSplitAmpersand, String cmdSplitMulti) {
	
		boolean checkSemiColon = false;
		boolean checkAmpersand = false;
		
		if (cmdSplitSemiColon.length > 1) {
			checkSemiColon = true;
		}
		
		if (cmdSplitAmpersand.length > 1) {
			checkAmpersand = true;
		}
		
		if (checkAmpersand && checkSemiColon) {// If multiple arguments are used
			execCommandMultiple(cmdSplitMulti);
		}
		else if (checkAmpersand) {		// & is used
			execCommandAmpersand(cmdSplitAmpersand);
		}
		else {
			execCommandSemiColon(cmdSplitSemiColon);
		}
		
	}
	
	public void execCommandSemiColon(String[] args) {
		
		for (int i = 0; i < args.length; i++) {
			String[] commands = SysLib.stringToArgs(args[i]);
			
			SysLib.cout(commands[0] + ": \n\t");
			
			if (SysLib.exec(commands) < 0) {
				return;
			}
			
			
			SysLib.join();
			
		}
	}
	
	
	public void execCommandAmpersand(String[] args) {
		
		int processCount = 0;
		
		for (int i = 0; i < args.length; i++) {
			String[] commands = SysLib.stringToArgs(args[i]);
			
			SysLib.cout(commands[0] + "\n\t");
			
			if (SysLib.exec(commands) < 0) {
				processCount--;
			}
			else {
				// Otherwise, increment
				processCount++;
			}
		}
	
		for (int j = 0; j < processCount; j++) {
			SysLib.join();
		}
		
	}
	
	
	public void execCommandMultiple(String args) {
		String[] cmdSplitSemiColon = args.split(";");
		
		for (int i = 0; i < cmdSplitSemiColon.length; i++) {
			SysLib.cout(cmdSplitSemiColon[i] + "\n");
			
			String[] cmdSplitAmpersand = cmdSplitSemiColon[i].split("&");
			if (cmdSplitAmpersand.length == 1) {
				execCommand(cmdSplitSemiColon[i]);
			}
			else {
				execCommandAmpersand(cmdSplitAmpersand);
			}
			
		}
	}
	
	
	public void execCommand(String arg) {
		String[] args = SysLib.stringToArgs(arg);
		if (SysLib.exec(args) > 0) {
			SysLib.join();
		}
	}
}
					
