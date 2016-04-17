package michals.chet.filetree;
import java.util.HashSet;


public class FileNode {
	public static final int DEFALUT_DISTANCE = -1;
	private String nodeName;
	private int distanceFromRoot; //Set to -1 for orphaned from root, 0 for root, positive for connected to the root 
	private HashSet<FileNode> childNodes;
	
	public FileNode(String name) {
		nodeName = name; 
		distanceFromRoot = -1;
		childNodes = new HashSet<FileNode>();
	}
	
	public void setDistanceFromRoot(int distance) {
		distanceFromRoot = distance;
	}
	
	public int getDistanceFromRoot(){
		return distanceFromRoot;
	}
	
	public void resetDistanceFromRoot(){
		distanceFromRoot = -1;
	}
	
	public String getName() {
		return nodeName;
	}
	
	public void addChildNode(FileNode childNode) {
		childNodes.add(childNode);
	}
	
	//HashSet returned is a copy.
	public HashSet<FileNode> getChildNodeList() {
		return new HashSet<FileNode>(childNodes);
	}
}
