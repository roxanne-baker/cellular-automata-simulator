import java.util.function.BiConsumer;

import javafx.scene.Group;

/**
 * 
 * @author Roxanne Baker
 * This class is the base class for all Grid objects and subclasses. 
 */
public abstract class Grid {

	public Cell[][] myCells;
	
	/**
	 * Sets up a Grid object using the setGrid and showGrid methods.
	 * @param root Group of objects to be displayed on the grid.
	 * @param height Height of the grid.
	 * @param width Width of the grid.
	 */
	public Grid(Group root, int height, int width) {
		setGrid(height, width);
		showGrid(root, height, width);
	}
	
	/**
	 * The base method for setGrid methods in subclasses.
	 * @param Height of the grid.
	 * @param Width of the grid.
	 */
	public abstract void setGrid (int height, int width);
	
	/**
	 * Fills the grid with the cells.
	 * @param root Group of objects to be displayed on the grid.
	 * @param height Height of the grid.
	 * @param width Width of the grid.
	 */
	public void showGrid(Group root, int height, int width) {
		for (int i=0;i<height; i++) {
			for (int j=0; j<width; j++) {
				root.getChildren().add(myCells[i][j].shape);
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
	

}