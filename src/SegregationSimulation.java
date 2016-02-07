
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;

public class SegregationSimulation extends Simulation {

	private double threshold;
	public static final String RED = "RED";
	public static final String BLUE = "BLUE";
	public static final String EMPTY = "WHITE";
	
	public SegregationSimulation(Grid newGrid, double thresholdDecimal){
		super(newGrid);
		threshold = thresholdDecimal;
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addCardinalNeighbors(grid, position));
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addDiagonalNeighbors(grid, position));		
	}
	
	public void setStateNameToColor() {
		stateNameToColor = new HashMap<String, Color>();
		stateNameToColor.put(this.RED, Color.RED);
		stateNameToColor.put(this.BLUE, Color.BLUE);
		stateNameToColor.put(this.EMPTY, Color.WHITE);
	}
	
	public List<Cell> getEmptyCells() {
		List<Cell> emptyCells = new ArrayList<Cell>();
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals(EMPTY)) {
					emptyCells.add(myCells[i][j]);
				}
			}
		}
		return emptyCells;
	}
	
	public boolean moveCell(Cell cell) {
		int numNeighbors = 0;
		int similarNeighbors = 0;
		for (Cell neighbor : cell.getMyNeighbours()) {
			if(!neighbor.getState().equals(EMPTY)) {
				numNeighbors++;
			}
			if(neighbor.getState().equals(cell.getState())) {
				similarNeighbors++;
			}
		}
		if (((double) similarNeighbors/numNeighbors) < threshold) {
			return true;
		}
		return false;
	}
	
	
	public void updateCellStates() {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				updateCell(myCells[i][j]);
			}
		}
	}
	
	public void updateCell(Cell cell) {
		List<Cell> emptyCells = getEmptyCells();
		if (moveCell(cell) && !emptyCells.isEmpty() && !cell.justUpdated) {
			emptyCells.get(0).setState(cell.getState());
			emptyCells.get(0).justUpdated = true;
			
			cell.setState(EMPTY);
			cell.justUpdated = true;
			emptyCells.remove(0);
			emptyCells.add(cell);
		}
	}
	
	public void update() {
		updateCellStates();
		setCellColor(myCells);
		resetJustUpdated();
	}
}