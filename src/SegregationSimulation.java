
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;

public class SegregationSimulation extends Simulation {

	private double threshold;
	Cell[][] myGrid;
	
	public SegregationSimulation(Cell[][] myGrid, int thresholdPercentage) {
		threshold = thresholdPercentage*0.01;
		this.myGrid = myGrid;
		setAllNeighbors();
		setCellColor();
	}
	
	public List<Cell> getEmptyCells() {
		List<Cell> emptyCells = new ArrayList<Cell>();
		for (int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				if (myGrid[i][j].getState().equals("EMPTY")) {
					emptyCells.add(myGrid[i][j]);
				}
			}
		}
		return emptyCells;
	}
	
	public boolean moveCell(Cell cell) {
		int numNeighbors = 0;
		int similarNeighbors = 0;
		for (Cell neighbor : cell.getMyNeighbours()) {
			if(!neighbor.getState().equals("EMPTY")) {
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
	
	public void moveAllCells() {
		// what if more dissatisfied cells than open spaces?
		List<Cell> emptyCells = getEmptyCells();
		for(int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				if (moveCell(myGrid[i][j]) && !emptyCells.isEmpty()) {
					emptyCells.get(0).setState(myGrid[i][j].getState());
					myGrid[i][j].setState("EMPTY");
					emptyCells.remove(0);
					emptyCells.add(myGrid[i][j]);
				}
			}
		}
	}
	
	public void setCellColor() {
//		Cell[][] nextGrid = myGrid.clone();
		
		for (int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				if (myGrid[i][j].getState().equals("BLUE")) {
					myGrid[i][j].shape.setFill(Color.BLUE);
				}
				else if (myGrid[i][j].getState().equals("RED")) {
					myGrid[i][j].shape.setFill(Color.RED);
				}
				else {
					myGrid[i][j].shape.setFill(Color.WHITE);
				}
			}
		}
//		myGrid = nextGrid;
	}
	
	public void setAllNeighbors() {
		for(int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				setNeighbors(myGrid[i][j], i, j);
			}
		}
	}
	
	public void setNeighbors(Cell cell, int row, int col) {
		boolean isFirstRow = (row == 0);
		boolean isLastRow = (row == myGrid.length-1);
		boolean isFirstCol = (col == 0);
		boolean isLastCol = (col == myGrid[0].length-1);
		if (!isFirstRow) {
			if (!isFirstCol) {
				cell.getMyNeighbours().add(myGrid[row-1][col-1]);
			}
			cell.getMyNeighbours().add(myGrid[row-1][col]);
			if (!isLastCol) {
				cell.getMyNeighbours().add(myGrid[row-1][col+1]);
			}
		}
		if (!isFirstCol) {
			cell.getMyNeighbours().add(myGrid[row][col-1]);
		}
		if (!isLastCol) {
			cell.getMyNeighbours().add(myGrid[row][col+1]);
		}
		if (!isLastRow) {
			if (!isFirstCol) {
				cell.getMyNeighbours().add(myGrid[row+1][col-1]);
			}
			cell.getMyNeighbours().add(myGrid[row+1][col]);
			if (!isLastCol) {
				cell.getMyNeighbours().add(myGrid[row+1][col+1]);
			}
		}
	}
	
	public void update() {
		moveAllCells();
		setCellColor();
	}
}