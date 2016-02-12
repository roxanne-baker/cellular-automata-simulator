
public class ToroidalBorder extends Border{

	public ToroidalBorder() {
	}
	
	Cell[][] myGrid;
	int row;
	int col;
	boolean isHexagonal = false;
	
	
	public void setGridAndBorders(Cell[][] grid, boolean addDiagonals) {
		myGrid = grid;
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid[0].length; j++) {
				addCardinalNeighbors(grid, i, j);
				if (addDiagonals) {
					addDiagonalNeighbors(grid, i, j);
				}
			}
		}
	}
	
	public int getNeighbourVal(int rowOrColVal, int edgeIndex, int borderVal, int normalVal) {
		if (rowOrColVal != edgeIndex) {
		    return normalVal;
		}
		else {
		    return borderVal;
		}
	}
	
	public void addLeftNeighbour(int row, int col) {
		int nextCol = getNeighbourVal(col, 0, myGrid[0].length-1, col-1);
		myGrid[row][col].getMyNeighbours().add(myGrid[row][nextCol]);
	}
	
	public void addRightNeighbour(int row, int col) {
		int nextCol = getNeighbourVal(col, myGrid[0].length-1, 0, col+1);
		myGrid[row][col].getMyNeighbours().add(myGrid[row][nextCol]);
	}	
	
	public void addTopNeighbour(int row, int col) {
		int nextRow = getNeighbourVal(row, 0, myGrid.length-1, row-1);
		myGrid[row][col].getMyNeighbours().add(myGrid[nextRow][col]);
	}
	
	public void addBotNeighbour(int row, int col) {
		int nextRow = getNeighbourVal(row, myGrid.length-1, 0, row+1);
		myGrid[row][col].getMyNeighbours().add(myGrid[nextRow][col]);
	}
	
	private void addLowerLeftNeighbour(int row, int col) {
		int nextCol = getNeighbourVal(col, 0, myGrid[0].length-1, col-1);	
		int nextRow = getNeighbourVal(row, myGrid.length-1, 0, row+1);
		myGrid[row][col].getMyNeighbours().add(myGrid[nextRow][nextCol]);		
	}
	
	private void addUpperLeftNeighbour(int row, int col) {
		int nextCol = getNeighbourVal(col, 0, myGrid[0].length-1, col-1);	
		int nextRow = getNeighbourVal(row, 0, myGrid.length, row-1);
		myGrid[row][col].getMyNeighbours().add(myGrid[nextRow][nextCol]);			
	}
	
	private void addLowerRightNeighbour(int row, int col) {
		int nextCol = getNeighbourVal(col, myGrid[0].length-1, 0, col+1);
		int nextRow = getNeighbourVal(row, myGrid.length-1, 0, row+1);
		myGrid[row][col].getMyNeighbours().add(myGrid[nextRow][nextCol]);		
	}
	
	private void addUpperRightNeighbour(int row, int col) {
		int nextCol = getNeighbourVal(col, myGrid[0].length-1, 0, col+1);
		int nextRow = getNeighbourVal(row, 0, myGrid.length, row-1);
		myGrid[row][col].getMyNeighbours().add(myGrid[nextRow][nextCol]);			
	}
	
	public void addCardinalNeighbors(Cell[][] myGrid, int row, int col) {
		addLeftNeighbour(row, col);
		addRightNeighbour(row, col);
		addTopNeighbour(row, col);
		addBotNeighbour(row, col);
	}
	
	public void addDiagonalNeighbors(Cell[][] myGrid, int row, int col) {
		addLowerLeftNeighbour(row, col);
		addLowerRightNeighbour(row, col);
		addUpperLeftNeighbour(row, col);
		addUpperRightNeighbour(row, col);
	}
}
