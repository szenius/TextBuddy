import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

public class TextBuddyTest {
	
	private TextBuddy buddy;
	
	@Before
	public void testSetup() throws IOException{
		buddy = new TextBuddy("testFile.txt");
	}

	@Test
	public void testAddText() throws IOException{
		assertEquals(buddy.executeCommand("add one little plankton"), 
					"added to testFile.txt: \"one little plankton\"");
		assertEquals(buddy.executeCommand("add two little giants"), 
				"added to testFile.txt: \"two little giants\"");
		assertEquals(buddy.executeCommand("add three little daisies"), 
				"added to testFile.txt: \"three little daisies\"");
	}
	
	@Test
	public void testDeleteText() throws IOException{
		testAddText();
		assertEquals(buddy.executeCommand("delete 1"), 
					"deleted from testFile.txt: \"one little plankton\"");
		assertEquals(buddy.executeCommand("delete 2"), 
				"deleted from testFile.txt: \"three little daisies\"");
		assertEquals(buddy.executeCommand("delete 1"), 
				"deleted from testFile.txt: \"two little giants\"");
	}
	
	@Test
	public void testClearAllText() throws IOException{
		testAddText();
		assertEquals(buddy.executeCommand("clear"), 
					"all content deleted from testFile.txt");
	}
	
	@Test
	public void testDisplayText() throws IOException{
		testAddText();
		
		String expectedDisplayText = "1. one little plankton\n";
		expectedDisplayText += "2. two little giants\n";
		expectedDisplayText += "3. three little daisies";
		
		assertEquals(buddy.executeCommand("display"), expectedDisplayText);
	}
	
	@Test
	public void testSortFile() throws IOException{
		testAddText();
		
		assertEquals(buddy.executeCommand("sort"), "testFile.txt is now sorted.");
		
		String expectedDisplayText = "1. one little plankton\n";
		expectedDisplayText += "2. three little daisies";
		expectedDisplayText += "3. two little giants\n";

		assertEquals(buddy.executeCommand("display"), expectedDisplayText);
	}

}
