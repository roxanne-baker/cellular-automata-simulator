
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.scene.paint.Color;

public class FireSimulation extends Simulation {

	/*
	 * @author Roxanne Baker
	 * Contains logic for representing
	 * the Fire simulation
	 */
	private double probCatch;
	public static final String FIRE = "FIRE";
	public static final String TREE = "TREE";
	public static final String EMPTY = "EMPTY";
	
	public FireSimulation(Grid myGrid, double probCatchDecimal) {
		super(myGrid);
		probCatch = probCatchDecimal;
		myGrid.addAllNeighbors(myCells, (grid, position) -> myGrid.addCardinalNeighbors(grid, position));
	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#setStateNameToColor()
	 */
	public void setStateNameToColor() {
		List<String> stateNames = new ArrayList<String>(Arrays.asList(FIRE, TREE, EMPTY));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.FIREBRICK, Color.FORESTGREEN, Color.TAN));
		setStateNameToColor(stateNames, colorNames);
	}
	
	/*
	 * updates the state of a given cell
	 */
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
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#updateCellStates()
	 */
	public void updateCellStates() {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				updateCell(myCells[i][j]);
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
		resetJustUpdated();
	}
}