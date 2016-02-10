
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;

public class SegregationSimulation extends Simulation {

	/*
	 * @author Roxanne Baker
	 * Contains logic for representing the
	 * Segregation simulation
	 */
	private double threshold;
	public static final String RED = "red";
	public static final String BLUE = "blue";
	public static final String EMPTY = "empty";
	private int numberOfStates=3;
	private static final String STYLESHEET= "seggregation.css";
	
	public SegregationSimulation(Grid newGrid, double thresholdDecimal){
		super(newGrid);
		threshold = thresholdDecimal;
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addCardinalNeighbors(grid, position));
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addDiagonalNeighbors(grid, position));		
	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#setStateNameToColor()
	 */
	public void setStateNameToColor() {
		stateNameToColor = new HashMap<String, Color>();
		stateNameToColor.put(this.RED, Color.RED);
		stateNameToColor.put(this.BLUE, Color.BLUE);
		stateNameToColor.put(this.EMPTY, Color.WHITE);
	}
	
	/*
	 * Gets all empty cells, representing
	 * where a cell is able to move to
	 */
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
	
	/*
	 * @returns boolean representing
	 * whether or not a cell is dissatisfied
	 * and wants to "move"
	 */
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
	
	/*
	 * updates the state of all cells
	 */
	public void updateCellStates() {
		String[][] nextCellStates = getInitialStates();
		nextCellStates = getNextStates(nextCellStates);
		setNextStates(nextCellStates);		
	}
	
	/*
	 * returns a double array of Strings that
	 * represents the current states of the cells
	 * in the grid
	 */
	private String[][] getInitialStates() {
		String[][] newState=new String[myCells.length][myCells[0].length];
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				newState[i][j]=myCells[i][j].getState();
			}
		}
		return newState;
	}
	
	/*
	 * returns a double array that represents what
	 * the next states of the cells in the grid
	 * are going to be
	 */
	private String[][] getNextStates(String[][] nextCellStates) {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				nextCellStates = updateCell(new int[]{i, j}, nextCellStates);
			}
		}
		return nextCellStates;
	}
	
	/*
	 * takes a String double array and sets all states
	 * in the grid based on the contents of the array
	 */
	public void setNextStates(String[][] newState) {
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				myCells[i][j].setState(newState[i][j]);
			}
		}
	}
	
	/*
	 * updates the position of a single cell given its
	 * corresponding position in the grid and what
	 * the next states are
	 */
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
	public int getNumberOfStates(){
		return numberOfStates;
	}
	public HashMap<Color, Number> returnProportion(){
		int countRed=0;
		int countBlue=0;
		int countEmpty=0;
		int total=0;
		HashMap<Color, Number> proportions=new HashMap<Color, Number>();
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				if(myCells[i][j].getState().equals(RED)){
					countRed++;
				}
				else if(myCells[i][j].getState().equals(BLUE)){
					countBlue++;
				}
				else if(myCells[i][j].getState().equals(EMPTY)){
					countEmpty++;
				}
				total++;
			}
		}
		double prop1=(double)countRed/total;
		double prop2=(double)countBlue/total;
		double prop3=(double)countEmpty/total;
		proportions.put(Color.RED, prop1);
		proportions.put(Color.BLUE, prop2);
		proportions.put(Color.WHITE, prop3);
		return proportions;
	}
	public String returnStyleSheet(){
		return STYLESHEET;
	}
	
	/*
	 * updates both the states and color of all cells in the grid
	 */
	public void update() {
		updateCellStates();
		setCellColor(myCells);
		resetJustUpdated();
	}
}