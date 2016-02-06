
import java.util.Random;

import javafx.scene.paint.Color;

public class FireSimulation extends Simulation {

	private double probCatch;
	
	public FireSimulation(Grid myGrid, double probCatchDecimal) {
		super(myGrid);
		probCatch = probCatchDecimal;
		myGrid.addAllNeighbors((cell, position) -> myGrid.addCardinalNeighbors(cell, position));
	}
	
	public void updateCell(Cell cell) {
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
	
	public void updateCellStates() {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				updateCell(myCells[i][j]);
			}
		}
	}
	
	public void setCellColor(Cell cell) {
		if (cell.getState().equals("TREE")) {
			cell.shape.setFill(Color.FORESTGREEN);
		}
		else if (cell.getState().equals("FIRE")) {
			cell.shape.setFill(Color.FIREBRICK);
		}
		else {
			cell.shape.setFill(Color.TAN);
		}
	}
	
	public void update() {
		updateCellStates();
		setCellColor();
		resetJustUpdated();
	}
}