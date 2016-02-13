
public abstract class Border {

	public Border() {
	}
	
	public abstract void setGridAndBorders(Cell[][] grid, boolean addDiagonals);
	
	public abstract void addCardinalNeighbors(Cell[][] grid, int row, int col);
	
	public abstract void addDiagonalNeighbors(Cell[][] grid, int row, int col);
	
	
	public abstract Cell getNorthNeighbor(Cell[][] grid, int row, int col);
	
	public abstract Cell getSouthNeighbor(Cell[][] grid, int row, int col);
	
	public abstract Cell getLeftNeighbor(Cell[][] grid, int row, int col);
	
	public abstract Cell getRightNeighbor(Cell[][] grid, int row, int col);
	
	public abstract Cell getUpperLeftNeighbor(Cell[][] grid, int row, int col);
	
	public abstract Cell getUpperRightNeighbor(Cell[][] grid, int row, int col);
	
	public abstract Cell getLowerLeftNeighbor(Cell[][] grid, int row, int col);
	
	public abstract Cell getLowerRightNeighbor(Cell[][] grid, int row, int col);
	
	
}
