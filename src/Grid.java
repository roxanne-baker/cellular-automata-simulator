import java.util.function.BiConsumer;

import javafx.scene.Group;

public abstract class Grid {

	public Cell[][] myCells;
	
	public Grid(Group root, int width, int height) {
		setGrid(width, height);
		showGrid(root, width, height);
	}
	
	public abstract void setGrid (int width, int height);
	
	public void showGrid(Group root, int width, int height) {
		for (int i=0;i<width; i++) {
			for (int j=0; j<height; j++) {
				root.getChildren().add(myCells[i][j].shape);
			}
		}
	}
	
	public void resetJustUpdated() {
		for (int i=0;i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				myCells[i][j].justUpdated = false;
			}
		}
	}
	
	public void addAllNeighbors(Cell[][] myGrid, BiConsumer<Cell[][], int[]> addNeighborType) {
		for(int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				addNeighborType.accept(myGrid, new int[]{i, j});
			}
		}
	}
	
	public abstract void addCardinalNeighbors(Cell[][] myGrid, int[] position);
	
	public abstract void addDiagonalNeighbors(Cell[][] myGrid, int[] position);
	
}