/*
 * Copyright 2019 Luis Mendoza
 *
 * This library is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as published by the
 * Free Software Foundation; either version 2.1 of the License, or (at your
 * option) any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library; if not, write to the Free Software Foundation Inc.,
 * 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA
 */

package net.numlink;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * This class implements the main game functionality for NumLink.
 *
 * @author Luis Mendoza
 */
public class NumLink extends Application
{
  public static final int GRID_SIZE = 5;
  public static final int SLOT_SIZE_WITH_BORDER = 100;
  public static final int SLOT_SIZE = SLOT_SIZE_WITH_BORDER - 20;
  public static final int CIRCLE_SIZE = SLOT_SIZE - 20;

  public static final int BOARD_WIDTH = GRID_SIZE * SLOT_SIZE_WITH_BORDER;
  public static final int BOARD_HEIGHT = GRID_SIZE * SLOT_SIZE_WITH_BORDER;

  public static final int SCREEN_WIDTH = BOARD_WIDTH + 20;
  public static final int SCREEN_HEIGHT = BOARD_HEIGHT + 20;

  public static final int NEXT_LEVEL_BUTTON_X = (SCREEN_WIDTH/2) - 140;
  public static final int NEXT_LEVEL_BUTTON_Y = (SCREEN_HEIGHT/2) + 10;
  public static final int NEXT_LEVEL_BUTTON_WIDTH = 150;
  public static final int NEXT_LEVEL_BUTTON_HEIGHT = 40;

  public static final int REPEAT_LEVEL_BUTTON_X = (SCREEN_WIDTH/2) + 30;
  public static final int REPEAT_LEVEL_BUTTON_Y = (SCREEN_HEIGHT/2) + 10;
  public static final int REPEAT_LEVEL_BUTTON_WIDTH = 110;
  public static final int REPEAT_LEVEL_BUTTON_HEIGHT = 40;

  public static final int NORTH = 0;
  public static final int EAST = 1;
  public static final int SOUTH = 2;
  public static final int WEST = 3;

  private Node[][] nodes;
  private FPSCounter fpsCounter;
  private AudioClip music;
  private List<Stack<Node>> solutionStacks;
  private Node currentSolutionStartNode;
  private Node currentSolutionEndNode;
  private Node currentNode;
  private Node previousNode;
  private double currentMouseX;
  private double currentMouseY;
  private int numberOfSlotsLeft;
  private int numberOfSolutionsLeft;
  private boolean levelSolved;
  private int currentLevel;

  /**
   * Override method for JavaFX Application. This method is called once at the
   * beginning of the program. It initializes all variables.
   *
   * @param stage Handle to JavaFX stage
   */
  @Override
  public void start(Stage stage)
  {
    Thread thread = new Thread(fpsCounter);
    thread.start();

    nodes = new Node[GRID_SIZE][GRID_SIZE];
    fpsCounter = new FPSCounter();
    solutionStacks = new ArrayList<Stack<Node>>();
    currentMouseX = 0.0;
    currentMouseY = 0.0;
    currentLevel = 1;

    Group root = new Group();
    Scene scene = new Scene(root);
    stage.setTitle("NumLink");
    stage.setScene(scene);

    // instantiate nodes
    for (int i = 0; i < GRID_SIZE; i++)
    {
      for (int j = 0; j < GRID_SIZE; j++)
      {
        nodes[j][i] = new Node();
      }
    }

    // load level
    try
    {
      loadLevel("level1.txt");
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    // create menus
    Menu fileMenu = new Menu("File");
    Menu editMenu = new Menu("Edit");
    Menu aboutMenu = new Menu("About");
    
    // create file menu items
    MenuItem newGame = new MenuItem("New game");
    newGame.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        try
        {
          loadLevel("level1.txt");
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }			
    });
    
