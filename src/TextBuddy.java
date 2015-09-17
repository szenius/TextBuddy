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
	
	private static File file;
	private static String fileName; 
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DISPLAY = "%1$d. %2$s";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEARED_ALL = "all content deleted from %1$s";
	private static final String MESSAGE_EMPTY_FILE = "%1$s is empty";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format!";
	
	// These are the correct number of parameters for each command
	private static final int PARAM_SIZE_FOR_DELETE = 1;
		
	private static Scanner scanner = new Scanner(System.in);
	private static BufferedReader reader;
	private static BufferedWriter writer;
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		initialiseFile(args);
		initialiseReader();
		initialiseWriter();
		showToUser(String.format(MESSAGE_WELCOME, fileName));
		runTextBuddy();
	}
	
	public String testExecuteCommand(String userCommand) throws IOException {
		String commandType = getFirstWord(userCommand).toLowerCase();
		fileName = "test.txt";
		file = new File(fileName);
		initialiseReader();
		initialiseWriter();
		
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
				//throw an error if the command is not recognized
				throw new Error("unrecognized command.");
		}
	}

	private static void initialiseFile(String[] args) throws IOException{
		fileName = args[0];	
		file = new File(fileName);

		if (!file.exists()) {
			try {
				file.createNewFile();
			} catch(IOException ioe) {
				showToUser("error occured while creating new file.");
			}
		}

	}
	
	private static void initialiseReader(){
		try {
			reader = new BufferedReader(new FileReader(file));
		} catch (FileNotFoundException fnfe) {
			showToUser("file not found.");
		}
	}
	
	private static void initialiseWriter() {
		try {
			writer = new BufferedWriter(new FileWriter(file));
		} catch (FileNotFoundException fnfe) {
			showToUser("file not found.");
		} catch (IOException ioe) {
			showToUser("error occured while initialising writer.");
		}
	}
	
	private static void showToUser(String text) {
		System.out.println(text);
	}
	
	private static void runTextBuddy() throws IOException {
		while (true) {
			System.out.print("command:");
			String command = scanner.nextLine();
			String feedback = executeCommand(command);
			showToUser(feedback);
		}	
	}
	
	public static String executeCommand(String userCommand) throws IOException {
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
				//throw an error if the command is not recognized
				throw new Error("unrecognized command.");
		}
	}
	
	/**
	 * This operation is used to clear all text in the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	private static String clearAllText(String userCommand) {
		if (!removeFirstWord(userCommand).equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		try {
			String nextLine = new String();
			
			while((nextLine = reader.readLine()) != null) {
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
	private static String deleteText(String userCommand) {
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
	 * @return status of the operation
	 */
	private static String displayText(String userCommand) {
		if (!removeFirstWord(userCommand).equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		int lineNum = 1;
		String toDisplay = new String();
		String toWriteBack = new String();
		String nextLine = new String();
		
		try {
			nextLine = reader.readLine();
			
			if (nextLine == null) {
				return String.format(MESSAGE_EMPTY_FILE, fileName);
			}
			
			toDisplay += (lineNum + ". " + nextLine);
			toWriteBack += nextLine;
			lineNum++;
			
			while((nextLine = reader.readLine()) != null) {
				toDisplay += ("\n" + lineNum + ". " + nextLine);
				toWriteBack += ("\n" + nextLine);
				lineNum++;
			}
			
			toWriteBack += "\n";
			
			writer.write(toWriteBack);
			writer.flush();
			
		} catch (IOException ioe){
			ioe.printStackTrace();
		}
		
		return toDisplay;
	}

	/**
	 * This operation is used to add a line of text to the end of the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	private static String addText(String userCommand) {
		String textToAdd = removeFirstWord(userCommand);
		
		try {
			writer.write(textToAdd + System.getProperty("line.separator"));
			writer.flush();
		} catch(IOException ioe) {
			ioe.printStackTrace();
		}
	
		return String.format(MESSAGE_ADDED, fileName, textToAdd);
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