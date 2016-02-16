import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PredatorPreySimulation extends Simulation {

	/*
	 * @author Roxanne Baker
	 * Contains logic for representing the
	 * PredatorPrey simulation
	 */
	private PredatorPreyCell[][] myCells;
	private int turnsUntilPreyBreeds;
	private int turnsUntilPredatorBreeds;
	private int turnsUntilPredatorStarves;
	public static final String PREDATOR = "predator";
	public static final String PREY = "prey";
	public static final String EMPTY = "empty";

	private int numberOfStates=3;
	private Timeline myTime;
	private static final String STYLESHEET= "predator.css";
	
	public PredatorPreySimulation(Grid newGrid, int predatorStarve, int predatorBreed, int preyBreed, Group root, Timeline animation, Border border, String myName){
		setMyCells(newGrid.myCells);
		setCellColor(myCells);
		turnsUntilPreyBreeds = preyBreed;
		turnsUntilPredatorStarves = predatorStarve;
		turnsUntilPredatorBreeds = predatorBreed;
		border.setGridAndBorders(myCells, false);
		myTime=animation;
		name=myName;
		addListeners(myCells,root);

	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#setStateNameToColor()
	 */
	public void setStateNameToColor() {
		List<String> stateNames = new ArrayList<String>(Arrays.asList(PREDATOR, PREY, EMPTY));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.GRAY, Color.ORANGE, Color.BLUE));
		setStateNameToColor(stateNames, colorNames);
	}
	


	/*
	 * Cells have additional properties that need
	 * to be kept track of (time since breeding/feeding)
	 * must convert each cell to a PredatorPreyCell
	 */
	public void setMyCells(Cell[][] newCells) {
		myCells = new PredatorPreyCell[newCells.length][newCells[0].length];
		for (int i=0; i<newCells.length; i++) {
			for (int j=0; j<newCells[0].length; j++) {
				myCells[i][j] = new PredatorPreyCell(newCells[i][j].shape);
				myCells[i][j].setState(newCells[i][j].getState());
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see Simulation#update()
	 */
	public void update() {
		updateCellStates();
		setCellColor(myCells);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#updateCellStates()
	 */
	public void updateCellStates() {
		moveCellsOfState(PREDATOR, cell -> updatePredatorState(cell));
		moveCellsOfState(PREY, cell -> updatePreyState(cell));
		updateCell(cell -> killPredatorOrBreed(cell));
	}
	
	/*
	 * updates a given property of a cell given the cell
	 */
	private void updateCell(Consumer<PredatorPreyCell> updateProperty) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
					updateProperty.accept(myCells[i][j]);					
			}
		}			
	}
	
	/*
	 * Given a String representing a certain state and a function
	 * will move all cells of that state by the function provided
	 */
	private void moveCellsOfState(String state, Consumer<PredatorPreyCell> moveStateFunction) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals(state)) {
					moveStateFunction.accept(myCells[i][j]);					
				}	
			}
		}			
	}	

	/*
	 * contains logic for updating the state of a cell
	 * that is a predator
	 */
	public void updatePredatorState(PredatorPreyCell cell) {
		if (cell.justUpdated) return;
		List<PredatorPreyCell> neighbours = getPredPreyNeighbours(cell);
		List<Integer> openSpaces = getNeighboursOfState(EMPTY, neighbours);
		List<Integer> preySpaces = getNeighboursOfState(PREY, neighbours);
		int indexToSwitchWith = getPredatorNeighbourToSwitch(openSpaces, preySpaces);	

		if (indexToSwitchWith >= 0) {
			if (openSpaces.contains(indexToSwitchWith)) {
				neighbours.get(indexToSwitchWith).turnsSinceEating = cell.turnsSinceEating + 1;
			}
			else {
				neighbours.get(indexToSwitchWith).turnsSinceEating = 0;
			}
			switchCells(neighbours.get(indexToSwitchWith), cell);			
		}
		else {
			cell.turnsSinceBreeding += 1;
			cell.turnsSinceEating += 1;
		}		
	}
	
	/*
	 * contains logic for updating the state of a cell
	 * that is prey
	 */
	public void updatePreyState(PredatorPreyCell cell) {
		if (cell.justUpdated) return;
		List<PredatorPreyCell> neighbours = getPredPreyNeighbours(cell);
		List<Integer> openSpaces = getNeighboursOfState(EMPTY, neighbours);
		int indexToSwitchWith = getPreyNeighbourToSwitch(openSpaces);

		if (indexToSwitchWith >= 0) {
			neighbours.get(indexToSwitchWith).turnsSinceEating = 0;	
			switchCells(neighbours.get(indexToSwitchWith), cell);	
		}
		else {
			cell.turnsSinceBreeding += 1;
		}
	}
	
	/*
	 * gets the neighbours of a cell as a list of PredatorPreyCell's
	 * instead of as just a list of Cells
	 * This allows us to access properties unique to PredatorPreyCell's
	 */
	public List<PredatorPreyCell> getPredPreyNeighbours(PredatorPreyCell cell) {
		List<PredatorPreyCell> predPreyNeighbours = new ArrayList<PredatorPreyCell>();
		for (Cell neighbour : cell.getMyNeighbours()) {
			predPreyNeighbours.add((PredatorPreyCell) neighbour);				
		}		
		return predPreyNeighbours;
	}

	/*
	 * Given a cell, will determine whether it should
	 * breed or (in the case of a predator) die
	 */
	public void killPredatorOrBreed(PredatorPreyCell cell) {
		cell.justUpdated = false;
		if (cell.getState().equals(PREDATOR) && cell.turnsSinceEating > turnsUntilPredatorStarves) {
			cell.setState(EMPTY);
			cell.turnsSinceBreeding = 0;
		}
		else {
			breed(PREY, turnsUntilPreyBreeds, cell);
			breed(PREDATOR, turnsUntilPredatorBreeds, cell);
		}
	}

	/*
	 * Contains logic for breeding the given type of cell
	 * and a value for numberTurnsUntilBreed
	 */
	public void breed(String predatorOrPrey, int numberTurnsUntilBreed, PredatorPreyCell cell) {
		Random random = new Random();
		List<PredatorPreyCell> neighbours = getPredPreyNeighbours(cell);
		
		List<Integer> openSpaces = getNeighboursOfState(EMPTY, neighbours);

		if (cell.getState().equals(predatorOrPrey) && !openSpaces.isEmpty() && cell.turnsSinceBreeding > numberTurnsUntilBreed) {
			int indexToBreedAt = random.nextInt(openSpaces.size());
			neighbours.get(indexToBreedAt).setState(predatorOrPrey);
			cell.turnsSinceBreeding = 0;
		}		
	}

	/*
	 * Gets all neighbours of the given state
	 */
	public List<Integer> getNeighboursOfState(String state, List<PredatorPreyCell> neighbourCells) {
		List<Integer> stateSpaces = new ArrayList<Integer>();
		for (int i=0; i<neighbourCells.size(); i++) {
			if (neighbourCells.get(i).getState().equals(state) && neighbourCells.get(i).justUpdated == false) {
				stateSpaces.add(i);
			}
		}	
		return stateSpaces;		
	}

	/*
	 * Takes two cells and switches their states
	 * and updates their values for turnsSinceBreeding
	 */
	public void switchCells(PredatorPreyCell neighbour, PredatorPreyCell cell) {
		neighbour.setState(cell.getState());
		neighbour.turnsSinceBreeding = cell.turnsSinceBreeding + 1;
		neighbour.justUpdated = true;

		cell.setState(EMPTY);
		cell.turnsSinceBreeding = 0;
		cell.justUpdated = true;	
	}

	/*
	 * @return int representing an index in the given list to move to at random
	 */
	private int getIndexToSwitch(List<Integer> stateSpaces) {
		Random random = new Random();
		int indexToSwitchWith = -1;
		if (!stateSpaces.isEmpty()) {
			int stateSpaceIndex = random.nextInt(stateSpaces.size());	
			indexToSwitchWith = stateSpaces.get(stateSpaceIndex);
		}	
		return indexToSwitchWith;
	}

	/*
	 * @return an index for a prey to switch to
	 * from its openSpaces
	 */
	private int getPreyNeighbourToSwitch(List<Integer> openSpaces) {
		return getIndexToSwitch(openSpaces);
	}

	/*
	 * @return int representing index for a predator to switch with
	 * from either nearby prey or an empty space
	 */
	private int getPredatorNeighbourToSwitch(List<Integer> openSpaces, List<Integer> preySpaces) {
		int indexToSwitchWith = getIndexToSwitch(preySpaces);
		if (indexToSwitchWith == -1) {
			indexToSwitchWith = getIndexToSwitch(openSpaces);
		}
		return indexToSwitchWith;
	}	

	public int getNumberOfStates(){
		return numberOfStates;
	}
	public String returnStyleSheet(){
		return STYLESHEET;
	}
	public void setPreyBreeds(int number){
		turnsUntilPreyBreeds=number;
	}
	public void setPredatorBreeds(int number){
		turnsUntilPredatorBreeds=number;
	}
	public void setPredatorStarves(int number){
		turnsUntilPredatorStarves=number;
	}
	public HashMap<Color, Number> returnProportion(){
		int countPredator=0;
		int countPrey=0;
		int countEmpty=0;
		int total=0;
		HashMap<Color, Number> proportions=new HashMap<Color, Number>();
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				if(myCells[i][j].getState().equals(PREDATOR)){
					countPredator++;
				}
				else if(myCells[i][j].getState().equals(PREY)){
					countPrey++;
				}
				else if(myCells[i][j].getState().equals(EMPTY)){
					countEmpty++;
				}
				total++;
			}
		}
		double prop1=(double)countPredator/total;
		double prop2=(double)countPrey/total;
		double prop3=(double)countEmpty/total;
		proportions.put(Color.GRAY, prop1);
		proportions.put(Color.ORANGE, prop2);
		proportions.put(Color.WHITE, prop3);
		return proportions;
	}

	public void addListeners(Cell[][] myCells, Group root) {
		for (int i = 0; i < myCells.length; i++) {
			for (int j = 0; j < myCells[0].length; j++) {
				Cell newCell;
				newCell = myCells[i][j];
				myCells[i][j].returnNode().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changeState(root, newCell));
			}
		}
	}

	public double[] returnParameters(){
		double[]param =new double[5];
		param[0]=myCells.length;
		param[1]=myCells[0].length;
		param[2]= turnsUntilPreyBreeds;
		param[3]= turnsUntilPredatorBreeds;
		param[4]= turnsUntilPredatorStarves;
		return param;
	}
}

