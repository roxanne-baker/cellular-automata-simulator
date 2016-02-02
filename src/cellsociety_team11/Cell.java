package cellsociety_team11;

import java.util.ArrayList;

public class Cell {
	private int countNeighbours;
	private ArrayList<Cell> myNeighbours;
	private String state;
	private String id;
	public int getNeighbourCount(){
		return countNeighbours;
	}
	public ArrayList<Cell> getMyNeighbours(){
		return myNeighbours;
	}
	public String getState(){
		return state;
	}
	public void setState(String newState){
		state=newState;
	}
	public String getId(){
		return id;
	}

}
