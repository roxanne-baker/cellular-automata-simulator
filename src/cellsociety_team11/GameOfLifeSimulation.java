package cellsociety_team11;

import java.util.ArrayList;
import java.util.HashMap;

public class GameOfLifeSimulation extends Simulation {
	private Cell[][] myGrid;
	public GameOfLifeSimulation(Cell[][] newGrid){
		myGrid=newGrid;
	}
	@Override
	public void update() {
		// TODO Auto-generated method stub
		for(int i=0;i<myGrid.length;i++){
			for(int j=0;j<myGrid.length;j++){
				int k=myGrid[i][j].getNeighbourCount();
				int count=0;
				ArrayList<Cell> neighbours=new ArrayList<Cell>();
				neighbours=myGrid[i][j].getMyNeighbours();
				for(int l=0;l<k;l++){
					if(neighbours.get(l).getState().equals("live")){
						count++;
					}
				}
				if((count<2 || count>4) && myGrid[i][j].getState().equals("live")){
					myGrid[i][j].setState("dead");
				}
				if(count==3 && myGrid[i][j].getState().equals("dead")){
					myGrid[i][j].setState("live");
				}
			}
		}
	}
	public void updateNeighbours(){
		
	}
	
}
