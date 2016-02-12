import java.util.function.BiConsumer;

import javafx.scene.Group;

public abstract class Grid {

	public Cell[][] myCells;
	
	public Grid(Group root, int height, int width) {
		setGrid(height, width);
		showGrid(root, height, width);
	}
	
	public abstract void setGrid (int height, int width);
	
	public void showGrid(Group root, int height, int width) {
		for (int i=0;i<height; i++) {
			for (int j=0; j<width; j++) {
				root.getChildren().add(myCells[i][j].shape);
			}
		}
	}
	
//	public void resetJustUpdated() {
//		for (int i=0;i<myCells.length; i++) {
//			for (int j=0; j<myCells[0].length; j++) {
//				myCells[i][j].justUpdated = false;
//			}
//		}
//	}
	
	public void addAllNeighbors(Cell[][] myGrid, BiConsumer<Cell[][], int[]> addNeighborType) {
		for(int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				addNeighborType.accept(myGrid, new int[]{i, j});
			}
		}
	}
	
	
//	public void addCardinalNeighbors(Cell[][] myGrid, int[] position) {
//		int row = position[0];
//		int col = position[1];
//		boolean isFirstRow = (row == 0);
//		boolean isLastRow = (row == myGrid.length-1);
//		boolean isFirstCol = (col == 0);
//		boolean isLastCol = (col == myGrid[0].length-1);
//		if (!isFirstRow) {
//			myGrid[row][col].getMyNeighbours().add(myGrid[row-1][col]);
//		}
//		if (!isFirstCol) {
//			myGrid[row][col].getMyNeighbours().add(myGrid[row][col-1]);
//		}
//		if (!isLastCol) {
//			myGrid[row][col].getMyNeighbours().add(myGrid[row][col+1]);
//		}
//		if (!isLastRow) {
//			myGrid[row][col].getMyNeighbours().add(myGrid[row+1][col]);
//		}
//	}
//	
//	public abstract void addDiagonalNeighbors(Cell[][] myGrid, int[] position);
	
}