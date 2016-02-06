import java.util.function.BiConsumer;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class SquareGrid extends Grid {

	private static final int gridSideLength = 350;
	private static final int gridWidthBuffer = 25;
	private static final int gridHeightBuffer = 30;
	
	public SquareGrid(Group root, int width, int height) {
		super(root,width,height);
	}
	
	public void setGrid(int width, int height) {
		
		myCells = new Cell[width][height];
		int cellWidth = gridSideLength/width;
		int cellHeight = gridSideLength/height;
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				Shape cellShape = getSquare(i*cellWidth+gridWidthBuffer, j*cellHeight+gridHeightBuffer, cellWidth, cellHeight);
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


	public void addCardinalNeighbors(Cell cell, int[] position) {
		int row = position[0];
		int col = position[1];
		boolean isFirstRow = (row == 0);
		boolean isLastRow = (row == myCells.length-1);
		boolean isFirstCol = (col == 0);
		boolean isLastCol = (col == myCells[0].length-1);
		if (!isFirstRow) {
			cell.getMyNeighbours().add(myCells[row-1][col]);
		}
		if (!isFirstCol) {
			cell.getMyNeighbours().add(myCells[row][col-1]);
		}
		if (!isLastCol) {
			cell.getMyNeighbours().add(myCells[row][col+1]);
		}
		if (!isLastRow) {
			cell.getMyNeighbours().add(myCells[row+1][col]);
		}
	}
	
	public void addDiagonalNeighbors(Cell cell, int[] position) {
		int row = position[0];
		int col = position[1];
		boolean isFirstRow = (row == 0);
		boolean isLastRow = (row == myCells.length-1);
		boolean isFirstCol = (col == 0);
		boolean isLastCol = (col == myCells[0].length-1);
		if (!isFirstRow && !isFirstCol) {
			cell.getMyNeighbours().add(myCells[row-1][col-1]);
		}
		if (!isFirstRow && !isLastCol) {
			cell.getMyNeighbours().add(myCells[row-1][col+1]);
		}
		if (!isLastRow && !isFirstCol) {
			cell.getMyNeighbours().add(myCells[row+1][col-1]);
		if (!isLastRow && isLastCol) {
			cell.getMyNeighbours().add(myCells[row+1][col+1]);
		}
		}
	}
	
}