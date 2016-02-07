package cellsociety_team11;
import java.util.function.BiConsumer;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class SquareGrid extends Grid {

	private static final int gridSideLength = 350;
	private static final int gridWidthBuffer = 30;
	private static final int gridHeightBuffer = 25;
	
	public SquareGrid(Group root, int height, int width) {
		super(root,height,width);
	}
	
	public void setGrid(int height, int width) {
		
		myCells = new Cell[height][width];
		int cellWidth = gridSideLength/width;
		int cellHeight = gridSideLength/height;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
<<<<<<< HEAD
				Shape cellShape = getSquare(i*cellHeight+gridHeightBuffer, j*cellWidth+gridWidthBuffer, cellHeight, cellWidth);
=======
				Shape cellShape = getSquare(i*cellHeight+gridWidthBuffer, j*cellWidth+gridHeightBuffer, cellHeight, cellWidth);
>>>>>>> 2f30c614277a1396ba901a1e2a2e431f8595b72b
				myCells[i][j] = new Cell(cellShape);
			}
		}
	}
	
	public Rectangle getSquare(int leftX, int topY, int width, int height) {
		Rectangle r = new Rectangle();
		r.setX(leftX);
		r.setY(topY);
		r.setWidth(width);
		r.setHeight(height);
		
		r.setFill(Color.WHITE);
		r.setStroke(Color.BLACK);
		r.setStrokeType(StrokeType.INSIDE);
		r.setStrokeWidth(1);
		
		return r;
	}	


	public void addCardinalNeighbors(Cell[][] myGrid, int[] position) {
		int row = position[0];
		int col = position[1];
		boolean isFirstRow = (row == 0);
		boolean isLastRow = (row == myGrid.length-1);
		boolean isFirstCol = (col == 0);
		boolean isLastCol = (col == myGrid[0].length-1);
		if (!isFirstRow) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row-1][col]);
		}
		if (!isFirstCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row][col-1]);
		}
		if (!isLastCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row][col+1]);
		}
		if (!isLastRow) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row+1][col]);
		}
	}
	
	public void addDiagonalNeighbors(Cell[][] myGrid, int[] position) {
		int row = position[0];
		int col = position[1];
		boolean isFirstRow = (row == 0);
		boolean isLastRow = (row == myGrid.length-1);
		boolean isFirstCol = (col == 0);
		boolean isLastCol = (col == myGrid[0].length-1);
		if (!isFirstRow && !isFirstCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row-1][col-1]);
		}
		if (!isFirstRow && !isLastCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row-1][col+1]);
		}
		if (!isLastRow && !isFirstCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row+1][col-1]);
		if (!isLastRow && !isLastCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row+1][col+1]);
		}
		}
	}
	
}