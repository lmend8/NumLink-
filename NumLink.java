
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
import javafx.stage.Stage;


public class NumLink extends Application {

	Stage window;
	VBox layout;

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

	public void drawOverlay(GraphicsContext gc) {

		drawCircle(gc, 22, 24, "3", Color.GRAY);
		drawCircle(gc, 22, 225, "X", Color.GRAY);
		drawCircle(gc, 222, 22, "4", Color.GRAY);
		drawCircle(gc, 222, 325, "X", Color.GRAY);
		drawCircle(gc, 423, 23, "5", Color.GRAY);
		drawCircle(gc, 423, 425, "X", Color.GRAY);

	}

	private void drawCircle(GraphicsContext gc, int x, int y, String text, Color color) {

		gc.setFill(color);
		gc.fillOval(x, y, 60, 60);
		gc.setFill(Color.WHITE);
		gc.fillText(text, x + 26, y + 32);

	}

	public void drawLine(GraphicsContext gc) {

		gc.setLineWidth(7.0);
		gc.setStroke(Color.YELLOW);
		gc.strokeLine(x, y, x2, y2);

		canvas.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				System.out.println("mouse press");
				x = 55; // x-coordinate of mouse.
				y = 88; // y-coordinate of mouse.
			}
		});

		canvas.setOnMouseDragged(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				System.out.println("mouse dragging");

				x2 = event.getX(); // x-coordinate of mouse.
				y2 = event.getY(); // y-coordinate of mouse.

			}
		});

		canvas.setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				System.out.println("Mouse released");

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

		Scene scene = new Scene(layout, 520, 550);

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

		// canvas.setOnMouseDragged(e -> mouseDrag(e));

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

	public void mouseDrag(MouseEvent e) {

		if (dragging == false)
			return; // Nothing to do because the user isn't drawing.

		// double x = e.getX(); // x-coordinate of mouse.
		// double y = e.getY(); // y-coordinate of mouse.

		gc.setLineWidth(5.0);
		gc.strokeLine(50, 80, 52, 250); // Draw the line.

	} // end mouseDragged()

}