import javafx.scene.shape.Polygon;

public class ToroidalBorder extends Border{

	public ToroidalBorder() {
	}
	
	private Cell[][] myGrid;
	private boolean isHexagonal = false;
	
	
	public void setGridAndBorders(Cell[][] grid, boolean addDiagonals) {
		myGrid = grid;
		setIsHexagonal();
		for (int i=0; i<grid.length; i++) {
			for (int j=0; j<grid[0].length; j++) {
				addCardinalNeighbors(grid, i, j);
				if (addDiagonals) {
					addDiagonalNeighbors(grid, i, j);
				}
			}
		}
		
	}
	
	private void setIsHexagonal() {
		if (myGrid[0][0].shape.getClass().equals(new Polygon().getClass())) {
			Polygon gridShape = (Polygon) myGrid[0][0].shape;
			if (gridShape.getPoints().size() == 6) {
				isHexagonal = true;
			}
		}
	}
	
	
	private int getNeighborVal(int rowOrColVal, int edgeIndex, int borderVal, int normalVal) {
		if (rowOrColVal != edgeIndex) {
		    return normalVal;
		}
		else {
		    return borderVal;
		}
	}
	
	public Cell getLeftNeighbor(Cell[][] grid, int row, int col) {
		int nextCol = getNeighborVal(col, 0, myGrid[0].length-1, col-1);
		return grid[row][nextCol];
	}
	
	public Cell getRightNeighbor(Cell[][] grid, int row, int col) {
		int nextCol = getNeighborVal(col, myGrid[0].length-1, 0, col+1);
		return grid[row][nextCol];
	}	
	
	public Cell getNorthNeighbor(Cell[][] grid, int row, int col) {
		int nextRow = getNeighborVal(row, 0, myGrid.length-1, row-1);
		return grid[nextRow][col];
	}
	
	public Cell getSouthNeighbor(Cell[][] grid, int row, int col) {
		int nextRow = getNeighborVal(row, myGrid.length-1, 0, row+1);
		return grid[nextRow][col];
	}
	
	public Cell getLowerLeftNeighbor(Cell[][] grid, int row, int col) {
		int nextCol = getNeighborVal(col, 0, myGrid[0].length-1, col-1);	
		int nextRow = getNeighborVal(row, myGrid.length-1, 0, row+1);
		return grid[nextRow][nextCol];	
	}
	
	public Cell getUpperLeftNeighbor(Cell[][] grid, int row, int col) {
		int nextCol = getNeighborVal(col, 0, myGrid[0].length-1, col-1);	
		int nextRow = getNeighborVal(row, 0, myGrid.length-1, row-1);
		return grid[nextRow][nextCol];				
	}
	
	public Cell getLowerRightNeighbor(Cell[][] grid, int row, int col) {
		int nextCol = getNeighborVal(col, myGrid[0].length-1, 0, col+1);
		int nextRow = getNeighborVal(row, myGrid.length-1, 0, row+1);
		return grid[nextRow][nextCol];		
	}
	
	public Cell getUpperRightNeighbor(Cell[][] grid, int row, int col) {
		int nextCol = getNeighborVal(col, myGrid[0].length-1, 0, col+1);
		int nextRow = getNeighborVal(row, 0, myGrid.length-1, row-1);
		return grid[nextRow][nextCol];				
	}
	
	public void addCardinalNeighbors(Cell[][] myGrid, int row, int col) {
		myGrid[row][col].getMyNeighbours().add(getNorthNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getSouthNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getLeftNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getRightNeighbor(myGrid, row, col));
	}
	
	public void addDiagonalNeighbors(Cell[][] myGrid, int row, int col) {
		myGrid[row][col].getMyNeighbours().add(getUpperLeftNeighbor(myGrid, row, col));
		myGrid[row][col].getMyNeighbours().add(getLowerLeftNeighbor(myGrid, row, col));
		if (!isHexagonal) {
			myGrid[row][col].getMyNeighbours().add(getUpperRightNeighbor(myGrid, row, col));
			myGrid[row][col].getMyNeighbours().add(getLowerRightNeighbor(myGrid, row, col));
		}
	}
}
