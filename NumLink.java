
import java.io.BufferedReader;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

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
	Node [][] board = new Node[5][5];
	int mousePositionX;
	int mousePositionY;
	private int NORTH;
	private int SOUTH;
	private int WEST;
	private int EAST;

	
	/**
	 * method for loading the levels from a text file.
	 * @param level load the level by number 
	 */

	public void loadLevel(int level) {

		String line = null;	
		String fileName = "level"+ level +".txt"; 
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
			while(line != null){
				String[] tokens = line.split(",");
				for(int i =0; i<board.length; i++) {
					board[i][lineCounter].init();
					if (tokens[i].equals("0")) {
						continue;
					} else if (tokens[i].equals("X")) {
						board[i][lineCounter].setText("X");
						board[i][lineCounter].setColor(Color.GRAY);
						board[i][lineCounter].setxSlotPosition(i);
						board[i][lineCounter].setySlotPosition(lineCounter);
						board[i][lineCounter].setEndNode(true);	
						board[i][lineCounter].setStartNode(false);

					} else {
						board[i][lineCounter].setText(tokens[i]);
						board[i][lineCounter].setColor(Color.GRAY);
						board[i][lineCounter].setxSlotPosition(i);
						board[i][lineCounter].setySlotPosition(lineCounter);
						board[i][lineCounter].setStartNode(true);
						board[i][lineCounter].setEndNode(false);
					}				
				}
				lineCounter ++;
				line = br.readLine();
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	/*
	 * method to detected the node object in our board when the user click on the mouse
	 */
	
	public Node getNodeFromCurrentMousePostion() {

		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				int x = board[i][j].getxSlotPosition() +1;
				int y = board[i][j].getySlotPosition() +1;
				if ( 10 < mousePositionX && mousePositionX < x * 100 -5 &&  10 < mousePositionY  && mousePositionY < y * 100 -5 ) {
					return board[i][j]; 	
				}
			}
		}	
		return null;
	}


	/*
	 * Method to draw the grid in our board. 
	 */

	public void drawGrid(GraphicsContext gc) {
		gc.setFill(Color.SLATEGRAY);
		gc.fillRect(0, 0, 550, 550);

		for (int x = 10; x < 500; x += 100) {
			for (int y = 10; y < 500; y += 100) {
				gc.setFill(Color.BLACK);
				gc.fillRect(x, y, 85, 85);
		
			}
		}
	}

	
  /* 
   * Draws lines and circles by going over the attributes of every node.
   *
   * @param gc Handle to graphics context
   */
	
	public void drawOverlay(GraphicsContext gc) {

		for(int i =0; i<board.length; i++) {
			for(int j = 0; j<board[i].length; j++) {
				
				
				// draw circles. 
				if(board[i][j].isStartNode() || board[i][j].isEndNode()) {
					drawCircle(gc, board[i][j].getxSlotPosition(),board[i][j].getySlotPosition(), board[i][j].getText(), board[i][j].getColor());
				}
			}
		}

	}

	
	/*
	 * Method to draws our node into the board
	 * @param gc  Handle to graphic context
	 * @param xSlotPosition The x slot position in the board. 
	 * @param ySlotPosition The y slot position in the board.
	 * @param text String to indicate whether the node is a startNode or endNode
	 * @param color  Color of the node.
	 */
	private void drawCircle(GraphicsContext gc, int xSlotPosition, int ySlotPosition, String text, Color color) {

		gc.setFill(color);
		gc.fillOval(xSlotPosition * 100+20, ySlotPosition* 100 +20, 60, 60);
		gc.setFill(Color.WHITE);
		gc.setFont(Font.font(20));
		gc.fillText(text, xSlotPosition*100 + 45, ySlotPosition*100 + 55);


	}

	
	public void drawLine(GraphicsContext gc) { 
		//gc.setLineWidth(7.0);
		//gc.setStroke(Color.YELLOW);
		//gc.strokeLine(x, y, x2, y2);
		

		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				//System.out.println("mouse press");
				mousePositionX = (int) e.getX(); // x-coordinate of mouse.
				mousePositionY = (int) e.getY(); // y-coordinate of mouse.
				Node test = getNodeFromCurrentMousePostion();
				 x = test.getxSlotPosition();
				 y = test.getySlotPosition();
				System.out.println("x = "  + x + " y = " + y);
			}
		});

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				//System.out.println("mouse dragging");

				x2 = event.getX(); // x-coordinate of mouse.
				y2 = event.getY(); // y-coordinate of mouse.

			}
		});

		canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				//System.out.println("Mouse released");

			}
		});

	}

	public static void main(String[] args) {
		launch(args);
	}

	private boolean dragging;

	private double x, y, x2, y2;

	private Canvas canvas; // The canvas on which everything is drawn.

	private GraphicsContext gc; // For drawing on the canvas.

	@Override
	public void start(Stage primaryStage) throws Exception {

		for(int i=0; i<board.length; i++) {
			for(int j=0; j<board[i].length; j++) {
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
		MenuItem newFile = new MenuItem("New game...");
		fileMenu.getItems().add(newFile);
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

		Scene scene = new Scene(layout, 505, 530);

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
				Scene about = new Scene(layout2, 520, 520);
				window.setScene(about);
			}
		});

		window.setScene(scene);
		window.show();


		new AnimationTimer() {
			@Override
			public void handle(long currentTimeInNanoSeconds) {

				drawGrid(gc);
				drawOverlay(gc);
				drawLine(gc);
			

			}
		}.start();

		newFile.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {

			}
		});

	}

}