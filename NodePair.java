/* Project Name: NumLink
 * By: Developers MLHL
 * Date Created: 9/30/19
 * Purpose: Creation of a pair of nodes, and to check if the pair is connected or not
 */

public class NodePair {
	//start and end don't need getters or setters because the constructor will create the node pair
		//Also, the starting nodes won't change at any point
	
	private Node start;
	private Node end;
	private boolean connected;
	
	/*
	 * Constructs a nodePair that has a start node and an end node
	 * @param s      a node that is the "start"
	 * @param e      a node that is the "end"
	 * @param c      a boolean that represents the connected status
	 * @return       a nodePair with the 2 nodes and a connected boolean
	 */
	public NodePair(Node s, Node e, boolean c) {
		start = s;
		end = e;
		connected = c;
	}//End NodePair constructor
	
	//getter methods
	/*
	 * Checks if the 2 nodes are connected
	 * @return       a boolean based on if the start and end node are connected to each other
	 */
	public boolean isConnected() {
		return connected;
	}//End isConnected

	//setter methods
	/*
	 * Sets the connected status to the parameter c
	 * @param c      a boolean that represents the connected status
	 * @return void
	 */
	public void setConnected(boolean c) {
		connected = c;
	}//End setConnected
	
	/*
	 * Main (description)
	 * @param args
	 * @return void
	 */
	public static void main(String[] args) {

	}//End main

}//End NodePair class
