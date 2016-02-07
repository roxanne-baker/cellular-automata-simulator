import java.util.HashMap;
import java.util.Map;

import javafx.scene.paint.Color;

public class Simulation {
	
	Cell[][] myCells;
	Map<String, Color> stateNameToColor;
	
	public Simulation(Grid newGrid) {
		this();
		myCells = newGrid.myCells;
		setCellColor(myCells);
	}
	
	public Simulation(){
		setStateNameToColor();
	}
	
	public void setStateNameToColor() {
		stateNameToColor = new HashMap<String, Color>();
	}
	
	public void update(){};
	
	public void updateCellStates(){};
	
	public void setCellColor(Cell cell) {
		cell.shape.setFill(getColorFromStateName(cell.getState()));
	}
	
	public void setCellColor(Cell[][] myGrid) {
		for (int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				myGrid[i][j].shape.setFill(getColorFromStateName(myGrid[i][j].getState()));
			}
		}
	}
	
	public Color getColorFromStateName(String state) {
		return stateNameToColor.get(state);
	}
	
	public void resetJustUpdated() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				myCells[i][j].justUpdated = false;
			}
		}
	}
	
}