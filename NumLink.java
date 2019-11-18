
import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class NumLink extends Application {

	Stage window;
	VBox layout;
	Node[][] board = new Node[5][5];
	int mousePositionX;
	int mousePositionY;
	public static final int GRID_SIZE = 5;
	public static final int SLOT_SIZE_WITH_BORDER = 100;
	public static final int SLOT_SIZE = SLOT_SIZE_WITH_BORDER - 20;
	public static final int CIRCLE_SIZE = SLOT_SIZE - 20;

	public static final int BOARD_WIDTH = GRID_SIZE * SLOT_SIZE_WITH_BORDER;
	public static final int BOARD_HEIGHT = GRID_SIZE * SLOT_SIZE_WITH_BORDER;

	public static final int SCREEN_WIDTH = BOARD_WIDTH + 20;
	public static final int SCREEN_HEIGHT = BOARD_HEIGHT + 20;

	public static final int NEXT_LEVEL_BUTTON_X = (SCREEN_WIDTH / 2) - 140;
	public static final int NEXT_LEVEL_BUTTON_Y = (SCREEN_HEIGHT / 2) + 10;
	public static final int NEXT_LEVEL_BUTTON_WIDTH = 150;
	public static final int NEXT_LEVEL_BUTTON_HEIGHT = 40;

	public static final int REPEAT_LEVEL_BUTTON_X = (SCREEN_WIDTH / 2) + 30;
	public static final int REPEAT_LEVEL_BUTTON_Y = (SCREEN_HEIGHT / 2) + 10;
	public static final int REPEAT_LEVEL_BUTTON_WIDTH = 110;
	public static final int REPEAT_LEVEL_BUTTON_HEIGHT = 40;

	public static final int NORTH = 0;
	public static final int EAST = 1;
	public static final int SOUTH = 2;
	public static final int WEST = 3;

	private int currentLevel = 1;
	private Node currentNode;
	private Node previousNode;
	private Node currentSolutionEndNode;
	private Node currentSolutionStartNode;
	private int numberOfSlotsLeft;
	private int numberOfSolutionsLeft;
	private boolean levelSolved;
	private List<Stack<Node>> solutionStacks;

	public static void main(String[] args) {
		launch(args);
	}

	private Canvas canvas; // The canvas on which everything is drawn.

	private GraphicsContext gc; // For drawing on the canvas.

	@Override
	public void start(Stage primaryStage) throws Exception {

		solutionStacks = new ArrayList<Stack<Node>>();

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				board[i][j] = new Node();
			}
		}

		loadLevel(1);

		// Initialize tools
		window = primaryStage;
		primaryStage.setTitle("NumLink");
		// Group root = new Group();

		// canvas

		// file menu
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		Menu aboutMenu = new Menu("About");

		// Menu items
		MenuItem openFile = new MenuItem("Open...");
		fileMenu.getItems().add(openFile);
		fileMenu.getItems().add(new SeparatorMenuItem());
		MenuItem settings = new MenuItem("Settings...");
		editMenu.getItems().add(settings);
		editMenu.getItems().add(new SeparatorMenuItem());
		MenuItem exitFile = new MenuItem("Exit...");
		fileMenu.getItems().add(exitFile);

		exitFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Platform.exit();
				;
			}
		});

		canvas = new Canvas(1100, 1100);

		gc = canvas.getGraphicsContext2D();

		// edit menu
		editMenu.getItems().add(new MenuItem("MakeSize..."));

		// about menu
		aboutMenu.getItems().add(new MenuItem("About game"));

		// Main menu bar
		MenuBar menubar = new MenuBar();
		menubar.getMenus().addAll(fileMenu, editMenu, aboutMenu);

		layout = new VBox();
		layout.getChildren().add(menubar);
		layout.getChildren().add(canvas);

		Scene scene = new Scene(layout, 520, 545);

		aboutMenu.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				System.out.println("about window action");
				VBox layout2 = new VBox(20);
				Label label1 = new Label("Numlink Game\nCreated by Developers MLHL\n");
				Button button = new Button("close");
				button.setTranslateX(0);
				button.setTranslateY(120);
				layout2.getChildren().addAll(button, label1);
				button.setOnAction(e -> window.setScene(scene));
				Scene about = new Scene(layout2, 550, 550);
				window.setScene(about);
			}
		});

		window.setScene(scene);
		window.show();

		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				// System.out.println("mouse press");
				mousePositionX = (int) e.getSceneX(); // x-coordinate of mouse.
				mousePositionY = (int) e.getSceneY(); // y-coordinate of mouse.
				if (!levelSolved) {
					enterSolveMode();
				} else {
					// go to next level on mouse click
					try {
						checkButtonPushed();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
		});

		scene.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (!levelSolved) {
					mousePositionX = (int) event.getSceneX();
					mousePositionY = (int) event.getSceneY();
					searchNextNode();
				}
			}
		});

		scene.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {

				if (!levelSolved) {
					exitSolveMode();
				}
			}
		});

		new AnimationTimer() {
			@Override
			public void handle(long currentTimeInNanoSeconds) {

				drawGrid(gc);
				drawOverlay(gc);

				if (levelSolved) {
					drawLevelPassedScreen(gc);
				}

			}
		}.start();

	}

	/**
	 * method for loading the levels from a text file.
	 * 
	 * @param level load the level by number
	 */

	public void loadLevel(int level) {

		numberOfSolutionsLeft = 0;
		levelSolved = false;
		currentSolutionStartNode = null;
		currentSolutionEndNode = null;

		String line = null;
		String fileName = "level" + level + ".txt";
		File file = new File(fileName);
		FileReader fr = null;
		try {
			fr = new FileReader(file);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		BufferedReader br = new BufferedReader(fr);
		try {
			line = br.readLine();

			int lineCounter = 0;
			while (line != null) {
				String[] tokens = line.split(",");
				for (int i = 0; i < board.length; i++) {
					board[i][lineCounter].initialize();
					board[i][lineCounter].setxSlotPosition(i);
					board[i][lineCounter].setySlotPosition(lineCounter);

					if (tokens[i].equals("0")) {
						continue;
					} else if (tokens[i].equals("X")) {
						board[i][lineCounter].setText("X");
						board[i][lineCounter].setColor(Color.GRAY);
						board[i][lineCounter].setEndNode(true);
						board[i][lineCounter].setStartNode(false);

					} else {
						int nodeNumber = 0;
						try {
							nodeNumber = Integer.parseInt(tokens[i]);
						} catch (NumberFormatException e) {

						}
						if (nodeNumber != 0) {
							board[i][lineCounter].setNumber(nodeNumber);
							board[i][lineCounter].setStartNode(true);
							board[i][lineCounter].setUncrossable(true);
							numberOfSolutionsLeft++;
						}
						board[i][lineCounter].setText(tokens[i]);
						board[i][lineCounter].setColor(Color.GRAY);
						board[i][lineCounter].setStartNode(true);
						board[i][lineCounter].setEndNode(false);
					}
				}
				lineCounter++;
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		solutionStacks.clear();

	}

	/**
	 * Returns the appropriate node color for the given node number.
	 *
	 * @param nodeNumber Number corresponding to parent solution node
	 * @return Appropriate color for node number
	 */
	private Color getColorForNodeNumber(int nodeNumber) {
		Color returnColor = Color.GRAY;

		switch (nodeNumber) {
		case 2:
			returnColor = new Color(0.64, 0.77, 0.27, 1.0);// green
			break;
		case 3:
			returnColor = new Color(0.2, 0.59, 0.85, 1.0);// blue
			break;
		case 4:
			returnColor = new Color(1.0, 0.8, 0.0, 1.0);// yellow
			break;
		case 5:
			returnColor = new Color(0.90, 0.29, 0.25, 1.0);// red
			break;
		}
		return returnColor;
	}

	/**
	 * Returns the handle to the node the mouse has currently picked one, null
	 * otherwise. Value can be null if mouse is within slot borders for example.
	 *
	 * @return Handle to node if mouse has picked one, null otherwise
	 */
	private Node getNodeFromCurrentMousePosition() {
		// check if mouse is within slot
		int offsetMouseX = (int) mousePositionX % SLOT_SIZE_WITH_BORDER;

		// offset menu on Y
		int offsetMouseY = (int) (mousePositionY - 30) % SLOT_SIZE_WITH_BORDER;

		// System.out.println("currentMouseY=" + currentMouseY);
		// System.out.println("offsetMouseY=" + offsetMouseY);

		if (offsetMouseX > 20 && offsetMouseX < 100 && offsetMouseY > 20 && offsetMouseY < 100) {
			int x = (int) (mousePositionX / SLOT_SIZE_WITH_BORDER);
			int y = (int) ((mousePositionY - 30) / SLOT_SIZE_WITH_BORDER);
			// System.out.println("Returned node " + x + "," + y);
			return board[x][y];
		}

		// System.out.println("Returned null");
		return null;
	}

	/*
	 * Method to draw the grid in our board.
	 */

	public void drawGrid(GraphicsContext gc) {
		// draw background color
		gc.setFill(new Color(0.17, 0.19, 0.24, 1.0));
		gc.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		// draw square slots
		gc.setFill(new Color(0.125, 0.15, 0.18, 1.0));
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				gc.fillRect((i * SLOT_SIZE_WITH_BORDER) + 20, (j * SLOT_SIZE_WITH_BORDER) + 20, SLOT_SIZE, SLOT_SIZE);
			}
		}
	}

	/*
	 * Draws lines and circles by going over the attributes of every node.
	 *
	 * @param gc Handle to graphics context
	 */

	/**
	 * Draws lines and circles by going over the attributes of every node.
	 *
	 * @param gc Handle to graphics context
	 */
	private void drawOverlay(GraphicsContext gc) {
		for (int i = 0; i < GRID_SIZE; i++) {
			for (int j = 0; j < GRID_SIZE; j++) {
				Node node = board[j][i];

				// draw lines first
				if (node.isNorthLine()) {
					drawLine(gc, j, i, NORTH, node.getColor());
				}
				if (node.isEastLine()) {
					drawLine(gc, j, i, EAST, node.getColor());
				}
				if (node.isSouthLine()) {
					drawLine(gc, j, i, SOUTH, node.getColor());
				}
				if (node.isWestLine()) {
					drawLine(gc, j, i, WEST, node.getColor());
				}

				// draw circles last
				if (node.isStartNode() || node.isEndNode()) {
					drawCircle(gc, j, i, node.getText(), node.getColor());
				}
			}
		}
	}

	/*
	 * Method to draws our node into the board
	 * 
	 * @param gc Handle to graphic context
	 * 
	 * @param xSlotPosition The x slot position in the board.
	 * 
	 * @param ySlotPosition The y slot position in the board.
	 * 
	 * @param text String to indicate whether the node is a startNode or endNode
	 * 
	 * @param color Color of the node.
	 */
	private void drawCircle(GraphicsContext gc, int xSlotPosition, int ySlotPosition, String text, Color color) {

		int circlePaddingFromUpperCorner = 30;
		int textPaddingFromUpperCornerX = 49;
		int textPaddingFromUpperCornerY = 73;

		// draw circle
		gc.setFill(color);
		gc.fillOval(xSlotPosition * SLOT_SIZE_WITH_BORDER + circlePaddingFromUpperCorner,
				ySlotPosition * SLOT_SIZE_WITH_BORDER + circlePaddingFromUpperCorner, CIRCLE_SIZE, CIRCLE_SIZE);

		// draw text
		gc.setFill(Color.WHITE);
		gc.setFont(new Font("Arial", 35.0));
		gc.fillText(text, xSlotPosition * SLOT_SIZE_WITH_BORDER + textPaddingFromUpperCornerX,
				ySlotPosition * SLOT_SIZE_WITH_BORDER + textPaddingFromUpperCornerY);
	}

	/**
	 *
	 * @param gc            Handle to graphics context
	 * @param xSlotPosition X coordinate for the slot whose center will be the line
	 *                      origin. 0,0 is upper left corner
	 * @param ySlotPosition Y coordinate for the slot whose center will be the line
	 *                      origin. 0,0 is upper left corner
	 * @param direction     One of NORTH, SOUTH, EAST or WEST
	 * @param color         Color for the line
	 */
	private void drawLine(GraphicsContext gc, int xSlotPosition, int ySlotPosition, int direction, Color color) {
		double linePaddingForCenterPoint = 50.0;
		double lineWidth = 20.0;
		double lineLength = 50.0;

		gc.setFill(color);

		if (direction == NORTH) {
			gc.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
					ySlotPosition * SLOT_SIZE_WITH_BORDER + 10.0, lineWidth, lineLength);
		} else if (direction == EAST) {
			gc.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
					ySlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint, lineLength + 10.0, lineWidth);
		} else if (direction == SOUTH) {
			gc.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
					ySlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint + 10.0, lineWidth, lineLength);
		} else if (direction == WEST) {
			gc.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + 10.0,
					ySlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint, lineLength + 10.0, lineWidth);
		}
	}

	/**
	 * This method is called on mouse drag. It computes what actions need to be
	 * taken in order to add to solution stack and appropriately draw the overlay.
	 */

	private void searchNextNode() {
		Node node = getNodeFromCurrentMousePosition();

		System.out.println("node=" + node);
		System.out.println("currentNode=" + currentNode);
		if (node != null)
			System.out.println("node.IsUncrossable()=" + node.isUncrossable());
		if (node == null || previousNode == null || node == currentNode || node.isUncrossable()
				|| currentSolutionStartNode == null) {
			System.out.println("Node returned 0");
			return;
		}
		System.out.println("");

		// protect from diagonal movement
		if ((node.getxSlotPosition() > previousNode.getxSlotPosition()
				|| node.getxSlotPosition() < previousNode.getxSlotPosition())
				&& (node.getySlotPosition() > previousNode.getySlotPosition()
						|| node.getySlotPosition() < previousNode.getySlotPosition())) {
			System.out.println("Node returned 1");
			return;
		}

		// protect from skipping
		if (Math.abs(node.getxSlotPosition() - previousNode.getxSlotPosition()) >= 2
				|| Math.abs(node.getySlotPosition() - previousNode.getySlotPosition()) >= 2) {
			System.out.println("Node returned 2");
			return;
		}

		// if node is already visited, you need to undo the stack down to this node
		System.out.println("node.isVisited()=" + node.isVisited());
		if (node.isVisited()) {
			currentNode = node;
			undoStackToNode(node, true);
			previousNode = node;
			currentSolutionEndNode = null;// reset end node
			return;
		}

		// check if we have number of slots left
		if (numberOfSlotsLeft <= 0) {
			System.out.println("Node returned 4");
			return;
		}

		// check if node is ending and we don't have the right number of slots left
		// or node is already solved
		if ((node.isEndNode() && numberOfSlotsLeft != 1) || node.isSolved()) {
			System.out.println("Node returned 5");
			return;
		}

		node.setVisited(true);
		currentNode = node;
		node.setColor(currentSolutionStartNode.getColor());
		addNodeToSolutionStacks(node);

		// check if node is ending node, if so set it as solved
		if (node.isEndNode()) {
			currentSolutionEndNode = node;
			currentSolutionEndNode.setSolved(true);
			currentSolutionStartNode.setSolved(true);
		}

		// draw line from previous node to this node
		// determine if this node is north, south, east or west of previous node
		if (currentNode.getySlotPosition() < previousNode.getySlotPosition()) {
			currentNode.setSouthLine(true);
			previousNode.setNorthLine(true);
		} else if (currentNode.getySlotPosition() > previousNode.getySlotPosition()) {
			currentNode.setNorthLine(true);
			previousNode.setSouthLine(true);
		} else if (currentNode.getxSlotPosition() < previousNode.getxSlotPosition()) {
			currentNode.setEastLine(true);
			previousNode.setWestLine(true);
		} else if (currentNode.getxSlotPosition() > previousNode.getxSlotPosition()) {
			currentNode.setWestLine(true);
			previousNode.setEastLine(true);
		}

		// advance previous node
		previousNode = currentNode;
	}

	/**
	 * Adds the given node to the solution/change stack in order to be able to trace
	 * back our steps when we need to undo them, e.g., going back a node or
	 * releasing the mouse without having solved a node pair
	 *
	 * @param node Handle to node to be added to the stack
	 */
	private void addNodeToSolutionStacks(Node node) {
		// System.out.println("currentSolutionParentNode=" +
		// currentSolutionStartingNode);

		for (int i = 0; i < solutionStacks.size(); i++) {
			Stack<Node> solutionStack = solutionStacks.get(i);
			// System.out.println("Trying to add node, solution stack size=" +
			// solutionStack.size());
			// System.out.println("solutionStack.get(0)=" + solutionStack.get(0));
			// look at first node of solution
			if (currentSolutionStartNode == solutionStack.get(0)) {
				// node found, just add to stack
				// System.out.println("Found solution node");
				solutionStack.push(node);
				numberOfSlotsLeft--;
				return;
			}
		}

		// stack not found, create new one and add to solution stacks
		Stack<Node> solutionStack = new Stack<Node>();
		solutionStack.push(node);
		numberOfSlotsLeft--;
		solutionStacks.add(solutionStack);
	}

	/**
	 * Reverts changes saved in the node stack down to the given node.
	 *
	 * @param node      Which node to undo the stack down to
	 * @param softReset True if a soft reset should be performed on the parent node.
	 *                  A soft reset is done when dragging back down to the parent
	 *                  node
	 */
	private void undoStackToNode(Node node, boolean softReset) {
		// find current solution node on the stack and undo solution stack
		for (int i = 0; i < solutionStacks.size(); i++) {
			Stack<Node> solutionStack = solutionStacks.get(i);
			if (solutionStack != null && currentSolutionStartNode == solutionStack.get(0)) {
				Node previousNodeInStack = null;
				// undo stack
				while (!solutionStack.isEmpty()) {
					Node currentNodeInStack = solutionStack.peek();
					if (currentNodeInStack == node) {
						break;
					}

					if (currentNodeInStack.isEndNode()) {
						currentNodeInStack.resetEndNode();
					} else {
						currentNodeInStack.resetLine();
					}

					previousNodeInStack = currentNodeInStack;
					solutionStack.pop();
					numberOfSlotsLeft++;
					// System.out.println("Popped node, solution stack size=" +
					// solutionStack.size());
				}

				// remove solution from the list if given node was parent node
				if (node.isStartNode()) {
					if (softReset) {
						node.softReset();
					} else {
						node.resetStartNode();
						solutionStacks.remove(i);
					}
				} else {
					// erase lines coming into this node from previous node in stack
					if (previousNodeInStack.getxSlotPosition() > node.getxSlotPosition())
						currentNode.setEastLine(false);
					else if (previousNodeInStack.getxSlotPosition() < node.getxSlotPosition())
						currentNode.setWestLine(false);
					else if (previousNodeInStack.getySlotPosition() > node.getySlotPosition())
						currentNode.setSouthLine(false);
					else if (previousNodeInStack.getySlotPosition() < node.getySlotPosition())
						currentNode.setNorthLine(false);
				}

				break;
			}
		}
	}

	/**
	 * This method is called on mouse press. It initializes global variables and
	 * solution stack.
	 */
	private void enterSolveMode() {
		Node node = getNodeFromCurrentMousePosition();

		if (node != null && node.isStartNode()) {
			currentNode = node;
			previousNode = node;
			currentSolutionStartNode = node;
			currentSolutionEndNode = null;
			numberOfSlotsLeft = node.getNumber();

			// check if node is already solved
			if (node.isSolved()) {
				undoStackToNode(node, false);
				numberOfSolutionsLeft++;
			}

			node.setColor(getColorForNodeNumber(node.getNumber()));
			node.setVisited(true);
			node.setUncrossable(false);
			addNodeToSolutionStacks(node);
		}
	}

	/**
	 * This method is called when the mouse is released in order to determine if we
	 * need to undo any solution/changes.
	 */
	private void exitSolveMode() {
		if (currentSolutionStartNode != null && currentSolutionEndNode == null) {
			undoStackToNode(currentSolutionStartNode, false);
		} else if (currentSolutionStartNode != null && currentSolutionEndNode != null) {
			if (numberOfSolutionsLeft > 0) {
				System.out.println("Decrease number of solutions left");
				numberOfSolutionsLeft--;
			}
			markCurrentSolutionUncrossable();
			System.out.println("Number of solutions left: " + numberOfSolutionsLeft);
		}

		currentSolutionStartNode = null;
		currentSolutionEndNode = null;

		if (numberOfSolutionsLeft == 0) {
			System.out.println("CONGRATULATIONS, YOU PASSED TO THE NEXT LEVEL!");
			levelSolved = true;
		}
	}

	/**
	 * Makes all nodes in the current solution uncrossable
	 */
	private void markCurrentSolutionUncrossable() {
		// find current solution node on the stack and mark all nodes uncrossable
		for (int i = 0; i < solutionStacks.size(); i++) {
			Stack<Node> solutionStack = solutionStacks.get(i);
			if (solutionStack != null && currentSolutionStartNode == solutionStack.get(0)) {
				for (int j = 0; j < solutionStack.size(); j++) {
					solutionStack.get(j).setUncrossable(true);
				}
			}
		}
	}

	private void drawLevelPassedScreen(GraphicsContext graphicsContext) {
		graphicsContext.setFill(new Color(0.17, 0.19, 0.24, 0.5));
		graphicsContext.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

		graphicsContext.setFill(Color.WHITE);
		graphicsContext.setFont(new Font("Arial", 50.0));
		graphicsContext.fillText("Great Job!", (SCREEN_WIDTH / 2.0) - 120, (SCREEN_HEIGHT / 2.0) - 15);

		// next level button
		graphicsContext.setFill(new Color(0.5, 0.5, 0.5, 0.5));
		graphicsContext.fillRect(NEXT_LEVEL_BUTTON_X, NEXT_LEVEL_BUTTON_Y, NEXT_LEVEL_BUTTON_WIDTH,
				NEXT_LEVEL_BUTTON_HEIGHT);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.setFont(new Font("Arial", 28.0));
		graphicsContext.fillText("Next Level", (SCREEN_WIDTH / 2.0) - 130, (SCREEN_HEIGHT / 2.0) + 40);

		// repeat button
		graphicsContext.setFill(new Color(0.5, 0.5, 0.5, 0.5));
		graphicsContext.fillRect(REPEAT_LEVEL_BUTTON_X, REPEAT_LEVEL_BUTTON_Y, REPEAT_LEVEL_BUTTON_WIDTH,
				REPEAT_LEVEL_BUTTON_HEIGHT);
		graphicsContext.setFill(Color.WHITE);
		graphicsContext.setFont(new Font("Arial", 28.0));
		graphicsContext.fillText("Repeat", (SCREEN_WIDTH / 2.0) + 40, (SCREEN_HEIGHT / 2.0) + 40);
	}

	private void checkButtonPushed() throws IOException {
		// if next level button pushed
		if (mousePositionX >= NEXT_LEVEL_BUTTON_X && mousePositionX <= (NEXT_LEVEL_BUTTON_X + NEXT_LEVEL_BUTTON_WIDTH)
				&& (mousePositionY - 30) >= NEXT_LEVEL_BUTTON_Y
				&& (mousePositionY - 30) <= (NEXT_LEVEL_BUTTON_Y + NEXT_LEVEL_BUTTON_HEIGHT)) {
			System.out.println("Next level button pushed");
			currentLevel++;
			loadLevel(currentLevel);
		}
		// if repeat button pushed
		else if (mousePositionX >= REPEAT_LEVEL_BUTTON_X
				&& mousePositionX <= (REPEAT_LEVEL_BUTTON_X + REPEAT_LEVEL_BUTTON_WIDTH)
				&& (mousePositionY - 30) >= REPEAT_LEVEL_BUTTON_Y
				&& (mousePositionY - 30) <= (REPEAT_LEVEL_BUTTON_Y + REPEAT_LEVEL_BUTTON_HEIGHT)) {
			System.out.println("Repeat level button pushed");
			loadLevel(currentLevel);
		}
	}

}