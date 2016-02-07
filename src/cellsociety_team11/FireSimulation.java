package cellsociety_team11;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

public class FireSimulation extends Simulation {

	private double probCatch;
	public static final String FIRE = "FIRE";
	public static final String TREE = "TREE";
	public static final String EMPTY = "EMPTY";
	
	public FireSimulation(Grid myGrid, double probCatchDecimal) {
		super(myGrid);
		probCatch = probCatchDecimal;
		myGrid.addAllNeighbors(myCells, (grid, position) -> myGrid.addCardinalNeighbors(grid, position));
	}
	
	public void setStateNameToColor() {
		stateNameToColor = new HashMap<String, Color>();
		stateNameToColor.put(FIRE, Color.FIREBRICK);
		stateNameToColor.put(TREE, Color.FORESTGREEN);
		stateNameToColor.put(EMPTY, Color.TAN);
	}
	
	public void updateCell(Cell cell) {
		if (cell.getState().equals(FIRE) && !cell.justUpdated) {
			Random random = new Random();
			cell.setState(EMPTY);
			for (Cell neighbor : cell.getMyNeighbours()) {
				if (neighbor.getState().equals(TREE)) {
					double treeCatch = random.nextDouble();
					if (treeCatch <= probCatch) {
						neighbor.setState(FIRE);
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
	
	public void update() {
		updateCellStates();
		setCellColor(myCells);
		resetJustUpdated();
	}
}