    MenuItem openLevel = new MenuItem("Open level...");
    openLevel.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent event)
      {
        try
        {
          FileChooser fileChooser = new FileChooser();
          fileChooser.setTitle("Open Resource File");
          fileChooser.getExtensionFilters().addAll(
            new ExtensionFilter("Text Files", "*.txt"),
            new ExtensionFilter("All Files", "*.*"));
          File selectedFile = fileChooser.showOpenDialog(stage);
          loadLevel(selectedFile.getAbsolutePath());
        }
        catch (IOException e)
        {
          e.printStackTrace();
        }
      }
    });
    
    MenuItem exit = new MenuItem("Exit");
    exit.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        Platform.exit();
      }
    });

    // add menu items to menu
    fileMenu.getItems().add(newGame);
    fileMenu.getItems().add(openLevel);
    fileMenu.getItems().add(exit);

    MenuItem configuration = new MenuItem("Configuration...");
    editMenu.getItems().add(configuration);

    MenuItem about = new MenuItem("About NumLink...");
    about.setOnAction(new EventHandler<ActionEvent>()
    {
      @Override
      public void handle(ActionEvent e)
      {
        Label aboutLabel1 = new Label("NumLink");
        aboutLabel1.setFont(Font.font("Arial", FontWeight.BOLD, 35.0));
        Label aboutLabel2 = new Label("Created by");
        Label aboutLabel3 = new Label("Luis Mendoza");
        Label aboutLabel4 = new Label("Luis Mendoza");
        Label aboutLabel5 = new Label("Luis Mendoza");
        Label aboutLabel6 = new Label("Luis Mendoza");
        VBox aboutVBox = new VBox();
        aboutVBox.setAlignment(Pos.CENTER);
        aboutVBox.getChildren().add(aboutLabel1);
        aboutVBox.getChildren().add(aboutLabel2);
        aboutVBox.getChildren().add(aboutLabel3);
        aboutVBox.getChildren().add(aboutLabel4);
        aboutVBox.getChildren().add(aboutLabel5);
        aboutVBox.getChildren().add(aboutLabel6);
        Scene secondScene = new Scene(aboutVBox, 300, 200);

        // New window (Stage)
        Stage newWindow = new Stage();
        newWindow.setTitle("About");
        newWindow.setScene(secondScene);

        // Specifies the modality for new window.
        newWindow.initModality(Modality.WINDOW_MODAL);

        // Specifies the owner Window (parent) for new window
        newWindow.initOwner(stage);

        // Set position of second window, related to primary window.
        newWindow.setX(stage.getX() + 130);
        newWindow.setY(stage.getY() + 150);

        newWindow.show();
      }
    });
    
    aboutMenu.getItems().add(about);

    // create a menubar
    MenuBar menuBar = new MenuBar();
    menuBar.getMenus().add(fileMenu);
    menuBar.getMenus().add(editMenu);
    menuBar.getMenus().add(aboutMenu);

    Canvas canvas = new Canvas(SCREEN_WIDTH, SCREEN_HEIGHT);

    // create a VBox
    VBox vBox = new VBox();
    vBox.getChildren().add(menuBar);
    vBox.getChildren().add(canvas);

    root.getChildren().add(vBox);

    GraphicsContext graphicsContext = canvas.getGraphicsContext2D();

    // play background music
    music = new AudioClip(new File("music.mp3").toURI().toString());
    music.play();

    /**
     * Anonymous handler. Gets called on mouse button pressed.
     */
    scene.setOnMousePressed(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent e)
      {
        currentMouseX = e.getSceneX();
        currentMouseY = e.getSceneY();

        if (!levelSolved)
        {
          enterSolveMode();
        }
        else
        {
          // go to next level on mouse click
          checkButtonPushed();
        }
      }
    });

    /**
     * Anonymous handler. Gets called on mouse button released.
     */
    scene.setOnMouseReleased(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent e)
      {
        if (!levelSolved)
        {
          exitSolveMode();
        }
      }
    });

    /**
     * Anonymous handler. Gets called when mouse is dragged.
     */
    scene.setOnMouseDragged(new EventHandler<MouseEvent>()
    {
      @Override
      public void handle(MouseEvent e)
      {
        if (!levelSolved)
        {
          currentMouseX = e.getSceneX();
          currentMouseY = e.getSceneY();
          searchNextNode();
        }
      }
    });

    /**
     * Anonymous animation timer. This method gets called on every frame
     * (optimally 60 per second).
     */
    new AnimationTimer()
    {
      @Override
      public void handle(long currentTimeInNanoseconds)
      {
        fpsCounter.incrementFPS();
        drawGrid(graphicsContext);
        drawOverlay(graphicsContext);

        if (levelSolved)
        {
          drawLevelPassedScreen(graphicsContext);
        }
      }
    }.start();

    // show stage
    stage.show();
  }

  /**
   * Override method for JavaFX Application. This method is called once before
   * the program exits. Performs cleanup.
   */
  @Override
  public void stop()
  {
    music.stop();
    fpsCounter.stopRun();
  }

  /**
   * Main method. Calls JavaFX launch method and relinquishes execution thread.
   *
   * @param args Command line argument list
   */
  public static void main(String[] args)
  {
    launch(args);
  }

  /**
   * Loads a level from file. A level is specified by a comma delimited text
   * file where the rows and columns match the game grid. A value of 0 specifies
   * an empty slot in the board.
   *
   * @param levelFilePath Path to level file
   */
  private void loadLevel(String levelFilePath) throws IOException
  {
    File file = new File(levelFilePath);
    BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
    numberOfSolutionsLeft = 0;
    levelSolved = false;
    currentSolutionStartNode = null;
    currentSolutionEndNode = null;

    String line;
    int lineNumber = 0;
    while ((line = bufferedReader.readLine()) != null)
    {
      String[] cells = line.split(",");
      for (int cellNumber = 0; cellNumber < cells.length; cellNumber++)
      {
        String cell = cells[cellNumber];
        nodes[cellNumber][lineNumber].initialize();
        nodes[cellNumber][lineNumber].setXSlotPosition(cellNumber);
        nodes[cellNumber][lineNumber].setYSlotPosition(lineNumber);
        if (cell.equals("0"))
        {
          continue;
        }
        else
        {
          nodes[cellNumber][lineNumber].setText(cell);
          nodes[cellNumber][lineNumber].setColor(Color.GRAY);

          int nodeNumber = 0;
          try
          {
            nodeNumber = Integer.parseInt(cell);
          }
          catch(NumberFormatException e)
          {
            //e.printStackTrace();
          }

          if (nodeNumber != 0)
          {
            nodes[cellNumber][lineNumber].setNumber(nodeNumber);
            nodes[cellNumber][lineNumber].setStartNode(true);
            nodes[cellNumber][lineNumber].setUncrossable(true);
            numberOfSolutionsLeft++;
          }
          else
          {
            nodes[cellNumber][lineNumber].setEndingNode(true);
          }
        }
      }
      lineNumber++;
    }
    
    solutionStacks.clear();
  }

  /**
   * Draws the board grid.
   *
   * @param graphicsContext Handle to graphics context
   */
  private void drawGrid(GraphicsContext graphicsContext)
  {
    // draw background color
    graphicsContext.setFill(new Color(0.17, 0.19, 0.24, 1.0));
    graphicsContext.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    // draw square slots
    graphicsContext.setFill(new Color(0.125, 0.15, 0.18, 1.0));
    for (int i = 0; i < GRID_SIZE; i++)
    {
      for (int j = 0; j < GRID_SIZE; j++)
      {
        graphicsContext.fillRect((i * SLOT_SIZE_WITH_BORDER) + 20,
                                 (j * SLOT_SIZE_WITH_BORDER) + 20,
                                 SLOT_SIZE, SLOT_SIZE);
      }
    }
  }

  /**
   * Draws lines and circles by going over the attributes of every node.
   *
   * @param graphicsContext Handle to graphics context
   */
  private void drawOverlay(GraphicsContext graphicsContext)
  {
    for (int i = 0; i < GRID_SIZE; i++)
    {
      for (int j = 0; j < GRID_SIZE; j++)
      {
        Node node = nodes[j][i];

        // draw lines first
        if (node.isNorthLine())
        {
          drawLine(graphicsContext, j, i, NORTH, node.getColor());
        }
        if (node.isEastLine())
        {
          drawLine(graphicsContext, j, i, EAST, node.getColor());
        }
        if (node.isSouthLine())
        {
          drawLine(graphicsContext, j, i, SOUTH, node.getColor());
        }
        if (node.isWestLine())
        {
          drawLine(graphicsContext, j, i, WEST, node.getColor());
        }

        // draw circles last
        if (node.isStartNode() || node.isEndNode())
        {
          drawCircle(graphicsContext, j, i, node.getText(), node.getColor());
        }
      }
    }
  }

  /**
   * Draws a circle at the given x-y coordinates with the specific text given.
   *
   * @param graphicsContext Handle to graphics context
   * @param xSlotPosition X coordinate for the slot to draw the circle in. 0,0 is upper
   * left corner
   * @param ySlotPosition Y coordinate for the slot to draw the circle in. 0,0 is upper
   * left corner
   * @param text Label for the circle
   * @param color Color for the circle
   */
  private void drawCircle(GraphicsContext graphicsContext, int xSlotPosition, int ySlotPosition, String text, Color color)
  {
    int circlePaddingFromUpperCorner = 30;
    int textPaddingFromUpperCornerX = 49;
    int textPaddingFromUpperCornerY = 73;

    // draw circle
    graphicsContext.setFill(color);
    graphicsContext.fillOval(xSlotPosition * SLOT_SIZE_WITH_BORDER + circlePaddingFromUpperCorner,
                             ySlotPosition * SLOT_SIZE_WITH_BORDER + circlePaddingFromUpperCorner,
                             CIRCLE_SIZE, CIRCLE_SIZE);

    // draw text
    graphicsContext.setFill(Color.WHITE);
    graphicsContext.setFont(new Font("Arial", 35.0));
    graphicsContext.fillText(text,
                             xSlotPosition * SLOT_SIZE_WITH_BORDER + textPaddingFromUpperCornerX,
                             ySlotPosition * SLOT_SIZE_WITH_BORDER + textPaddingFromUpperCornerY);
  }

  /**
   *
   * @param graphicsContext Handle to graphics context
   * @param xSlotPosition X coordinate for the slot whose center will be the line origin.
   * 0,0 is upper left corner
   * @param ySlotPosition Y coordinate for the slot whose center will be the line origin.
   * 0,0 is upper left corner
   * @param direction One of NORTH, SOUTH, EAST or WEST
   * @param color Color for the line
   */
  private void drawLine(GraphicsContext graphicsContext, int xSlotPosition, int ySlotPosition, int direction, Color color)
  {
    double linePaddingForCenterPoint = 50.0;
    double lineWidth = 20.0;
    double lineLength = 50.0;

    graphicsContext.setFill(color);

    if (direction == NORTH)
    {
      graphicsContext.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
                               ySlotPosition * SLOT_SIZE_WITH_BORDER + 10.0,
                               lineWidth,
                               lineLength);
    }
    else if (direction == EAST)
    {
      graphicsContext.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
                               ySlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
                               lineLength + 10.0,
                               lineWidth);
    }
    else if (direction == SOUTH)
    {
      graphicsContext.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
                               ySlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint + 10.0,
                               lineWidth,
                               lineLength);
    }
    else if (direction == WEST)
    {
      graphicsContext.fillRect(xSlotPosition * SLOT_SIZE_WITH_BORDER + 10.0,
                               ySlotPosition * SLOT_SIZE_WITH_BORDER + linePaddingForCenterPoint,
                               lineLength + 10.0,
                               lineWidth);
    }
  }

  /**
   * Returns the appropriate node color for the given node number.
   *
   * @param nodeNumber Number corresponding to parent solution node
   * @return Appropriate color for node number
   */
  private Color getColorForNodeNumber(int nodeNumber)
  {
    Color returnColor = Color.GRAY;

    switch(nodeNumber)
    {
      case 2 : returnColor = new Color(0.64, 0.77, 0.27, 1.0);//green
      break;
      case 3 : returnColor = new Color(0.2, 0.59, 0.85, 1.0);//blue
      break;
      case 4 : returnColor = new Color(1.0, 0.8, 0.0, 1.0);//yellow
      break;
      case 5 : returnColor = new Color(0.90, 0.29, 0.25, 1.0);//red
      break;
    }

    return returnColor;
  }

  /**
   * Draws the screen and buttons after a level is passed.
   *
   * @param graphicsContext Handle to graphics context
   */
  private void drawLevelPassedScreen(GraphicsContext graphicsContext)
  {
    graphicsContext.setFill(new Color(0.17, 0.19, 0.24, 0.5));
    graphicsContext.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

    graphicsContext.setFill(Color.WHITE);
    graphicsContext.setFont(new Font("Arial", 50.0));
    graphicsContext.fillText("Great Job!", (SCREEN_WIDTH/2.0) - 120, (SCREEN_HEIGHT/2.0) - 15);

    // next level button
    graphicsContext.setFill(new Color(0.5, 0.5, 0.5, 0.5));
    graphicsContext.fillRect(NEXT_LEVEL_BUTTON_X, NEXT_LEVEL_BUTTON_Y, NEXT_LEVEL_BUTTON_WIDTH, NEXT_LEVEL_BUTTON_HEIGHT);
    graphicsContext.setFill(Color.WHITE);
    graphicsContext.setFont(new Font("Arial", 28.0));
    graphicsContext.fillText("Next Level", (SCREEN_WIDTH/2.0) - 130, (SCREEN_HEIGHT/2.0) + 40);

    // repeat button
    graphicsContext.setFill(new Color(0.5, 0.5, 0.5, 0.5));
    graphicsContext.fillRect(REPEAT_LEVEL_BUTTON_X, REPEAT_LEVEL_BUTTON_Y, REPEAT_LEVEL_BUTTON_WIDTH, REPEAT_LEVEL_BUTTON_HEIGHT);
    graphicsContext.setFill(Color.WHITE);
    graphicsContext.setFont(new Font("Arial", 28.0));
    graphicsContext.fillText("Repeat", (SCREEN_WIDTH/2.0) + 40, (SCREEN_HEIGHT/2.0) + 40);
  }

  /**
   * This method is called on mouse press. It initializes global variables and
   * solution stack.
   */
  private void enterSolveMode()
  {
    Node node = getNodeFromCurrentMousePosition();

    if (node != null && node.isStartNode())
    {
      currentNode = node;
      previousNode = node;
      currentSolutionStartNode = node;
      currentSolutionEndNode = null;
      numberOfSlotsLeft = node.getNumber();

      // check if node is already solved
      if (node.isSolved())
      {
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
   * This method is called on mouse drag. It computes what actions need to be
   * taken in order to add to solution stack and appropriately draw the
   * overlay.
   */
  private void searchNextNode()
  {
    Node node = getNodeFromCurrentMousePosition();

    System.out.println("node=" + node);
    System.out.println("currentNode=" + currentNode);
    if (node != null)
      System.out.println("node.IsUncrossable()=" + node.isUncrossable());
    if (node == null || previousNode == null || node == currentNode ||
        node.isUncrossable() || currentSolutionStartNode == null)
    {
      System.out.println("Node returned 0");
      return;
    }
    System.out.println("");

    // protect from diagonal movement
    if ((node.getXSlotPosition() > previousNode.getXSlotPosition() ||
         node.getXSlotPosition() < previousNode.getXSlotPosition()) &&
        (node.getYSlotPosition() > previousNode.getYSlotPosition() ||
         node.getYSlotPosition() < previousNode.getYSlotPosition()))
    {
      System.out.println("Node returned 1");
      return;
    }

    // protect from skipping
    if (Math.abs(node.getXSlotPosition() - previousNode.getXSlotPosition()) >= 2 ||
        Math.abs(node.getYSlotPosition() - previousNode.getYSlotPosition()) >= 2)
    {
      System.out.println("Node returned 2");
      return;
    }

    // if node is already visited, you need to undo the stack down to this node
    System.out.println("node.isVisited()=" + node.isVisited());
    if (node.isVisited())
    {
      currentNode = node;
      undoStackToNode(node, true);
      previousNode = node;
      currentSolutionEndNode = null;//reset end node
      return;
    }

    // check if we have number of slots left
    if (numberOfSlotsLeft <= 0)
    {
      System.out.println("Node returned 4");
      return;
    }

    // check if node is ending and we don't have the right number of slots left
    // or node is already solved
    if ((node.isEndNode() && numberOfSlotsLeft != 1) || node.isSolved())
    {
      System.out.println("Node returned 5");
      return;
    }

    node.setVisited(true);
    currentNode = node;
    node.setColor(currentSolutionStartNode.getColor());
    addNodeToSolutionStacks(node);

    // check if node is ending node, if so set it as solved
    if (node.isEndNode())
    {
      currentSolutionEndNode = node;
      currentSolutionEndNode.setSolved(true);
      currentSolutionStartNode.setSolved(true);
    }

    // draw line from previous node to this node
    // determine if this node is north, south, east or west of previous node
    if (currentNode.getYSlotPosition() < previousNode.getYSlotPosition())
    {
      currentNode.setSouthLine(true);
      previousNode.setNorthLine(true);
    }
    else if (currentNode.getYSlotPosition() > previousNode.getYSlotPosition())
    {
      currentNode.setNorthLine(true);
      previousNode.setSouthLine(true);
    }
    else if (currentNode.getXSlotPosition() < previousNode.getXSlotPosition())
    {
      currentNode.setEastLine(true);
      previousNode.setWestLine(true);
    }
    else if (currentNode.getXSlotPosition() > previousNode.getXSlotPosition())
    {
      currentNode.setWestLine(true);
      previousNode.setEastLine(true);
    }

    // advance previous node
    previousNode = currentNode;
  }

  /**
   * This method is called when the mouse is released in order to determine if
   * we need to undo any solution/changes.
   */
  private void exitSolveMode()
  {
    if (currentSolutionStartNode != null && currentSolutionEndNode == null)
    {
      undoStackToNode(currentSolutionStartNode, false);
    }
    else if (currentSolutionStartNode != null && currentSolutionEndNode != null)
    {
      if (numberOfSolutionsLeft > 0)
      {
        System.out.println("Decrease number of solutions left");
        numberOfSolutionsLeft--;
      }
      markCurrentSolutionUncrossable();
      System.out.println("Number of solutions left: " + numberOfSolutionsLeft);
    }

    currentSolutionStartNode = null;
    currentSolutionEndNode = null;

    if (numberOfSolutionsLeft == 0)
    {
      System.out.println("CONGRATULATIONS, YOU PASSED TO THE NEXT LEVEL!");
      levelSolved = true;
    }
  }

  /**
   * Returns the handle to the node the mouse has currently picked one, null
   * otherwise. Value can be null if mouse is within slot borders for example.
   *
   * @return Handle to node if mouse has picked one, null otherwise
   */
  private Node getNodeFromCurrentMousePosition()
  {
    // check if mouse is within slot
    int offsetMouseX = (int)currentMouseX % SLOT_SIZE_WITH_BORDER;

    // offset menu on Y
    int offsetMouseY = (int)(currentMouseY-30) % SLOT_SIZE_WITH_BORDER;

    //System.out.println("currentMouseY=" + currentMouseY);
    //System.out.println("offsetMouseY=" + offsetMouseY);

    if (offsetMouseX > 20 && offsetMouseX < 100 && offsetMouseY > 20 && offsetMouseY < 100)
    {
      int x = (int)(currentMouseX / SLOT_SIZE_WITH_BORDER);
      int y = (int)((currentMouseY-30) / SLOT_SIZE_WITH_BORDER);
      //System.out.println("Returned node " + x + "," + y);
      return nodes[x][y];
    }

    //System.out.println("Returned null");
    return null;
  }

  /**
   * Adds the given node to the solution/change stack in order to be able to
   * trace back our steps when we need to undo them, e.g., going back a node or
   * releasing the mouse without having solved a node pair
   *
   * @param node Handle to node to be added to the stack
   */
  private void addNodeToSolutionStacks(Node node)
  {
    //System.out.println("currentSolutionParentNode=" + currentSolutionStartingNode);

    for (int i = 0; i < solutionStacks.size(); i++)
    {
      Stack<Node> solutionStack = solutionStacks.get(i);
      //System.out.println("Trying to add node, solution stack size=" + solutionStack.size());
      //System.out.println("solutionStack.get(0)=" + solutionStack.get(0));
      // look at first node of solution
      if (currentSolutionStartNode == solutionStack.get(0))
      {
        // node found, just add to stack
        //System.out.println("Found solution node");
        solutionStack.push(node);
        numberOfSlotsLeft--;
        return;
      }
    }

    //stack not found, create new one and add to solution stacks
    Stack<Node> solutionStack = new Stack<Node>();
    solutionStack.push(node);
    numberOfSlotsLeft--;
    solutionStacks.add(solutionStack);
  }

  /**
   * Reverts changes saved in the node stack down to the given node.
   *
   * @param node Which node to undo the stack down to
   * @param softReset True if a soft reset should be performed on the parent
   * node. A soft reset is done when dragging back down to the parent node
   */
  private void undoStackToNode(Node node, boolean softReset)
  {
    // find current solution node on the stack and undo solution stack
    for (int i = 0; i < solutionStacks.size(); i++)
    {
      Stack<Node> solutionStack = solutionStacks.get(i);
      if (solutionStack != null && currentSolutionStartNode == solutionStack.get(0))
      {
        Node previousNodeInStack = null;
        // undo stack
        while (!solutionStack.isEmpty())
        {
          Node currentNodeInStack = solutionStack.peek();
          if (currentNodeInStack == node)
          {
            break;
          }

          if (currentNodeInStack.isEndNode())
          {
            currentNodeInStack.resetEndNode();
          }
          else
          {
            currentNodeInStack.resetLine();
          }

          previousNodeInStack = currentNodeInStack;
          solutionStack.pop();
          numberOfSlotsLeft++;
          //System.out.println("Popped node, solution stack size=" + solutionStack.size());
        }

        //remove solution from the list if given node was parent node
        if (node.isStartNode())
        {
          if (softReset)
          {
            node.softReset();
          }
          else
          {
            node.resetStartNode();
            solutionStacks.remove(i);
          }
        }
        else
        {
          // erase lines coming into this node from previous node in stack
          if (previousNodeInStack.getXSlotPosition() > node.getXSlotPosition())
            currentNode.setEastLine(false);
          else if (previousNodeInStack.getXSlotPosition() < node.getXSlotPosition())
            currentNode.setWestLine(false);
          else if (previousNodeInStack.getYSlotPosition() > node.getYSlotPosition())
            currentNode.setSouthLine(false);
          else if (previousNodeInStack.getYSlotPosition() < node.getYSlotPosition())
            currentNode.setNorthLine(false);
        }

        break;
      }
    }
  }

  /**
   * Makes all nodes in the current solution uncrossable
   */
  private void markCurrentSolutionUncrossable()
  {
    // find current solution node on the stack and mark all nodes uncrossable
    for (int i = 0; i < solutionStacks.size(); i++)
    {
      Stack<Node> solutionStack = solutionStacks.get(i);
      if (solutionStack != null && currentSolutionStartNode == solutionStack.get(0))
      {
        for (int j = 0; j < solutionStack.size(); j++)
        {
          solutionStack.get(j).setUncrossable(true);
        }
      }
    }
  }

  /**
   * Checks if the next level or repeat level buttons have been pushed.
   */
  private void checkButtonPushed()
  {
    // if next level button pushed
    if (currentMouseX >= NEXT_LEVEL_BUTTON_X &&
        currentMouseX <= (NEXT_LEVEL_BUTTON_X + NEXT_LEVEL_BUTTON_WIDTH) &&
        (currentMouseY-30) >= NEXT_LEVEL_BUTTON_Y &&
        (currentMouseY-30) <= (NEXT_LEVEL_BUTTON_Y + NEXT_LEVEL_BUTTON_HEIGHT))
    {
      System.out.println("Next level button pushed");
      currentLevel++;
      try
      {
        loadLevel("level" + currentLevel + ".txt");
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    // if repeat button pushed
    else if (currentMouseX >= REPEAT_LEVEL_BUTTON_X &&
             currentMouseX <= (REPEAT_LEVEL_BUTTON_X + REPEAT_LEVEL_BUTTON_WIDTH) &&
             (currentMouseY-30) >= REPEAT_LEVEL_BUTTON_Y &&
             (currentMouseY-30) <= (REPEAT_LEVEL_BUTTON_Y + REPEAT_LEVEL_BUTTON_HEIGHT))
    {
      System.out.println("Repeat level button pushed");
      try
      {
        loadLevel("level" + currentLevel + ".txt");
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
    }
  }
}
