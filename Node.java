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
	/*
	 * Constructs a Node that has an x and y coordinate, a value, and a boolean if it is a permant node (start or end)
	 * @param one      x-coordinate of the Node
	 * @param two      y-coordinate of the Node
	 * @param v        an int that represents the name and value of the Node
	 * @param start    a boolean that represents the start/end status of the Node
	 * @return         a Node with an x and y coordinate, a value, and a start/end boolean
	 */
	public Node(int one, int two, int v, boolean start) {
		cOne = one;		 //row
		cTwo = two;		 //column
		value = v; 		 //value
		starting = start;//if the node is a starting node or not, determined by a false or true
	}//End Node constructor
	
	//getter methods
	/*
	 * Returns the x-coordinate of the Node
	 * @return       an int that represents the x-coordinate of the Node
	 */
	public int getCOne() {
		return cOne;
	}//End getCOne
	
	/*
	 * Returns the y-coordinate of the Node
	 * @return       an int that represents the y-coordinate of the Node
	 */
	public int getCTwo() {
		return cTwo;
	}//End getCTwo
	
	/*
	 * Returns the value/name of the Node
	 * @return       an int that represents the value/name of the Node
	 */
	public int getValue() {
		return value;
	}//End getValue
	
	/*
	 * Returns the boolean value if the Node is a start or end Node
	 * @return       a boolean that represents the start/end status of a Node
	 */
	public boolean getStarting() {
		return starting;
	}//End getStarting
	
	
	//setter methods
	/*
	 * Sets the x-coordinate of the Node
	 * @param x        new x-coordinate of the Node
	 * @return void
	 */
	public void setCOne(int x) {
		cOne = x;
	}//End setCOne
	
	/*
	 * Sets the y-coordinate of the Node
	 * @param y        new y-coordinate of the Node
	 * @return void
	 */
	public void setCTwo(int y) { 
		cTwo = y;
	}//End setCTwo
	
	/*
	 * Sets the value of a node
	 * @param z        new value of the Node
	 * @return void
	 */
	public void setValue(int z) { 
		value = z;
	}//End setValue
	
	/*
	 * Sets the starting status of the Node
	 * @param a        new starting status of the Node
	 * @return void
	 */
	public void setStarting(boolean a) { //used when creating starting node pairs
		starting = a;
	}//End setStarting
	
	
	/*
	 * Main (description)
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {

	}//End main

}//End Node class
