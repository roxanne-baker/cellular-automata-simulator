

public abstract class Simulation {
	
	Cell[][] myCells;
	
	public Simulation(Grid newGrid) {
		myCells = newGrid.myCells;
		setCellColor(myCells);
	}
	
	public Simulation(){
	}
	
	public abstract void update();
	
	public abstract void updateCellStates();
	
	public void setCellColor(Cell cell) {
		cell.shape.setFill(cell.getState());
	}
	
	public void setCellColor(Cell[][] myGrid) {
		for (int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				myGrid[i][j].shape.setFill(myGrid[i][j].getState());
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