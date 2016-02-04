
import java.util.Random;

import javafx.scene.paint.Color;

public class FireSimulation extends Simulation {

	private double probCatch;
	Cell[][] myCells;
	
	public FireSimulation(Cell[][] myCells, double probCatchDecimal) {
		probCatch = probCatchDecimal;
		this.myCells = myCells;
		setAllNeighbors();
		setCellColor();
	}
	
	public void updateCellState(Cell cell) {
		if (cell.getState().equals("FIRE") && !cell.justUpdated) {
			Random random = new Random();
			cell.setState("EMPTY");
			for (Cell neighbor : cell.getMyNeighbours()) {
				if (neighbor.getState().equals("TREE")) {
					double treeCatch = random.nextDouble();
					if (treeCatch <= probCatch) {
						neighbor.setState("FIRE");
						neighbor.justUpdated = true;
					}
				}
						
			}
		}
	}
	
	public void updateAllCells() {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				updateCellState(myCells[i][j]);
			}
		}
	}
	
	public void setCellColor() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals("TREE")) {
					myCells[i][j].shape.setFill(Color.FORESTGREEN);
				}
				else if (myCells[i][j].getState().equals("FIRE")) {
					myCells[i][j].shape.setFill(Color.FIREBRICK);
				}
				else {
					myCells[i][j].shape.setFill(Color.TAN);
				}
			}
		}
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
	
	public void update() {
		updateAllCells();
		setCellColor();
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				myCells[i][j].justUpdated = false;
			}
		}
	}
}