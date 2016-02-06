

public abstract class Simulation {
	
	Cell[][] myCells;
	
	public Simulation(Grid newGrid) {
		myCells = newGrid.myCells;
		setCellColor();
	}
	
	public Simulation(){
	}
	
	public abstract void update();
	
	public abstract void updateCellStates();
	
	public abstract void setCellColor(Cell cell);
	
	public void setCellColor() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				setCellColor(myCells[i][j]);
			}
		}
	}
	
	public void resetJustUpdated() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				myCells[i][j].justUpdated = false;
			}
		}
	}
	
}