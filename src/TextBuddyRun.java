import java.io.*;

public class TextBuddyRun {
	private static TextBuddy bud = new TextBuddy();
	
	private static final String MESSAGE_WELCOME = "Welcome to TextBuddy. %1$s is ready for use";
	
	public static void main(String[] args) throws FileNotFoundException, IOException{
		String fileName = args[0];
		bud.setup(fileName);
		bud.showToUser(String.format(MESSAGE_WELCOME, fileName));
		bud.runTextBuddy();
	}
}
