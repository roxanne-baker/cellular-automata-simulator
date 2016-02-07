package cellsociety_team11;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.paint.Color;
import javafx.scene.shape.Shape;

public class Cell {
	private int countNeighbours;
	public List<Cell> neighbours;
	private String state;
	public final Shape shape;
	boolean justUpdated = false;
	
	public Cell (Shape myShape){
		shape=myShape;
		neighbours=new ArrayList<Cell>();
	}
	public int getNeighbourCount(){
		return countNeighbours;
	}
	public void setNeighbours(List<Cell> myneighbours){
		neighbours=myneighbours;
	}
	public void add(Cell newCell){
		neighbours.add(newCell);
	}
	public List<Cell> getMyNeighbours(){
		return neighbours;
	}
	public String getState(){
		return state;
	}
	public void setState(String newState){
		state=newState;
	}	
}