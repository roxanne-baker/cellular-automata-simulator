package cellsociety_team11;

import java.util.ArrayList;

import javafx.scene.shape.Shape;

public class Cell {
	private int countNeighbours;
	private ArrayList<Cell> neighbours;
	private String state;
	public final Shape shape;
	public Cell (Shape myShape){
		shape=myShape;
		neighbours=new ArrayList<Cell>();
	}
	public int getNeighbourCount(){
		return countNeighbours;
	}
	public void setNeighbours(ArrayList<Cell> myneighbours){
		neighbours=myneighbours;
	}
	public void add(Cell newCell){
		neighbours.add(newCell);
	}
	public ArrayList<Cell> getMyNeighbours(){
		return neighbours;
	}
	public String getState(){
		return state;
	}
	public void setState(String newState){
		state=newState;
	}

}
