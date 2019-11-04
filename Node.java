import javafx.scene.paint.Color;

public class Node {

		private int xSlotPosition;
		private int ySlotPosition;
		private String text;
		private int number;
		
		private Color color;
		private boolean startNode;
		private boolean endNode;
		
		
		private boolean northLine;
		private boolean southLine;
		private boolean eastLine;
		private boolean westLine;
		
		public void init() {
			this.xSlotPosition = 0;
			this.ySlotPosition = 0;
			this.startNode = false;
			this.endNode = false;
			this.text = " ";
			this.number = 0; 
			this.northLine = false;
			this.southLine = false;
			this.eastLine = false;
			this.westLine = false;
			
		}
		public int getNumber() {
			return number;
		}

		public void setNumber(int number) {
			this.number = number;
		}
		
		public int getxSlotPosition() {
			return xSlotPosition;
		}
		public void setxSlotPosition(int xSlotPosition) {
			this.xSlotPosition = xSlotPosition;
		}
		public int getySlotPosition() {
			return ySlotPosition;
		}
		public void setySlotPosition(int ySlotPosition) {
			this.ySlotPosition = ySlotPosition;
		}
		public String getText() {
			return text;
		}
		public void setText(String text) {
			this.text = text;
		}
		public Color getColor() {
			return color;
		}
		public void setColor(Color color) {
			this.color = color;
		}
		public boolean isStartNode() {
			return startNode;
		}
		public void setStartNode(boolean startNode) {
			this.startNode = startNode;
		}
		public boolean isEndNode() {
			return endNode;
		}
		public void setEndNode(boolean endNode) {
			this.endNode = endNode;
		} 
		public boolean isNorthLine() {
			return northLine;
		}
		public void setNorthLine(boolean northLine) {
			this.northLine = northLine;
		}
		public boolean isSouthLine() {
			return southLine;
		}
		public void setSouthLine(boolean southLine) {
			this.southLine = southLine;
		}
		public boolean isEastLine() {
			return eastLine;
		}
		public void setEastLine(boolean eastLine) {
			this.eastLine = eastLine;
		}
		public boolean isWestLine() {
			return westLine;
		}
		public void setWestLine(boolean westLine) {
			this.westLine = westLine;
		}
	
	
	

}
