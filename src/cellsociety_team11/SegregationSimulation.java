package cellsociety_team11;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;

public class SegregationSimulation extends Simulation {

	private double threshold;
	public static final String RED = "red";
	public static final String BLUE = "blue";
	public static final String EMPTY = "empty";
	
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
	
	public List<int[]> getEmptyCells(String[][] nextCells) {
		List<int[]> emptyCells = new ArrayList<int[]>();
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (nextCells[i][j].equals(EMPTY) && !myCells[i][j].justUpdated) {
					emptyCells.add(new int[]{i, j});
				}
			}
		}
		return emptyCells;
	}
	
	public boolean moveCell(Cell cell) {
		if (cell.getState().equals(EMPTY)) return false;
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
		String[][] nextCellStates = getInitialStates();
		nextCellStates = getNextStates(nextCellStates);
		setNextStates(nextCellStates);		
	}
	
	private String[][] getInitialStates() {
		String[][] newState=new String[myCells.length][myCells[0].length];
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				newState[i][j]=myCells[i][j].getState();
			}
		}
		return newState;
	}
	
	private String[][] getNextStates(String[][] nextCellStates) {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				nextCellStates = updateCell(new int[]{i, j}, nextCellStates);
			}
		}
		return nextCellStates;
	}
	
	public void setNextStates(String[][] newState) {
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				myCells[i][j].setState(newState[i][j]);
			}
		}
	}
	
	public String[][] updateCell(int[] position, String[][] nextCells) {
		int row = position[0];
		int col = position[1];
		List<int[]> emptyCells = getEmptyCells(nextCells);
		if (moveCell(myCells[row][col]) && !emptyCells.isEmpty() && !myCells[row][col].justUpdated) {
			int emptyRow = emptyCells.get(0)[0];
			int emptyCol = emptyCells.get(0)[1];
			nextCells[emptyRow][emptyCol] = myCells[row][col].getState();
			nextCells[row][col] = EMPTY;
			
			myCells[row][col].justUpdated = true;
			myCells[emptyRow][emptyCol].justUpdated = true;
		}
		return nextCells;
	}
	
	public void update() {
		updateCellStates();
		setCellColor(myCells);
		resetJustUpdated();
	}
}