import java.util.*;
import java.io.*;
/**
 * This class is used to store/retrieve the text input by users in/from the file mytextfile.txt
 * The program throws out an error message to indicate invalid commands when they are made
 * The command format is given by the example interaction below:
 
 Welcome to TextBuddy. mytextfile.txt is ready for use
 command: add little brown fox
 added to mytextfile.txt: “little brown fox”
 command: display
 1. little brown fox
 command: add jumped over the moon
 added to mytextfile.txt: “jumped over the moon”
 command: display
 1. little brown fox
 2. jumped over the moon
 command: delete 2
 deleted from mytextfile.txt: “jumped over the moon”
 command: display
 1. little brown fox
 command: clear
 all content deleted from mytextfile.txt
 command: display
 mytextfile.txt is empty
 command: abcd
 invalid command format!
 command: exit
 
 * @author Szeying
 */

public class TextBuddy {
	
	private File file;
	private String fileName; 
	
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEARED_ALL = "all content deleted from %1$s";
	private static final String MESSAGE_EMPTY_FILE = "%1$s is empty";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format!";
	
	// These are the correct number of parameters for each command
	private static final int PARAM_SIZE_FOR_DELETE = 1;
		
	private static Scanner scanner = new Scanner(System.in);
	private static BufferedReader reader;
	private static PrintWriter writer;
	
	public void setup(String fileName) throws IOException {
		initialiseFile(fileName);
		initialiseReader();
		initialiseWriter();
	}

	private void initialiseFile(String fileName) throws IOException{
		this.fileName = fileName;	
		file = new File(fileName);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException ioe) {
				showToUser("error occured while creating new file.");
			}
		}
	}
	
	private void initialiseReader(){
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException fnfe) {
			showToUser("file not found.");
		}
	}
	
	private void initialiseWriter() {
		try {
			writer = new PrintWriter(file);
		} catch (FileNotFoundException fnfe) {
			showToUser("file not found.");
		} 
	}
	
	public void showToUser(String text) {
		System.out.println(text);
	}
	
	public void runTextBuddy() throws IOException {
		while (true) {
			System.out.print("command:");
			String command = scanner.nextLine();
			String feedback = executeCommand(command);
			if (feedback != null) {
				showToUser(feedback);
			}
		}	
	}
	
	public String executeCommand(String userCommand) throws IOException {
		String commandType = getFirstWord(userCommand).toLowerCase();
		
		switch (commandType) {
			case "add":
				return addText(userCommand);
			case "display":
				return displayText(userCommand);
			case "delete":
				return deleteText(userCommand);
			case "clear":
				return clearAllText(userCommand);
			case "exit":	
				reader.close();
				writer.close();
				System.exit(0);
			default:
				//show error message if the command is not recognized
				return MESSAGE_INVALID_FORMAT;
		}
	}
	
	/**
	 * This operation is used to clear all text in the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	public String clearAllText(String userCommand) {
		if (!removeFirstWord(userCommand).equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		try {			
			while(reader.readLine() != null) {
				continue;
			}

		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
		

		return String.format(MESSAGE_CLEARED_ALL, fileName);
	}
	
	/**
	 * This operation is used to delete a line of text in the textfile
	 * based on the input line number
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	public String deleteText(String userCommand) {
		String[] parameters = splitParameters(removeFirstWord(userCommand).trim());

		if (parameters.length != PARAM_SIZE_FOR_DELETE) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		int toDeleteLineNum = Integer.valueOf(parameters[0]);
		int lineNum = 1;
		String lineToDelete = new String();
		ArrayList<String> toWriteBack = new ArrayList<String>();
		
		String nextLine;
		
		// Read all lines in text file except the line to be deleted to a temp file
		try {
			while((nextLine = reader.readLine()) != null) {
				if (lineNum == toDeleteLineNum) {
					lineToDelete = nextLine;
					lineNum++;
					continue;
				}
				
				toWriteBack.add(nextLine);				
				lineNum++;			
			}
			
			for (int i = 0; i < toWriteBack.size(); i++) {
				writer.write(toWriteBack.get(i) + System.getProperty("line.separator"));
				writer.flush();
			}
			
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
				
		return String.format(MESSAGE_DELETED, fileName, lineToDelete);
	}

	/**
	 * This operation is used to display all text in the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return MESSAGE_DISPLAY_SUCCESS if operation succeeds
	 */
	public String displayText(String userCommand) {
		if (!removeFirstWord(userCommand).equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		int lineNum = 1;
		String fileContent = new String();
		String nextLine = new String();
		
		try {
			nextLine = reader.readLine();
			
			if (nextLine == null) {
				return String.format(MESSAGE_EMPTY_FILE, fileName);
			}
			
			fileContent += lineNum + ". " + nextLine;
			lineNum++;
			writer.println(nextLine);
			
			while ((nextLine = reader.readLine()) != null) {
				fileContent += "\n" + lineNum + ". " + nextLine;
				writer.println(nextLine);
				lineNum++;
			}
			
			writer.flush();
			
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
		
		return fileContent;
	}

	/**
	 * This operation is used to add a line of text to the end of the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	public String addText(String userCommand) {
		String lineToAdd = removeFirstWord(userCommand);
		
		writer.write(lineToAdd + System.getProperty("line.separator"));
		writer.flush();
	
		return String.format(MESSAGE_ADDED, fileName, lineToAdd);
	}
	
	private static String removeFirstWord(String userCommand) {
		return userCommand.replace(getFirstWord(userCommand), "").trim();
	}

	private static String getFirstWord(String userCommand) {
		String commandTypeString = userCommand.trim().split("\\s+")[0];
		return commandTypeString;
	}
	
	private static String[] splitParameters(String commandParametersString) {
		String[] parameters = commandParametersString.trim().split("\\s+");
		return parameters;
	}	
}