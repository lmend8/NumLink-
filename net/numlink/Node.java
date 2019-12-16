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

import javafx.scene.paint.Color;

/**
 * This class encapsulates the functionality behind a node which in turn
 * represents a slot in the game board.
 *
 * @author Luis Mendoza
 */
public class Node
{
  private int xSlotPosition;
  private int ySlotPosition;
  private boolean startNode;
  private boolean endNode;
  private int number;
  private String text;
  private Color color;
  private boolean northLine;
  private boolean southLine;
  private boolean eastLine;
  private boolean westLine;
  private boolean uncrossable;
  private boolean visited;
  private boolean solved;

  /**
   * Class constructor. Calls initialize method to initialize attributes.
   */
  public Node()
  {
    initialize();
  }

  /**
   * Initializes attributes.
   */
  public void initialize()
  {
    startNode = false;
    number = 0;
    endNode = false;
    northLine = false;
    southLine = false;
    eastLine = false;
    westLine = false;
    uncrossable = false;
    solved = false;
    visited = false;
  }

  /**
   * Resets attributes for a start node.
   */
  public void resetStartNode()
  {
    northLine = false;
    southLine = false;
    eastLine = false;
    westLine = false;
    color = Color.GRAY;
    visited = false;
    solved = false;
    uncrossable = true;
  }

  /**
   * Performs a soft reset of the start node y just erasing all lines. This is
   * useful when mouse dragging back to initial node.
   */
  public void softReset()
  {
    northLine = false;
    southLine = false;
    eastLine = false;
    westLine = false;
  }

  /**
   * Resets attributes for a end node.
   */
  public void resetEndNode()
  {
    northLine = false;
    southLine = false;
    eastLine = false;
    westLine = false;
    color = Color.GRAY;
    visited = false;
    solved = false;
    uncrossable = false;
  }

  /**
   * Resets attributes for line node.
   */
  public void resetLine()
  {
    northLine = false;
    southLine = false;
    eastLine = false;
    westLine = false;
    uncrossable = false;
    visited = false;
  }

  // generated getters and setters below
  public int getXSlotPosition()
  {
    return xSlotPosition;
  }

  public void setXSlotPosition(int xSlotPosition)
  {
    this.xSlotPosition = xSlotPosition;
  }

  public int getYSlotPosition()
  {
    return ySlotPosition;
  }

  public void setYSlotPosition(int ySlotPosition)
  {
    this.ySlotPosition = ySlotPosition;
  }

  public boolean isStartNode()
  {
    return startNode;
  }

  public void setStartNode(boolean startNode)
  {
    this.startNode = startNode;
  }

  public boolean isEndNode()
  {
    return endNode;
  }

  public void setEndingNode(boolean endNode)
  {
    this.endNode = endNode;
  }

  public int getNumber()
  {
    return number;
  }

  public void setNumber(int number)
  {
    this.number = number;
  }

  public String getText()
  {
    return text;
  }

  public void setText(String text)
  {
    this.text = text;
  }

  public Color getColor()
  {
    return color;
  }

  public void setColor(Color color)
  {
    this.color = color;
  }

  public boolean isNorthLine()
  {
    return northLine;
  }

  public void setNorthLine(boolean northLine)
  {
    this.northLine = northLine;
  }

  public boolean isSouthLine()
  {
    return southLine;
  }

  public void setSouthLine(boolean southLine)
  {
    this.southLine = southLine;
  }

  public boolean isEastLine()
  {
    return eastLine;
  }

  public void setEastLine(boolean eastLine)
  {
    this.eastLine = eastLine;
  }

  public boolean isWestLine()
  {
    return westLine;
  }

  public void setWestLine(boolean westLine)
  {
    this.westLine = westLine;
  }

  public boolean isUncrossable()
  {
    return uncrossable;
  }

  public void setUncrossable(boolean uncrossable)
  {
    this.uncrossable = uncrossable;
  }

  public boolean isVisited()
  {
    return visited;
  }

  public void setVisited(boolean visited)
  {
    this.visited = visited;
  }

  public boolean isSolved()
  {
    return solved;
  }

  public void setSolved(boolean solved)
  {
    this.solved = solved;
  }
}
