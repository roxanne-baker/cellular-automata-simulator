
public abstract class Border {

	public Border() {
	}
	
	public abstract void setGridAndBorders(Cell[][] grid, boolean addDiagonals);
	
	public abstract void addCardinalNeighbors(Cell[][] grid, int row, int col);
	
	public abstract void addDiagonalNeighbors(Cell[][] grid, int row, int col);
	
	
	
}
