import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.function.Consumer;

import javafx.scene.paint.Color;

public class PredatorPreySimulation extends Simulation {

	private PredatorPreyCell[][] myCells;
	private int turnsUntilPreyBreeds;
	private int turnsUntilPredatorBreeds;
	private int turnsUntilPredatorStarves;
	
	public static final Color PREDATOR = Color.GRAY;
	public static final Color PREY = Color.ORANGE;
	public static final Color EMPTY = Color.BLUE;

	public PredatorPreySimulation(Grid newGrid, int predatorStarve, int predatorBreed, int preyBreed){
		setMyCells(newGrid.myCells);
		setCellColor(myCells);
		turnsUntilPreyBreeds = preyBreed;
		turnsUntilPredatorStarves = predatorStarve;
		turnsUntilPredatorBreeds = predatorBreed;
		newGrid.addAllNeighbors(myCells, (grid, position) -> newGrid.addCardinalNeighbors(grid, position));
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

	public void update() {
		updateCellStates();
		setCellColor(myCells);
	}
	
	public void updateCellStates() {
		moveCellsOfState(PREDATOR, cell -> updatePredatorState(cell));
		moveCellsOfState(PREY, cell -> updatePreyState(cell));
		updateCell(cell -> killPredatorOrBreed(cell));
	}
	
	private void updateCell(Consumer<PredatorPreyCell> updateProperty) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
					updateProperty.accept(myCells[i][j]);					
			}
		}			
	}
	
	private void moveCellsOfState(Color state, Consumer<PredatorPreyCell> moveStateFunction) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals(state)) {
					moveStateFunction.accept(myCells[i][j]);					
				}	
			}
		}			
	}	

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
	
	public List<PredatorPreyCell> getPredPreyNeighbours(PredatorPreyCell cell) {
		List<PredatorPreyCell> predPreyNeighbours = new ArrayList<PredatorPreyCell>();
		for (Cell neighbour : cell.getMyNeighbours()) {
			predPreyNeighbours.add((PredatorPreyCell) neighbour);				
		}		
		return predPreyNeighbours;
	}

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

	public void breed(Color predatorOrPrey, int numberTurnsUntilBreed, PredatorPreyCell cell) {
		Random random = new Random();
		List<PredatorPreyCell> neighbours = getPredPreyNeighbours(cell);
		
		List<Integer> openSpaces = getNeighboursOfState(EMPTY, neighbours);

		if (cell.getState().equals(predatorOrPrey) && !openSpaces.isEmpty() && cell.turnsSinceBreeding > numberTurnsUntilBreed) {
			int indexToBreedAt = random.nextInt(openSpaces.size());
			neighbours.get(indexToBreedAt).setState(predatorOrPrey);
			cell.turnsSinceBreeding = 0;
		}		
	}

	public List<Integer> getNeighboursOfState(Color state, List<PredatorPreyCell> neighbourCells) {
		List<Integer> stateSpaces = new ArrayList<Integer>();
		for (int i=0; i<neighbourCells.size(); i++) {
			if (neighbourCells.get(i).getState().equals(state) && neighbourCells.get(i).justUpdated == false) {
				stateSpaces.add(i);
			}
		}	
		return stateSpaces;		
	}

	public void switchCells(PredatorPreyCell neighbour, PredatorPreyCell cell) {
		neighbour.setState(cell.getState());
		neighbour.turnsSinceBreeding = cell.turnsSinceBreeding + 1;
		neighbour.justUpdated = true;

		cell.setState(EMPTY);
		cell.turnsSinceBreeding = 0;
		cell.justUpdated = true;	
	}

	private int getIndexToSwitch(List<Integer> stateSpaces) {
		Random random = new Random();
		int indexToSwitchWith = -1;
		if (!stateSpaces.isEmpty()) {
			int stateSpaceIndex = random.nextInt(stateSpaces.size());	
			indexToSwitchWith = stateSpaces.get(stateSpaceIndex);
		}	
		return indexToSwitchWith;
	}

	private int getPreyNeighbourToSwitch(List<Integer> openSpaces) {
		return getIndexToSwitch(openSpaces);
	}

	private int getPredatorNeighbourToSwitch(List<Integer> openSpaces, List<Integer> preySpaces) {
		int indexToSwitchWith = getIndexToSwitch(preySpaces);
		if (indexToSwitchWith == -1) {
			indexToSwitchWith = getIndexToSwitch(openSpaces);
		}
		return indexToSwitchWith;
	}	
}
