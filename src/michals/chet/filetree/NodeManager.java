package michals.chet.filetree;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;


public class NodeManager {
	
	@SuppressWarnings("serial")
	public class InvalidInputException extends Exception{

		public InvalidInputException(String errorMessage) {
			super(errorMessage);
		}
				
	}
	
	private HashMap<String, FileNode> masterNodeList;
	private String lastRoot;
	
	
	//Takes in a set of file names, and populates the masterNodeList. Must be called before other methods
	public void createNodeList(Set<String> inputList) {
		masterNodeList = new HashMap<String, FileNode>();
		for (String fileName : inputList) {
			FileNode newNode = new FileNode(fileName);
			masterNodeList.put(fileName, newNode);
		}
	}
	
	//Takes in a list of pairs, and sets up the relationship between the nodes. 
	public void setNodeRelationships(List<SimpleEntry <String, String>> inputList) throws InvalidInputException, IllegalStateException {
		checkMasterNodeList();		
		for (Entry<String, String> parentChildPair: inputList) {
			FileNode parent = masterNodeList.get(parentChildPair.getKey());
			FileNode child = masterNodeList.get(parentChildPair.getValue());
			
			if (parent == null) {
				throw new InvalidInputException("Parent was not defined in intial file list: " 
						+ parentChildPair.getKey());
			}
			
			if (child == null) continue;
			
			parent.addChildNode(child);
		}		
	}
	
	//Calculates the distance between all nodes, based off of the root node 
	public void calculateDistancesFromRoot(String rootName) throws InvalidInputException, IllegalStateException {
		checkMasterNodeList();
		lastRoot = rootName;
		LinkedList<FileNode> searchQueue = new LinkedList<FileNode>();
		FileNode rootNode = masterNodeList.get(rootName);
		if (rootNode == null) throw new InvalidInputException("Root Node of name '" + rootName + "' not found."); 
		resetDistancesFromRoot();
		rootNode.setDistanceFromRoot(0);
		searchQueue.push(rootNode);
		
		while (searchQueue.peek() != null) {
			FileNode nextNode = searchQueue.poll();
			searchQueue.addAll(setDistancesFromRootForNode(nextNode));
		}
	}
	
	//Returns a list of nodes not connected to the root note
	public List<String> getDisconnectedNodes() throws IllegalStateException {
		checkMasterNodeList();
		ArrayList<String> returnList = new ArrayList<String>();
		for (FileNode currentNode : masterNodeList.values()) {
			if (currentNode.getDistanceFromRoot() == FileNode.DEFALUT_DISTANCE)
				returnList.add(currentNode.getName());
		}
		Collections.sort(returnList);
		return returnList;
	}
	
	//Outputs a String that lists out the tree structure of the shortest route to each file, along with orphaned files at the bottom  
	public String getSummeryOfLastCalculation() throws InvalidInputException, IllegalStateException {
		checkMasterNodeList();
		FileNode rootNode = masterNodeList.get(lastRoot);
		if (rootNode == null) throw new InvalidInputException("Root Node of name '" + lastRoot + "' not found.");
		StringBuilder returnString = summeryOfNodesRelationships(rootNode).append(System.lineSeparator()).append(getOrphanedFiles());
		return returnString.toString();
	}
	
	//Return a StringBuilder Object of all nodes connected to node passed in
	private StringBuilder summeryOfNodesRelationships(FileNode nodeToCheck) {
		StringBuilder currentStringBuilder = new StringBuilder();
		return summeryOfNodesRelationships(nodeToCheck, currentStringBuilder);
	}
	
	//Return a StringBuilder Object of all nodes connected to node passed in, adding onto the passed into StringBuilder
	private StringBuilder summeryOfNodesRelationships(FileNode nodeToCheck, StringBuilder currentStringBuilder) {
		//Make a nice little tree structure for easy visualization
		int indentLevel = nodeToCheck.getDistanceFromRoot();
		if (indentLevel > 0) {
			for (int i = 0; i < indentLevel; i++) {
				currentStringBuilder.append("\t");
			}
			currentStringBuilder.append("|-- ");
		}
		currentStringBuilder.append(nodeToCheck.getName() + System.lineSeparator());
		HashSet<FileNode> childNodes = nodeToCheck.getChildNodeList();
		for (FileNode childNode : childNodes) {
			summeryOfNodesRelationships(childNode,currentStringBuilder);
		}
		return currentStringBuilder;
	}
	
	//Returns a StringBuilder Object of all orphaned files in alphabetical order
	private StringBuilder getOrphanedFiles(){
		ArrayList<String> listOfOrphans = new ArrayList<String>();
		for (FileNode node : masterNodeList.values()) {
			if (node.getDistanceFromRoot() == FileNode.DEFALUT_DISTANCE) {
				listOfOrphans.add(node.getName());
			}
		}
		if (listOfOrphans.isEmpty()) {
			return new StringBuilder();
		}
		
		Collections.sort(listOfOrphans);
		StringBuilder currentStringBuilder = new StringBuilder("List of Orphaned Files: " + System.lineSeparator() + System.lineSeparator());
		for (String nodeName : listOfOrphans) {
			currentStringBuilder.append(nodeName + System.lineSeparator());
		}
		return currentStringBuilder;
	}
	
	private void checkMasterNodeList() throws IllegalStateException {
		if (masterNodeList == null) throw new IllegalStateException("Node List must be created before this function can be called");
	}
	
	//Takes in a node, and sets the distance from the root for all child nodes. 
	private HashSet<FileNode> setDistancesFromRootForNode(FileNode currentNode) {
		HashSet<FileNode> returnList = currentNode.getChildNodeList();
		for (FileNode childNode : returnList) {
			if (childNode.getDistanceFromRoot() != FileNode.DEFALUT_DISTANCE) {
				returnList.remove(childNode);
			} else {
				childNode.setDistanceFromRoot(currentNode.getDistanceFromRoot() + 1);
			}
		}
		return returnList;
	}
	
	private void resetDistancesFromRoot() {
		for (FileNode currentNode : masterNodeList.values()) {
			currentNode.resetDistanceFromRoot();
		}
	}
}
