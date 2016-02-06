import java.util.List;

import javafx.scene.paint.Color;

public class GameOfLifeSimulation extends Simulation {

	private int count=0;
	
	// pass in Grid in order to set neighbors accordingly
	public GameOfLifeSimulation(Grid newGrid){
		super(newGrid);
		newGrid.addAllNeighbors((cell, position) -> newGrid.addCardinalNeighbors(cell, position));
		newGrid.addAllNeighbors((cell, position) -> newGrid.addDiagonalNeighbors(cell, position));		
	}
	
	
	public String[][] updateCell(Cell cell, int[] position, String[][] newState) {
		int row = position[0];
		int col = position[1];
		int liveNeighbours = getNumLiveNeighbours(cell);
		if((liveNeighbours<2 || liveNeighbours>3) && cell.getState().equals("live")){
			newState[row][col] = "dead";
		}
		if(liveNeighbours == 3 && cell.getState().equals("dead")){
			newState[row][col] = "live";
		}
		return newState;
	}
	
	public int getNumLiveNeighbours(Cell cell) {
		int k = cell.getMyNeighbours().size();
		int count=0;
		List<Cell> neighbours = cell.getMyNeighbours();
		for(int l=0;l<k;l++){
			if(neighbours.get(l).getState().equals("live")){
				count++;
			}
		}
		return count;
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
				if((liveNeighbours<2 || liveNeighbours>3) && myCells[i][j].getState().equals("live")){
					newState[i][j] = "dead";
				}
				if(liveNeighbours == 3 && myCells[i][j].getState().equals("dead")){
					newState[i][j] = "live";
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
	
	public void setCellColor(Cell cell) {
		if (cell.getState().equals("live")) {
			cell.shape.setFill(Color.WHITE);
		}
		else if (cell.getState().equals("dead")) {
			cell.shape.setFill(Color.BLACK);
		}
	}
	
	public void update() {
		updateCellStates();
		setCellColor();
	}
	
}