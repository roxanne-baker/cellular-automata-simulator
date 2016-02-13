import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SugarScapeSimulation extends Simulation{

	private SugarScapeCell[][] myCells;
	public static final String HAS_AGENT = "HAS AGENT";
	public static final String NO_AGENT = "NO AGENT";
	
	private int numberOfStates=3;
	private Timeline myTime;
	private static final String STYLESHEET= "predator.css";  // NEED TO CHANGE
	private Border myBorder;
	
	public SugarScapeSimulation(Grid newGrid, Group root, Timeline animation, Border border){
		setMyCells(newGrid.myCells);
		setCellColor(myCells);
		myBorder = border;
		myBorder.setGridAndBorders(myCells, false);
		myTime=animation;
		addListeners(myCells,root);
		
	}
	
	public void setMyCells(Cell[][] newCells) {
		myCells = new SugarScapeCell[newCells.length][newCells[0].length];
		for (int i=0; i<newCells.length; i++) {
			for (int j=0; j<newCells[0].length; j++) {
				myCells[i][j] = new SugarScapeCell(newCells[i][j].shape, i, j, 1);
				myCells[i][j].setState(newCells[i][j].getState());
			}
		}
	}
	
	public void updateAgentSugarAtCell(SugarAgent agent) {
		agent.updateSugar();
	}
	
	public void killAgentAtCell(SugarAgent agent) {
		agent.killAgentIfNeeded();
	}
	
	
	public void actionOnAllCells(Consumer<SugarAgent> action) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].agent != null) {
					action.accept(myCells[i][j].agent);
				}
			}
		}		
	}
	
	public void updateGroundPatches() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].agent != null) {
					myCells[i][j].update();
				}
			}
		}
	}
	
	public void update() {
		actionOnAllCells((SugarAgent agent) -> agent.updateSugar());
		actionOnAllCells((SugarAgent agent) -> agent.killAgentIfNeeded());
		updateGroundPatches();
		setAllStates();
		setCellColor(myCells);
	}
	
	
	
	
	public void setStateNameToColor() {		
		List<String> stateNames = new ArrayList<String>(Arrays.asList(HAS_AGENT, NO_AGENT));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.BLUE, Color.GREEN));
		setStateNameToColor(stateNames, colorNames);
	}
	
	public void setCellColor(SugarScapeCell[][] myGrid) {
		for (int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				myGrid[i][j].shape.setFill(getColor(myGrid[i][j]));
			}
		}
	}
	
	public Color getColor(SugarScapeCell cell) {
		if (cell.agent == null) {
			return new Color(0, 255, 0, (cell.sugarAmount)/(cell.sugarMax));
		}
		else {
			return stateNameToColor.get(cell.getState());
		}
	}
	
	public void setAllStates() {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				setStateCell(myCells[i][j]);
			}
		}		
	}
	
	public void setStateCell(SugarScapeCell cell) {
		if (cell.agent == null) {
			cell.setState(NO_AGENT);
		}
		else {
			cell.setState(HAS_AGENT);
		}		
	}	
	

	public SugarScapeCell[][] getMyCells() {
		return myCells;
	}

	public void setMyCells(SugarScapeCell[][] myCells) {
		this.myCells = myCells;
	}
	
	public void addListeners(SugarScapeCell[][] myCells, Group root) {
		for (int i = 0; i < myCells.length; i++) {
			for (int j = 0; j < myCells[0].length; j++) {
				SugarScapeCell newCell;
				newCell = myCells[i][j];
				myCells[i][j].returnNode().addEventHandler(MouseEvent.MOUSE_CLICKED, e -> changeState(root, newCell));
			}
		}
	}

	public void changeState(Group root, SugarScapeCell myCell) {
		myTime.stop();
		if (myCell.getState().equals(NO_AGENT)) {
			myCell.setState(HAS_AGENT);
			myCell.agent = new SugarAgent(this, myBorder);
			myCell.shape.setFill(getColor(myCell));
		} else if (myCell.getState().equals(HAS_AGENT)) {
			myCell.setState(NO_AGENT);
			myCell.agent = null;
			myCell.shape.setFill(getColor(myCell));
		}
	}

	@Override
	public String returnStyleSheet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HashMap<Color, Number> returnProportion() {
		// TODO Auto-generated method stub
		return null;
	}
}
