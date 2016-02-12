import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javafx.scene.paint.Color;

public class GameOfLifeSimulation extends Simulation {

	private int count=0;
	private static final String STYLESHEET = "default.css";
	public static final String ALIVE = "alive";
	public static final String DEAD = "dead";
	
	
	// pass in Grid in order to set neighbors accordingly
	public GameOfLifeSimulation(Grid newGrid, Border border){
		super(newGrid);
		border.setGridAndBorders(myCells, true);	
	}
	
	public void setStateNameToColor() {
		List<String> stateNames = new ArrayList<String>(Arrays.asList(ALIVE, DEAD));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.BLACK, Color.WHITE));
		setStateNameToColor(stateNames, colorNames);
	}
	
	public String[][] updateCell(Cell cell, int[] position, String[][] newState) {
		int row = position[0];
		int col = position[1];
		int liveNeighbours = getNumLiveNeighbours(cell);
		if((liveNeighbours<2 || liveNeighbours>3) && cell.getState().equals(ALIVE)){
			newState[row][col] = DEAD;
		}
		if(liveNeighbours == 3 && cell.getState().equals(DEAD)){
			newState[row][col] = ALIVE;
		}
		return newState;
	}
	
	public int getNumLiveNeighbours(Cell cell) {
		int k = cell.getMyNeighbours().size();
		int numLiveNeighbours=0;
		List<Cell> neighbours = cell.getMyNeighbours();
		for(int l=0;l<k;l++){
			if(neighbours.get(l).getState().equals(ALIVE)){
				numLiveNeighbours++;
			}
		}
		return numLiveNeighbours;
	}
	
	public void updateCellStates() {
		System.out.println("Count:" + count);
		count++;
		String[][] newState = getInitialStates();
		newState = getNextStates(newState);
		setNextStates(newState);
	}
	
	private String[][] getNextStates(String[][] newState) {
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				int liveNeighbours = getNumLiveNeighbours(myCells[i][j]);
				if((liveNeighbours<2 || liveNeighbours>3) && myCells[i][j].getState().equals(ALIVE)){
					newState[i][j] = DEAD;
				}
				if(liveNeighbours == 3 && myCells[i][j].getState().equals(DEAD)){
					newState[i][j] = ALIVE;
				}
			}
		}
		return newState;
	}
	
	private String[][] getInitialStates() {
		String[][] newState=new String[myCells.length][myCells[0].length];
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				newState[i][j]=myCells[i][j].getState();
			}
		}
		return newState;
	}
	
	public void setNextStates(String[][] newState) {
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				myCells[i][j].setState(newState[i][j]);
			}
		}
	}
	
	public HashMap<Color, Number> returnProportion(){
		int countLive=0;
		int countDead=0;
		HashMap<Color, Number> proportions=new HashMap<Color, Number>();
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				if(myCells[i][j].getState().equals(ALIVE)){
					countLive++;
				}
				if(myCells[i][j].getState().equals(DEAD)){
					countDead++;
				}
			}
		}
		double prop1=(double)countLive/(countLive+countDead);
		double prop2=(double)countDead/(countLive+countDead);
		proportions.put(Color.BLACK, prop1);
		proportions.put(Color.WHITE, prop2);
		return proportions;
	}
	public String returnStyleSheet(){
		return STYLESHEET;
	}
	
	public void update() {
		updateCellStates();
		setCellColor(myCells);
	}
	
}