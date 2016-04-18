package michals.chet.filetree.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.AbstractMap.SimpleEntry;

import michals.chet.filetree.NodeManager;
import michals.chet.filetree.NodeManager.InvalidInputException;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;

public class NodeManagerTest {

	@Test
	public void testHappyPath() throws IllegalStateException, InvalidInputException {
		NodeManager testNodeManager = new NodeManager();
		//Initialize node list
		testNodeManager.createNodeList(makeTestFileList());
		
		//Set up relationships between nodes
		testNodeManager.setNodeRelationships(makeTestRelationships());
		
		//Find distance from specified root
		testNodeManager.calculateDistancesFromRoot("file1.js");
		
		//Get nodes orphaned from root
		List<String> orphins = testNodeManager.getDisconnectedNodes();
		
		//Test that list contains expected values
		List<String> expected = Arrays.asList("file4.js");
		assertThat(orphins, is(expected));
	}
	
	@Test (expected = IllegalStateException.class) 
	public void testIllegalStateException() throws IllegalStateException, InvalidInputException{
		NodeManager testNodeManager = new NodeManager();
		
		//This should throw an exception, since createNodeList has not been called yet
		testNodeManager.calculateDistancesFromRoot("file1.js");
	}
	
	@Test
	public void testGetSummeryOfLastCalculation() throws IllegalStateException, InvalidInputException {
		//Set up NodeManager the same way as in happy path test
		NodeManager testNodeManager = new NodeManager();
		testNodeManager.createNodeList(makeTestFileList());
		testNodeManager.setNodeRelationships(makeTestRelationships());
		testNodeManager.calculateDistancesFromRoot("file1.js");
		assertEquals(getExpectedCalcSummery(), testNodeManager.getSummeryOfLastCalculation());
	}
	
	private Set<String> makeTestFileList() {
		HashSet<String> returnList = new HashSet<String>();
		returnList.add("file1.js");
		returnList.add("file2.js");
		returnList.add("file3.js");
		returnList.add("file4.js");
		return returnList;
	}
	
	private List<SimpleEntry <String, String>> makeTestRelationships() {
		List<SimpleEntry <String, String>> returnList = new ArrayList<SimpleEntry<String, String>>();
		SimpleEntry<String, String> entry1 = new SimpleEntry<String, String>("file1.js", "file2.js");
		SimpleEntry<String, String> entry2 = new SimpleEntry<String, String>("file2.js", "file3.js");
		SimpleEntry<String, String> entry3 = new SimpleEntry<String, String>("file4.js", "file3.js");
		returnList.add(entry1);
		returnList.add(entry2);
		returnList.add(entry3);		
		
		return returnList;
	}
	
	private String getExpectedCalcSummery(){
		String returnString = "file1.js" + System.lineSeparator() + 
				"\t|-- file2.js" + System.lineSeparator() +
				"\t\t|-- file3.js" + System.lineSeparator() + System.lineSeparator() +
				"List of Orphaned Files: " + System.lineSeparator() + System.lineSeparator() +
				"file4.js" + System.lineSeparator();
		return returnString;
	}
}
