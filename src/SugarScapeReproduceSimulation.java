import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class SugarScapeReproduceSimulation extends Simulation{

	public SugarScapeCell[][] myCells;
	public static final String MALE_AGENT = "MALE";
	public static final String FEMALE_AGENT = "FEMALE";
	public static final String NO_AGENT = "NONE";
	
	private Border myBorder;
	
	private int numberOfStates=3;
	private Timeline myTime;
	private static final String STYLESHEET= "default.css";  // NEED TO CHANGE
	
	public SugarScapeReproduceSimulation(Grid newGrid, Border border, int predatorStarve, int predatorBreed, int preyBreed){
		setMyCells(newGrid.myCells);
		setCellColor(myCells);
		myBorder = border;
		myBorder.setGridAndBorders(myCells, false);
	}
	
	public void setMyCells(Cell[][] newCells) {
		myCells = new SugarScapeCell[newCells.length][newCells[0].length];
		for (int i=0; i<newCells.length; i++) {
			for (int j=0; j<newCells[0].length; j++) {
				myCells[i][j] = new SugarScapeCell(newCells[i][j].shape, i, j, 20, 1);
				myCells[i][j].setState(newCells[i][j].getState());
			}
		}
	}
	
	
	public void reproduceAgentAtCell(SugarAgentReproduce reproduceAgent) {
		reproduceAgent.reproduce();		
	}
	
	public void incrementAgentAgeAtCell(SugarAgentReproduce reproduceAgent) {
		reproduceAgent.incrementAge();		
	}
	
	public void updateAgentSugarAtCell(SugarAgentReproduce reproduceAgent) {
		reproduceAgent.updateSugar();
	}
	
	public void killAgentAtCell(SugarAgentReproduce reproduceAgent) {
		reproduceAgent.killAgentIfNeeded();
	}
	
	
	public void actionOnAllCells(Consumer<SugarAgentReproduce> action) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].agent != null) {
					SugarAgentReproduce reproduceAgent = (SugarAgentReproduce) myCells[i][j].agent;
					action.accept(reproduceAgent);
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
		actionOnAllCells((SugarAgentReproduce agent) -> agent.reproduce());
		actionOnAllCells((SugarAgentReproduce agent) -> agent.incrementAge());
		actionOnAllCells((SugarAgentReproduce agent) -> agent.updateSugar());
		actionOnAllCells((SugarAgentReproduce agent) -> agent.killAgentIfNeeded());
		updateGroundPatches();
		setAllStates();
		setCellColor(myCells);
	}
	
	
	
	public void setStateNameToColor() {		
		List<String> stateNames = new ArrayList<String>(Arrays.asList(MALE_AGENT, FEMALE_AGENT, NO_AGENT));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.BLUE, Color.RED, Color.GREEN));
		setStateNameToColor(stateNames, colorNames);
	}
	
	public void setCellColor(SugarScapeCell[][] myGrid) {
		for (int i=0; i<myGrid.length; i++) {
			for (int j=0; j<myGrid[0].length; j++) {
				myGrid[i][j].shape.setFill(getColor(myGrid[i][j]));
			}
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
			SugarAgentReproduce agent = (SugarAgentReproduce) cell.agent;
			if (agent.isMale == 0) {
				cell.setState(FEMALE_AGENT);
			}
			else {
				cell.setState(MALE_AGENT);
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
			myCell.agent = new SugarAgentReproduce(this, myBorder);
			SugarAgentReproduce agent = (SugarAgentReproduce) myCell.agent;
			agent.setIsMale(true);
			myCell.shape.setFill(getColor(myCell));
		}
		else if (myCell.getState().equals(MALE_AGENT)) {
			myCell.agent = new SugarAgentReproduce(this, myBorder);
			SugarAgentReproduce agent = (SugarAgentReproduce) myCell.agent;
			agent.setIsMale(false);
			myCell.shape.setFill(getColor(myCell));
		}
		else if (myCell.getState().equals(FEMALE_AGENT)) {
			myCell.agent = null;
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
