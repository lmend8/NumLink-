/* Project Name: NumLink
 * By: Developers MLHL
 * Date Created: 9/22/19
 * Purpose: Creation of a Node Object to store coordinates and values 
 */

public class Node {
	private int cOne;
	private int cTwo;
	private int value;
	private boolean starting;
	
	//constructors
	public Node(int one, int two, int v, boolean start) {
		cOne = one;		 //row
		cTwo = two;		 //column
		value = v; 		 //value
		starting = start;//if the node is a starting node or not, determined by a false or true
	}
	
	//getter methods
	public int getCOne() {
		return cOne;
	}
	public int getCTwo() {
		return cTwo;
	}
	public int getValue() {
		return value;
	}
	public boolean getStarting() {
		return starting;
	}
	
	
	//setter methods
	public void setCOne(int x) {
		cOne = x;
	}
	public void setCTwo(int y) { 
		cTwo = y;
	}
	public void setValue(int z) { 
		value = z;
	}
	public void setStarting(boolean a) { //used when creating starting node pairs
		starting = a;
	}
	
	
	
	public static void main(String[] args) {

	}

}
