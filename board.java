
//javaFx tools 
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

public class board extends Application {

	Stage window; // stage object to set up the window
	VBox layout; // VBox object to set up the board

	/**
	 * draw method to draw the board for our game
	 */
	public void draw(GraphicsContext gc) {

		gc.setFill(Color.GRAY); // background color.
		gc.fillRect(0, 0, 550, 550); // background for the window with x-cor y-cor width height.

		// loop that draw squares to make the board of 5X5
		for (int x = 10; x < 500; x += 100) {
			for (int y = 10; y < 500; y += 100) {
				gc.strokeRect(x, y, 100, 100);
				gc.setStroke(Color.BLACK);
				gc.setFill(Color.BLACK);
				gc.setLineWidth(.90);
			}
		}

	}

	public static void main(String[] args) {
		launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		// Initialize tools
		window = primaryStage;
		primaryStage.setTitle("NumLink");

		// canvas

		// file menu objects
		Menu fileMenu = new Menu("File");
		Menu editMenu = new Menu("Edit");
		Menu aboutMenu = new Menu("About");

		// Menu items objects
		fileMenu.getItems().add(new MenuItem("New game..."));
		fileMenu.getItems().add(new MenuItem("Open..."));
		fileMenu.getItems().add(new MenuItem("Save game..."));
		fileMenu.getItems().add(new SeparatorMenuItem());
		fileMenu.getItems().add(new MenuItem("Settings..."));
		fileMenu.getItems().add(new SeparatorMenuItem());
		fileMenu.getItems().add(new MenuItem("Exit..."));

		//Canvas API provides a custom texture so we can make our game
		//Canvas is a Node Subclass. 
		Canvas canvas = new Canvas(1000, 1000);
		GraphicsContext gc = canvas.getGraphicsContext2D();
		draw(gc);

		//  add a edit menu tab
		editMenu.getItems().add(new MenuItem("MakeSize..."));

		// add a about tab 
		aboutMenu.getItems().add(new MenuItem("About game"));

		// Main menu bar
		MenuBar menubar = new MenuBar();
		menubar.getMenus().addAll(fileMenu, editMenu, aboutMenu);

		layout = new VBox();// make a layout using VBox object
		layout.getChildren().add(menubar); // we add Menubar to our window
		layout.getChildren().add(canvas); // graphic for our window

		// Scene object to make the size of the window
		Scene scene = new Scene(layout, 550, 550);
		window.setScene(scene);//set the scene object into our window object
		window.show();// display the window

	}
}
