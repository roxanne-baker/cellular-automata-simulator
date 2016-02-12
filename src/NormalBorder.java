
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
	
	public void addCardinalNeighbors(Cell[][] myGrid, int row, int col) {
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
	
	public void addDiagonalNeighbors(Cell[][] myGrid, int row, int col) {
		if (!isFirstRow && !isFirstCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row-1][col-1]);
		}
		if (!isFirstRow && !isLastCol && !isHexagonal) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row-1][col+1]);
		}
		if (!isLastRow && !isFirstCol) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row+1][col-1]);
		if (!isLastRow && !isLastCol && !isHexagonal) {
			myGrid[row][col].getMyNeighbours().add(myGrid[row+1][col+1]);
		}
		}
	}
}
