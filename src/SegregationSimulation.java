
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
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
	
	public SegregationSimulation(Grid newGrid, double thresholdDecimal, Group root, Timeline animation, Border border, String name){
		super(newGrid, name);
		threshold = thresholdDecimal;
		border.setGridAndBorders(myCells, true);
		/**
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addCardinalNeighbors(grid, position));
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addDiagonalNeighbors(grid, position));	
		**/
		myTime=animation;
		addListeners(myCells, root);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#setStateNameToColor()
	 */
	public void setStateNameToColor() {
		stateNames = new ArrayList<String>(Arrays.asList(RED, BLUE, EMPTY));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.RED, Color.BLUE, Color.WHITE));
		setStateNameToColor(stateNames, colorNames);
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
	public void setValue(double newThreshold){
		threshold=newThreshold;
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

	public String returnStyleSheet(){
		return STYLESHEET;
	}
	public void addListeners(Cell[][]myCells, Group root){
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				Cell newCell;
				newCell=myCells[i][j];
				myCells[i][j].returnNode().addEventHandler(MouseEvent.MOUSE_CLICKED, e->changeState(root,newCell));
			}
		}
	}

	public double[] returnParameters(){
		double[]param =new double[3];
		param[0]=myCells.length;
		param[1]=myCells[0].length;
		param[2]=threshold;
		return param;
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