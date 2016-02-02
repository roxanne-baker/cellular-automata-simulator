import java.util.List;

import javafx.*;
import javafx.scene.shape.Shape;

public class Cell {

	public List<Cell> neighbors;
	public String state;
	public final Shape shape;
	
	public Cell(Shape cellShape){
		shape = cellShape;
		//should take in type Simulation
	}
}
