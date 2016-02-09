import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class Simulation {
	
	/*
	 * @author Roxanne Baker
	 * Superclass for all Simulations
	 * Used to update the color of cells based on states
	 * and to update the states of cells
	 */
	
	Cell[][] myCells;
	// maps name of state to the color it fills a cell with
	Map<String, Color> stateNameToColor;
	protected int numberOfStates;
	
	public Simulation(Grid newGrid) {
		this();
		myCells = newGrid.myCells;
		setCellColor(myCells);
	}
	
	public Simulation(){
		setStateNameToColor();
	}
	
	public void setStateNameToColor() {
		stateNameToColor = new HashMap<String, Color>();
	}
	
	/*
	 * update method that takes care of
	 * updating both cell states and color
	 */
	public void update(){};
	
	
	/*
	 * updates the state of all cells
	 */
	public void updateCellStates(){};
	
	
	/*
	 * sets the color of a single cell
	 * based on its state
	 */
	public void setCellColor(Cell cell) {
		cell.shape.setFill(getColorFromStateName(cell.getState()));
	}
	
	/*
	 * sets the color of all cells in a grid
	 */
	public void setCellColor(Cell[][] myGrid) {
		for (int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				myGrid[i][j].shape.setFill(getColorFromStateName(myGrid[i][j].getState()));
			}
		}
	}
	
	/*
	 * Given the name of a state as a String, returns
	 * the color that it corresponds to in stateNameToColor
	 */
	public Color getColorFromStateName(String state) {
		return stateNameToColor.get(state);
	}
	
	/*
	 * resets the value "justUpdated" for all cells
	 */
	public void resetJustUpdated() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				myCells[i][j].justUpdated = false;
			}
		}
	}
	public int getNumberOfStates(){
		return numberOfStates;
	}
	public HashMap<Color, Number> returnProportion(){
		int countLive=0;
		int countDead=0;
		HashMap<Color, Number> proportions=new HashMap<Color, Number>();
		return proportions;
	}
	
}