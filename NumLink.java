/* Project Name: NumLink
 * By: Developers MLHL
 * Date Created: 9/22/19
 * Purpose: Creation of a randomized number linking game
 * 
 * Eventually add: @author
 *                 @version
 */

import java.util.*;
public class NumLink {
	//Create object to hold a pair of numbers, such as a node or something of the sort, so that an arraylist can be created to hold them
	//This would allow for a private variable which can be checked if the node pairs are connected?
	
	//How to check if two node pairs are connected to each other
	//Could do a big function, but that would require more indepth code of class creation, so as to not loop between two numbers
	//Could make a function using node locations based on similar data, such as the number they would share
	
	/*
	 * Prints out the grid layout in the terminal
	 * @param arr1   a 2D array that holds all the nodes in a nxn grid
	 */
	public static void printGraph(int[][] arr1) {
		int size = arr1.length;
		for(int i = 0; i < size; i++){
			for(int j = 0; j < size; j++){
				System.out.printf("%3d", arr1[i][j]);
			}//end inner for loop
			System.out.println();
		}//end outer for loop
		System.out.println();
	}//end printGraph
	
	
	/*
	 * Creates a unique node (start or end) that is placed on the array
	 * @param a      the x coordinate for the uniqueNode
	 * @param b      the y coordinate for the uniqueNode
	 * @param v      the number of the node
	 * @param s      the size of the array (n)
	 * @param arr    the array containing nodes
	 * @return Node  a node with the cordinates, value, and is considered to be a starting node
	 */
	public static Node findUniqueTile(int a, int b, int v, int s, ArrayList<Node> arr) {	
		boolean newTile = false;
		boolean foundTile = false;;
		
				//recode this to make this faster for larger sized grids, could use hashmaps or arraylists?
			//should probably make this a function to be called with two integers and an Arraylist of nodes
		//repeats until the tile has a unique pair of coordinates
		while(!newTile) {
			foundTile = false;//resets foundTile value
			
			for(int i = 0; i < arr.size(); i++) {
				if(arr.get(i).getCOne() == a && arr.get(i).getCTwo() == b) {
					foundTile = true;//if the tile already exists, it becomes found, so found is true
				}//end if statement
			}//end for loop
			
			
			if(!foundTile) newTile = true; //if tile isnt found, it is a new tile, which 
			else {//if tile is found, variables should be reset, and the while loop resets again
				a = (int) ((Math.random() * ((s - 1) + 1)) + 0);
				b = (int) ((Math.random() * ((s - 1) + 1)) + 0);
			}//end else statement
		}//end while loop
		
		
		Node x = new Node(a, b, v, true); 
		return x;
	}//end findUniqueTile
	
	/*
	 * Utilizes the functions above to create a gird that has nodes that can be added to the grid
	 * @param args 
	 * @return void
	 */
	public static void main(String[] args) {
		//basic code to test arrays and printing lines
		int k = 4;
		int[][] arr1 = new int[k][k];
		for(int i = 0; i < k; i++) {
			for(int j = 0; j < k; j++) {
				arr1[i][j] = i + j;
				System.out.print(arr1[i][j]);
			}
			System.out.println();
		}

		System.out.println("Hello World"); //simple print test
		System.out.println(); 
		System.out.println(); //spacing added to seperate sections
		
		//first test of user input
		 Scanner girdCreation = new Scanner(System.in);  // Create a Scanner object
		 System.out.println("Enter a number for the desired grid size");
		 int size = girdCreation.nextInt();//reads input from the user using a scanner
		 //sizeAmount.close();  //closes the scanner  //not used since then no other scanners can be openned
		 
		 
		 System.out.println("Enter a number for the desired amount of starting node pairs");
		 int pairCount = girdCreation.nextInt();
		 while(pairCount != 3) {
			 System.out.println("Please enter a different number for the desired amount of starting pairs");
			 pairCount = girdCreation.nextInt();
		 }//end while loop

		 
		//hard coding of an example array to test with, like user input, interface, etc.
		int[][] arr2 = new int[size][size];
		ArrayList<Node> arr3 = new ArrayList<Node>();
		
		while(pairCount > 0) { //creates pairs so that it creates two nodes with the same value
			int cOne = (int) ((Math.random() * ((size - 1) + 1)) + 0);
			int cTwo = (int) ((Math.random() * ((size - 1) + 1)) + 0);
			int cThree = (int) ((Math.random() * ((size - 1) + 1)) + 0);
			int cFour = (int) ((Math.random() * ((size - 1) + 1)) + 0);
			int pairNumber = (int) ((Math.random() * ((size - 1) + 1)) + 1);
			
			Node a = findUniqueTile(cOne, cTwo, pairNumber, size, arr3);
			arr3.add(a);
			Node b = findUniqueTile(cThree, cFour, pairNumber, size, arr3);
			arr3.add(b);
			
			//Node a = new Node(cOne, cTwo, pairNumber, true); 
			//Node b = new Node(cThree, cFour, pairNumber, true);
			
			arr2[a.getCOne()][a.getCTwo()] = pairNumber;
			arr2[b.getCOne()][b.getCTwo()] = pairNumber;
			
			System.out.print("(" + a.getCOne() + "," + a.getCTwo() + "),(" + b.getCOne() + "," + b.getCTwo() + ") = " + pairNumber + "   ");
			
			pairCount = pairCount - 1;
		}
		System.out.println();
		
		//Program checks if all original starting nodes are now connected to each other through lines of repeating numbers
		
		//If all nodes connected, win condition, exits the while loop
		//If not, repeats everything over again
		
		//int count = 0;
		boolean gameWon = false;
		String x = "";
		
		while(!gameWon) {
			printGraph(arr2);

			Scanner tileData = new Scanner(System.in);
			System.out.println("Enter a number for the desired row (0 -> size)");
			int tileOne = tileData.nextInt();
			System.out.println("Enter a number for the desired column (0 -> size)");
			int tileTwo = tileData.nextInt();
			System.out.println("Enter a number to place in the tile");
			int tileNumber = tileData.nextInt();
			
			for(int i = 0; i < arr3.size(); i++) {
				while(arr3.get(i).getCOne() == tileOne && arr3.get(i).getCTwo() == tileTwo) {//fix this line of code to use node.getStarting
					i = 0;
					System.out.println("The desired tile to change is a starting tile, please select another tile");
					System.out.println("Enter a new number for the desired row (0 -> size)");
					tileOne = tileData.nextInt();
					System.out.println("Enter a new number for the desired column (0 -> size)");
					tileTwo = tileData.nextInt();
					System.out.println("Enter a number to place in the tile");
					tileNumber = tileData.nextInt();
				}//end while loop
			}//end for loop	
					
			arr2[tileOne][tileTwo] = tileNumber;//UI for java code development
			
				//Previous win condition
			//count = count + 1;
			//if(count == 3) gameWon = true;
			
			tileData.nextLine();//removes the new line created by nextInt() 
			System.out.println("Enter the word exit if you want the game to end");
			x = tileData.nextLine();
			
			if(x.equals("exit")) gameWon = true;
		}//end while loop
		
		if(!x.equals("exit")) System.out.println("Congrats! You Won!");
	}//end main
}//end NumLink class
