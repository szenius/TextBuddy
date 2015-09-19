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
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	private static final String MESSAGE_ADDED = "added to %1$s: \"%2$s\"";
	private static final String MESSAGE_DELETED = "deleted from %1$s: \"%2$s\"";
	private static final String MESSAGE_CLEARED_ALL = "all content deleted from %1$s";
	private static final String MESSAGE_SORTED = "all content sorted in %1$s";
	private static final String MESSAGE_EMPTY_FILE = "%1$s is empty";
	private static final String MESSAGE_INVALID_FORMAT = "invalid command format!";

	
	// These are the correct number of parameters for each command
	private static final int PARAM_SIZE_FOR_DELETE = 1;
	private static final int PARAM_SIZE_FOR_SEARCH = 1;
		
	private static Scanner scanner = new Scanner(System.in);
	private static BufferedReader reader;
	private static PrintWriter writer;

	public static void main(String[] args) throws FileNotFoundException, IOException{
		String fileName = args[0];
		TextBuddy buddy = new TextBuddy(fileName);
		buddy.showToUser(String.format(MESSAGE_WELCOME, fileName));
		buddy.runTextBuddy();
	}
	
	public TextBuddy(String fileName) throws IOException {
		initialiseFile(fileName);
		initialiseReader();
		initialiseWriter();
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
	
	public void shutDownTextBuddy() throws IOException {
		reader.close();
		writer.close();
		System.exit(0);
	}


	private void initialiseFile(String fileName) throws IOException{
		this.fileName = fileName;	
		file = new File(fileName);

		if (!file.exists()) {
			file.createNewFile();
		}
	}
	
	private void initialiseReader() throws FileNotFoundException {
		reader = new BufferedReader(new FileReader(file));
	}
	
	private void initialiseWriter() throws FileNotFoundException {
			writer = new PrintWriter(file);
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
			case "sort":
				return sortFile(userCommand);
			case "search":
				return searchFile(userCommand);
			case "exit":	
				shutDownTextBuddy();
			default:
				//show error message if the command is not recognized
				return MESSAGE_INVALID_FORMAT;
		}
	}
	
	/**
	 * This operation is used to add a line of text to the end of the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	public String addText(String userCommand) {
		if (removeFirstWord(userCommand).isEmpty()) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		String lineToAdd = removeFirstWord(userCommand);
		
		writer.println(lineToAdd);
		writer.flush();
	
		return String.format(MESSAGE_ADDED, fileName, lineToAdd);
	}
	
	/**
	 * This operation is used to display all text in the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return content to be displayed
	 */
	public String displayText(String userCommand) throws IOException {
		if (!removeFirstWord(userCommand).equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		int lineNum = 1;
		String fileContent = new String();
		String nextLine = new String();
		
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
			
		return fileContent;
	}
	
	/**
	 * This operation is used to delete a line of text in the textfile
	 * based on the input line number
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	public String deleteText(String userCommand) throws IOException {
		String[] parameters = splitParameters(removeFirstWord(userCommand).trim());

		if (parameters.length != PARAM_SIZE_FOR_DELETE) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}

		int toDeleteLineNum = Integer.valueOf(parameters[0]);
		int lineNum = 1;
		String lineToDelete = new String();
		String nextLine;
		
		while((nextLine = reader.readLine()) != null) {
			if (lineNum == toDeleteLineNum) {
				lineToDelete = nextLine;
				lineNum++;
				continue;
			}
			
			writer.println(nextLine);
			lineNum++;			
		}
		
		writer.flush();
				
		return String.format(MESSAGE_DELETED, fileName, lineToDelete);
	}
	
	/**
	 * This operation is used to clear all text in the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	public String clearAllText(String userCommand) throws IOException {
		if (!removeFirstWord(userCommand).equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		while(reader.readLine() != null) {
			continue;
		}

		return String.format(MESSAGE_CLEARED_ALL, fileName);
	}

	/**
	 * This operation is used to sort text alphabetically in the textfile
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return status of the operation
	 */
	public String sortFile(String userCommand) throws IOException {
		if (!removeFirstWord(userCommand).equals("")) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		String nextLine = reader.readLine();
		
		if (nextLine == null) {
			return String.format(MESSAGE_EMPTY_FILE, fileName);
		}
		
		ArrayList<String> sortedFileContent = new ArrayList<String>();
		sortedFileContent.add(nextLine);
		
		while ((nextLine = reader.readLine()) != null) {
			sortedFileContent.add(nextLine);
		}
		
		Collections.sort(sortedFileContent);
		
		for (int i = 0; i < sortedFileContent.size(); i++) {
			writer.println(sortedFileContent.get(i));
		}
		
		writer.flush();
		
		return String.format(MESSAGE_SORTED, fileName);
	}
	
	/**
	 * This operation is used to search for lines in the textfile
	 * 	that contains the input search word
	 * 
	 * @param userCommand
	 *            is the full string user has entered as the command
	 * @return lines which contain the search word
	 */
	public String searchFile(String userCommand) throws IOException {
		String[] parameters = splitParameters(removeFirstWord(userCommand).trim());

		if (parameters.length != PARAM_SIZE_FOR_SEARCH) {
			return String.format(MESSAGE_INVALID_FORMAT, userCommand);
		}
		
		String searchWord = parameters[0];
		String searchResults = new String();
		int lineNum = 1;
		String nextLine = reader.readLine();
		
		if (nextLine == null) {
			return String.format(MESSAGE_EMPTY_FILE, fileName);
		}
		
		if (nextLine.contains(searchWord)) {
			searchResults += lineNum + ". " + nextLine;
			writer.println(nextLine);
			lineNum++;
		}
		
		while ((nextLine = reader.readLine()) != null) {
			if (nextLine.contains(searchWord)) {
				searchResults += "\n" + lineNum + ". " + nextLine;
				lineNum++;
			}
			
			writer.println(nextLine);
		}	
		
		writer.flush();
		
		return searchResults;
	}
	
	
	public void showToUser(String text) {
		System.out.println(text);
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