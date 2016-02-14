import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class GameOfLifeSimulation extends Simulation {

	private int count=0;
	private Timeline myTime;
	private static final String STYLESHEET = "default.css";
	public static final String ALIVE = "alive";
	public static final String DEAD = "dead";
	
	/** pass in Grid in order to set neighbors accordingly
	 * 
	 * @param newGrid
	 */
<<<<<<< HEAD
	public GameOfLifeSimulation(Grid newGrid, Group root, Timeline animation, Border border){
		super(newGrid);
=======
	public GameOfLifeSimulation(Grid newGrid, Group root, Timeline animation, Border border, String myName){
		super(newGrid, myName);
		/**
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addCardinalNeighbors(grid, position));
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addDiagonalNeighbors(grid, position));
		**/
>>>>>>> c6bc429b063e5074d230a944bec42a35ebf86f04
		border.setGridAndBorders(myCells, true);
		numberOfStates=2;
		myTime=animation;
		addListeners(myCells, root);
	}
	
	public void setStateNameToColor() {
		List<String> stateNames = new ArrayList<String>(Arrays.asList(ALIVE, DEAD));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.BLACK, Color.WHITE));
		setStateNameToColor(stateNames, colorNames);
	}
	
	public String[][] updateCell(Cell cell, int[] position, String[][] newState) {
		int row = position[0];
		int col = position[1];
		int liveNeighbours = getNumLiveNeighbours(cell);
		if((liveNeighbours<2 || liveNeighbours>3) && cell.getState().equals(ALIVE)){
			newState[row][col] = DEAD;
		}
		if(liveNeighbours == 3 && cell.getState().equals(DEAD)){
			newState[row][col] = ALIVE;
		}
		return newState;
	}
	
	public int getNumLiveNeighbours(Cell cell) {
		int k = cell.getMyNeighbours().size();
		int numLiveNeighbours=0;
		List<Cell> neighbours = cell.getMyNeighbours();
		for(int l=0;l<k;l++){
			if(neighbours.get(l).getState().equals(ALIVE)){
				numLiveNeighbours++;
			}
		}
		return numLiveNeighbours;
	}
	
	public void updateCellStates() {
		System.out.println("Count:" + count);
		count++;
		String[][] newState = getInitialStates();
		newState = getNextStates(newState);
		setNextStates(newState);
	}
	
	private String[][] getNextStates(String[][] newState) {
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				int liveNeighbours = getNumLiveNeighbours(myCells[i][j]);
				if((liveNeighbours<2 || liveNeighbours>3) && myCells[i][j].getState().equals(ALIVE)){
					newState[i][j] = DEAD;
				}
				if(liveNeighbours == 3 && myCells[i][j].getState().equals(DEAD)){
					newState[i][j] = ALIVE;
				}
			}
		}
		return newState;
	}

	/**
	 * Updates the cells with their new states
	 * @param newState Double array of strings holding the new states of the cells
	 */

	public void setNextStates(String[][] newState) {
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				myCells[i][j].setState(newState[i][j]);
			}
		}
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
		double[]param =new double[2];
		param[0]=myCells.length;
		param[1]=myCells[0].length;
		return param;
	}
	
	/**
	 * Calls methods to update the cells and set their colors
	 */
	public void update() {
		updateCellStates();
		setCellColor(myCells);
	}
	
}