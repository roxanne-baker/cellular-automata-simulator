import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

public class PredatorPreySimulation extends Simulation {

	private PredatorPreyCell[][] myCells;
	private int turnsUntilPreyBreeds;
	private int turnsUntilPredatorBreeds;
	private int turnsUntilPredatorStarves;

	public PredatorPreySimulation(Cell[][] newCells, int predatorStarve, int predatorBreed, int preyBreed){
		setMyCells(newCells);
		turnsUntilPreyBreeds = preyBreed;
		turnsUntilPredatorStarves = predatorStarve;
		turnsUntilPredatorBreeds = predatorBreed;
		setAllNeighbors();
		setCellColor();
	}

	public void setCellColor(){
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals("EMPTY")) {
					myCells[i][j].shape.setFill(Color.BLUE);
				}
				else if (myCells[i][j].getState().equals("PREDATOR")) {
					myCells[i][j].shape.setFill(Color.GRAY);
				}
				else if (myCells[i][j].getState().equals("PREY")) {
					myCells[i][j].shape.setFill(Color.ORANGE);
				}				
			}
		}
	}

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
		updatePredatorStates();
		updatePreyStates();
		killAndBreed();
		setCellColor();
	}

	private void updatePredatorStates() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals("PREDATOR"))
					updatePredatorState(myCells[i][j]);
			}
		}		
	}

	private void updatePreyStates() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals("PREY"))
					updatePreyState(myCells[i][j]);
			}
		}		
	}

	private void killAndBreed() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				killPredatorOrBreed(myCells[i][j]);
				myCells[i][j].justUpdated = false;
			}
		}		
	}		

	public void updatePredatorState(PredatorPreyCell cell) {
		if (cell.justUpdated) return;
		List<PredatorPreyCell> neighbours = (List<PredatorPreyCell>)(List<?>) cell.getMyNeighbours();
		List<Integer> openSpaces = getNeighboursOfState("EMPTY", neighbours);
		List<Integer> preySpaces = getNeighboursOfState("PREY", neighbours);
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

	public void killPredatorOrBreed(PredatorPreyCell cell) {		
		if (cell.getState().equals("PREDATOR") && cell.turnsSinceBreeding > turnsUntilPredatorStarves) {
			cell.setState("EMPTY");
			cell.turnsSinceBreeding = 0;
		}
		else {
			breed("PREY", turnsUntilPreyBreeds, cell);
			breed("PREDATOR", turnsUntilPredatorBreeds, cell);
		}
	}

	public void breed(String predatorOrPrey, int numberTurnsUntilBreed, PredatorPreyCell cell) {
		Random random = new Random();
		List<PredatorPreyCell> neighbours = (List<PredatorPreyCell>)(List<?>) cell.getMyNeighbours();
		List<Integer> openSpaces = getNeighboursOfState("EMPTY", neighbours);

		if (cell.getState().equals(predatorOrPrey) && !openSpaces.isEmpty() && cell.turnsSinceBreeding > numberTurnsUntilBreed) {
			int indexToBreedAt = random.nextInt(openSpaces.size());
			neighbours.get(indexToBreedAt).setState(predatorOrPrey);
			cell.turnsSinceBreeding = 0;
		}		
	}


	public void updatePreyState(PredatorPreyCell cell) {
		if (cell.justUpdated) return;
		List<PredatorPreyCell> neighbours = (List<PredatorPreyCell>)(List<?>) cell.getMyNeighbours();

		List<Integer> openSpaces = getNeighboursOfState("EMPTY", neighbours);
		int indexToSwitchWith = getPreyNeighbourToSwitch(openSpaces);

		if (indexToSwitchWith >= 0) {
			neighbours.get(indexToSwitchWith).turnsSinceEating = 0;	
			switchCells(neighbours.get(indexToSwitchWith), cell);	
		}
		else {
			cell.turnsSinceBreeding += 1;
		}
	}


	public List<Integer> getNeighboursOfState(String state, List<PredatorPreyCell> neighbourCells) {
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

		cell.setState("EMPTY");
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

	public void setAllNeighbors() {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				setNeighbors(myCells[i][j], i, j);
			}
		}
	}

	public void setNeighbors(Cell cell, int row, int col) {
		boolean isFirstRow = (row == 0);
		boolean isLastRow = (row == myCells.length-1);
		boolean isFirstCol = (col == 0);
		boolean isLastCol = (col == myCells[0].length-1);
		if (!isFirstRow) {
			cell.getMyNeighbours().add(myCells[row-1][col]);
		}
		if (!isFirstCol) {
			cell.getMyNeighbours().add(myCells[row][col-1]);
		}
		if (!isLastCol) {
			cell.getMyNeighbours().add(myCells[row][col+1]);
		}
		if (!isLastRow) {
			cell.getMyNeighbours().add(myCells[row+1][col]);
		}
	}

}
