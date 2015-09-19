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
		assertEquals("added to testFile.txt: \"one little plankton\"", 
					buddy.executeCommand("add one little plankton"));
		assertEquals("added to testFile.txt: \"two little giants\"",
					buddy.executeCommand("add two little giants"));
		assertEquals("added to testFile.txt: \"three little daisies\"", 
					buddy.executeCommand("add three little daisies"));
	}
	
	@Test
	public void testDeleteText() throws IOException{
		testAddText();
		assertEquals("deleted from testFile.txt: \"one little plankton\"", 
					buddy.executeCommand("delete 1"));
		assertEquals("deleted from testFile.txt: \"three little daisies\"", 
					buddy.executeCommand("delete 2"));
		assertEquals("deleted from testFile.txt: \"two little giants\"", 
					buddy.executeCommand("delete 1"));
	}
	
	@Test
	public void testClearAllText() throws IOException{
		testAddText();
		assertEquals("all content deleted from testFile.txt", 
					buddy.executeCommand("clear"));
	}
	
	@Test
	public void testDisplayText() throws IOException{
		testAddText();
		
		String expectedDisplayText = "1. one little plankton\n";
		expectedDisplayText += "2. two little giants\n";
		expectedDisplayText += "3. three little daisies";
		
		assertEquals(expectedDisplayText, buddy.executeCommand("display"));
	}
	
	@Test
	public void testSortFile() throws IOException{
		testAddText();
		
		assertEquals("all content sorted in testFile.txt", buddy.executeCommand("sort"));
		
		String expectedDisplayText = "1. one little plankton\n";
		expectedDisplayText += "2. three little daisies\n";
		expectedDisplayText += "3. two little giants";

		assertEquals(buddy.executeCommand("display"), expectedDisplayText);
	}
	
	@Test
	public void testSearchFile() throws IOException{
		testAddText();
		
		String expectedSearchResults = "1. one little plankton\n";
		expectedSearchResults += "2. two little giants\n";
		expectedSearchResults += "3. three little daisies";
		
		assertEquals(expectedSearchResults, buddy.executeCommand("search little"));
		
		expectedSearchResults = "1. one little plankton";
		
		assertEquals(expectedSearchResults, buddy.executeCommand("search plankton"));
	}

}
