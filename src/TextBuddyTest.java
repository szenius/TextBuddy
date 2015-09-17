import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

public class TextBuddyTest {

	@Test
	public void testAddText() {
		TextBuddy bud = new TextBuddy();
		String message = new String();
		
		try {
			message = bud.testExecuteCommand("add little brown fox");
			assertEquals(message, "added to test.txt: \"little brown fox\"");
			
			message = bud.testExecuteCommand("add mary had a little lamb");
			assertEquals(message, "added to test.txt: \"mary had a little lamb\"");
	
			message = bud.testExecuteCommand("add merry merry ho ho ho");
			assertEquals(message, "added to test.txt: \"merry merry ho ho ho\"");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Test
	public void testDeleteText() {
		TextBuddy bud = new TextBuddy();
		String message = new String();
		
		try {
			bud.testExecuteCommand("add pppppppapokerface");
			bud.testExecuteCommand("add mamaramama");
			bud.testExecuteCommand("add just dance");
	
			message = bud.testExecuteCommand("delete 2");
			assertEquals(message, "deleted from test.txt: \"mamaramama\"");
			
			message = bud.testExecuteCommand("delete 1");
			assertEquals(message, "deleted from test.txt: \"pppppppapokerface\"");
			
			message = bud.testExecuteCommand("delete 1");
			assertEquals(message, "deleted from test.txt: \"just dance\"");
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}

	}
	
	@Test
	public void testClearAllText() {
		TextBuddy bud = new TextBuddy();
		String message = new String();
		
		try {
			bud.testExecuteCommand("add we the citizens");
			bud.testExecuteCommand("add of singapore");
			bud.testExecuteCommand("add pledge ourselves");
			bud.testExecuteCommand("add to be one united people");
	
			message = bud.testExecuteCommand("clear");
			assertEquals(message, "all content deleted from test.txt");	
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	@Test
	public void testDisplayText() {
		TextBuddy bud = new TextBuddy();
		String message = new String();
		
		try {
			bud.testExecuteCommand("add we are never ever ever");
			message = bud.testExecuteCommand("display");
			assertEquals(message, "1. we are never ever ever");
			
			bud.testExecuteCommand("add ever ever ever");
			bud.testExecuteCommand("add ever ever ever");
			message = bud.testExecuteCommand("display");
			assertEquals(message, "1. we are never ever ever\n2. ever ever ever\n 3. ever ever ever");
	
			bud.testExecuteCommand("delete 2");
			message = bud.testExecuteCommand("display");
			assertEquals(message, "1. we are never ever ever\n2. ever ever ever");
	
			bud.testExecuteCommand("add getting back together");
			message = bud.testExecuteCommand("display");
			assertEquals(message, "1. we are never ever ever\n2. ever ever ever\n3. getting back together");
	
			bud.testExecuteCommand("clear");
			message = bud.testExecuteCommand("display");
			assertEquals(message, "test.txt is empty");		
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		
	}

}
