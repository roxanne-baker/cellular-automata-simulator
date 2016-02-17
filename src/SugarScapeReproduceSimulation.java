import java.util.function.Consumer;
import javafx.scene.paint.Color;

public class SugarScapeReproduceSimulation extends Simulation{
	/**
	 * represents the grid of the simulation
	 */
	public SugarScapeCell[][] myCells;
	
	/**
	 * state when cell has a male agent
	 */
	public static final String MALE_AGENT = "MALE";
	
	/**
	 * state when cell has a female agent
	 */
	public static final String FEMALE_AGENT = "FEMALE";
	
	/**
	 * state when cell has no agent
	 */
	public static final String NO_AGENT = "NONE";
	
	/**
	 * instance of border used in this simulation
	 */
	private Border myBorder;
	
	/**
	 * String form of name of stylesheet that 
	 * would be used to display graph of concentrations
	 * of different states
	 */
	private static final String STYLESHEET= "default.css";  // NEED TO CHANGE
	
	/**
	 * 
	 * @param newGrid - the grid being passed in to the simulation
	 * @param border - the border being used for the simulation
	 */
	public SugarScapeReproduceSimulation(Grid newGrid, Border border){
		setMyCells(newGrid.myCells);
		setCellColor(myCells);
		myBorder = border;
		myBorder.setGridAndBorders(myCells, false);
	}
	
	/**
	 * converts double array of Cell to double
	 * array of SugarScapeCell so extra attributes
	 * can be referred to
	 * @param newCells
	 */
	public void setMyCells(Cell[][] newCells) {
		myCells = new SugarScapeCell[newCells.length][newCells[0].length];
		for (int i=0; i<newCells.length; i++) {
			for (int j=0; j<newCells[0].length; j++) {
				myCells[i][j] = new SugarScapeCell(newCells[i][j].shape, i, j, 20, 1);
				myCells[i][j].setState(newCells[i][j].getState());
			}
		}
	}
	
	/**
	 * Performs given action on all agents in grid
	 * @param action
	 */
	public void actionOnAllAgents(Consumer<SugarAgentReproduce> agentAction) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getAgent() != null) {
					SugarAgentReproduce reproduceAgent = (SugarAgentReproduce) myCells[i][j].getAgent();
					agentAction.accept(reproduceAgent);
				}
			}
		}		
	}
	
	/**
	 * Performs cellAction on all cells in grid
	 * @param cellAction
	 */
	public void actionOnAllCells(Consumer<SugarScapeCell> cellAction) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				cellAction.accept(myCells[i][j]);
			}
		}		
	}
	
	/**
	 * Performs series of actions on the agents
	 * and cells within the grid
	 */
	public void update() {
		actionOnAllAgents((SugarAgentReproduce agent) -> agent.reproduce());
		actionOnAllAgents((SugarAgentReproduce agent) -> agent.incrementAge());
		actionOnAllAgents((SugarAgentReproduce agent) -> agent.updateSugar());
		actionOnAllAgents((SugarAgentReproduce agent) -> agent.killAgentIfNeeded());
		actionOnAllCells((SugarScapeCell cell) -> cell.update());		// would change to 'updateSugarLevels'
		actionOnAllCells((SugarScapeCell cell) -> setStateCell(cell));
		actionOnAllCells((SugarScapeCell cell) -> setCellColor(cell));
	}
	
	/**
	 * sets the state of the cell based on
	 * the presence/sex of agent there
	 * @param cell
	 */
	public void setStateCell(SugarScapeCell cell) {
		if (cell.getAgent() == null) {
			cell.setState(NO_AGENT);
		}
		else {
			SugarAgentReproduce agent = (SugarAgentReproduce) cell.getAgent();
			if (agent.isMale()) {
				cell.setState(MALE_AGENT);
			}
			else {
				cell.setState(FEMALE_AGENT);
			}
		}		
	}
	
	/**
	 * sets the color for a given cell
	 * @param cell
	 */
	public void setCellColor(SugarScapeCell cell) {
		cell.shape.setFill(getColor(cell));		// would ideally change to 'getShape()'
	}
	
	/**
	 * the method that would actually be used to 
	 * determine the color to fill in the cell
	 * Accounts for concentration of sugar at cell
	 * @param cell
	 * @return Color
	 */
	public Color getColor(SugarScapeCell cell) {
		if (cell.getAgent() == null) {
			return new Color(0, 255, 0, cell.getSugarRatio());
		}
		else {
			return stateNameToColor.get(cell.getState());
		}
	}
	
	public String returnStyleSheet() {	// would ideally remove in complete refactoring
		return STYLESHEET;				// need to keep because abstract method
	}

	public Cell[][] getMyCells() {
		// TODO Auto-generated method stub
		return null;
	}
}