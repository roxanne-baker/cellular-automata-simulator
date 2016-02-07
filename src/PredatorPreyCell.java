
import javafx.scene.shape.Shape;

public class PredatorPreyCell extends Cell {
	/*
	 * special type of cell for PredatorPreySimulation
	 * to account for additional parameters
	 */
	public int turnsSinceBreeding = 0;
	public int turnsSinceEating = 0;
	
	public PredatorPreyCell(Shape shape) {
		super(shape);
	}	
}
