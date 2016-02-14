import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.paint.Color;

public abstract class Simulation {
	
	/*
	 * @author Roxanne Baker
	 * Superclass for all Simulations
	 * Used to update the color of cells based on states
	 * and to update the states of cells
	 */
	
	Cell[][] myCells;
	// maps name of state to the color it fills a cell with
	Map<String, Color> stateNameToColor;
	protected String name;
	protected int numberOfStates;
	protected Timeline myTime;
	protected List<String> stateNames;
	
	public Simulation(Grid newGrid, String myName) {
		this();
		myCells = newGrid.myCells;
		setCellColor(myCells);
		name=myName;
	}
	
	public Simulation(){
		setStateNameToColor();
	}
	
	public void setStateNameToColor(){
	}
	
	/**
	 * returns a double array of strings with the initial states of the cells
	 * @return newState
	*/
	protected String[][] getInitialStates() {
	    String[][] newState=new String[myCells.length][myCells[0].length];
	    for(int i=0; i<myCells.length; i++) {
	        for (int j=0; j<myCells[0].length; j++) {
	            newState[i][j]=myCells[i][j].getState();
	            }
	        }
	    return newState;
	    }
	
	/*
	 * update method that takes care of
	 * updating both cell states and color
	 */
	public void update(){};
	
	public void setValue(double newd){};
	
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
	public String returnName(){
		return name;
	}
	public double[] returnParameters(){
		return null;
	}
	public List<String> returnStates(){
		List<String> states=new ArrayList<String>();
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				states.add(myCells[i][j].getState());
			}
		}
		return states;
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
	public void setStateNameToColor(List<String> stateNames, List<Color> colorNames) {
		stateNameToColor = new HashMap<String, Color>();
		for (int i=0; i<stateNames.size(); i++) {
			stateNameToColor.put(stateNames.get(i), colorNames.get(i));
		}
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
	public abstract String returnStyleSheet();

	public Map<Color, Double> changeMapWithCell(Map<Color, Double> numerator, Cell cell) {
		boolean hasEquivalent = false;
		for (Color stateColor : numerator.keySet()) {
			Color myStateColor = stateNameToColor.get(cell.getState());
			if (stateColor.equals(myStateColor)) {
				hasEquivalent = true;
			}
		}
		if(!hasEquivalent) {
			Color stateColor = stateNameToColor.get(cell.getState());
			numerator.put(stateColor, 0.0);
		}
		else {
			Color stateColor = stateNameToColor.get(cell.getState());
			numerator.put(stateColor, numerator.get(stateColor)+1);
		}
		return numerator;
	}
	
	public Map<Color, Number> returnProportion() {
		int total=0;
		Map<Color, Double> numerator=new HashMap<Color, Double>();		
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				numerator = changeMapWithCell(numerator, myCells[i][j]);
				total++;
			}
		}
		Map<Color, Number> proportions=new HashMap<Color, Number>();
		for (Color stateColor : numerator.keySet()) {
			proportions.put(stateColor, numerator.get(stateColor)/((double) total));
		}
		return proportions;
	}
	
	public void changeState(Group root, Cell myCell){
		myTime.stop();
		for (int i=0; i < stateNames.size(); i++) {
			if (myCell.getState().equals(stateNames.get(i))) {
				String nextState = stateNames.get((i+1) % stateNames.size());
				myCell.setState(nextState);
				myCell.shape.setFill(stateNameToColor.get(nextState));
				break;
			}
		}
	}
		
	
}