import java.util.List;

import javafx.scene.shape.Shape;

public class PredatorPreyCell extends Cell {

	public List<Cell> neighbours;
	public int turnsSinceBreeding = 0;
	public int turnsSinceEating = 0;
	boolean justVacated = false;
	
	public PredatorPreyCell(Shape shape) {
		super(shape);
	}
	
}
