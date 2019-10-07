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
	
	public NodePair(Node s, Node e, boolean c) {
		start = s;
		end = e;
		connected = c;
	}
	
	//getter methods
	public boolean isConnected() {
		return connected;
	}

	//setter methods
	public void setConnected(boolean c) {
		connected = c;
	}
	
	public static void main(String[] args) {

	}

}
