
public class NormalBorder extends Border{

	public NormalBorder() {
	}
	
	Cell[][] myGrid;
	
	boolean isFirstRow;
	boolean isLastRow;
	boolean isFirstCol;
	boolean isLastCol;
	boolean isHexagonal = false;
	
	
	public void setGridAndBorders(Cell[][] grid, boolean addDiagonals) {
		myGrid = grid;
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid[0].length; j++) {
				isFirstRow = (i == 0);
				isLastRow = (i == myGrid.length-1);
				isFirstCol = (j == 0);
				isLastCol = (j == myGrid[0].length-1);
				addCardinalNeighbors(grid, i, j);
				if (addDiagonals) {
					addDiagonalNeighbors(grid, i, j);
				}
			}
		}
	}
	
	public Cell getNorthNeighbor(Cell[][] grid, int row, int col) {
		if (!isFirstRow) {
			return grid[row-1][col];
		}
		return null;
	}
	
	public Cell getSouthNeighbor(Cell[][] grid, int row, int col) {
		if (!isLastRow) {
			return grid[row+1][col];
		}
		return null;
	}
	
	public Cell getLeftNeighbor(Cell[][] grid, int row, int col) {
		if (!isFirstCol) {
			return grid[row][col-1];
		}
		return null;
	}
	
	public Cell getRightNeighbor(Cell[][] grid, int row, int col) {
		if (!isLastCol) {
			return grid[row][col+1];
		}
		return null;
	}
	
	public Cell getUpperLeftNeighbor(Cell[][] grid, int row, int col) {
		if (!isFirstRow && !isFirstCol) {
			return grid[row-1][col-1];
		}
		return null;
	}
	
	public Cell getUpperRightNeighbor(Cell[][] grid, int row, int col) {
		if (!isFirstRow && !isLastCol) {
			return grid[row-1][col+1];
		}
		return null;
	}
	
	public Cell getLowerLeftNeighbor(Cell[][] grid, int row, int col) {
		if (!isLastRow && !isFirstCol) {
			return grid[row+1][col-1];
		}
		return null;
	}
	
	public Cell getLowerRightNeighbor(Cell[][] grid, int row, int col) {
		if (!isLastRow && !isLastCol) {
			return grid[row+1][col+1];
		}
		return null;
	}
	
	public void addCardinalNeighbors(Cell[][] myGrid, int row, int col) {
		myGrid[row][col].getMyNeighbours().add(getNorthNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getSouthNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getLeftNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getRightNeighbor(myGrid, row, col));
	}
	
	public void addDiagonalNeighbors(Cell[][] myGrid, int row, int col) {
		myGrid[row][col].getMyNeighbours().add(getUpperLeftNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getUpperRightNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getLowerLeftNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getLowerRightNeighbor(myGrid, row, col));
	}
}
