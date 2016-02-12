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
	
	/**
	 * Resets the justUpdated value of each cell false after an update.
	 */
	public void resetJustUpdated() {
		for (int i=0;i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				myCells[i][j].justUpdated = false;
			}
		}
	}
	
	/**
	 * 
	 * @param myGrid the cells to which neighbors will be added.
	 * @param addNeighborType Function addneighbortype.
	 */
	public void addAllNeighbors(Cell[][] myGrid, BiConsumer<Cell[][], int[]> addNeighborType) {
		for(int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				addNeighborType.accept(myGrid, new int[]{i, j});
			}
		}
	}
	
	/**
	 * The base method for adding cardinal neighbors, which are those neighbors directly to the sides of the cell.
	 * @param myGrid the cells on which diagonal neighbors will be added to the cell.
	 * @param position an integer array containing two values, the first being the row and the
	 *  second being the column of the cell.
	 */
	public abstract void addCardinalNeighbors(Cell[][] myGrid, int[] position);
	
	/**
	 * The base method for adding diagonal neighbors, which are those neighbors diagonal to the cell.
	 * @param myGrid the cells on which diagonal neighbors will be added to the cell.
	 * @param position an integer array containing two values, the first being the row and the
	 * second being the column of the cell.
	 */
	public abstract void addDiagonalNeighbors(Cell[][] myGrid, int[] position);
	
}