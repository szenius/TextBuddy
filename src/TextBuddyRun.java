import java.io.*;
/**
 * This class is used to store/retrieve the text input by users in/from a file 
 * 	using an instance of TextBuddy object
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

public class TextBuddyRun {
	private static TextBuddy buddy;
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		String fileName = args[0];
		buddy = new TextBuddy(fileName);
		
		showToUser(String.format(MESSAGE_WELCOME, fileName));
		buddy.runTextBuddy();
	}
	
	public static void showToUser(String text) {
		System.out.println(text);
	}
}
