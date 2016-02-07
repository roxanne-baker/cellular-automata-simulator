import javafx.scene.shape.Shape;

public class PredatorCell extends PredatorPreyCell {

	public int turnsSinceEating = 0;
	
	public PredatorCell(Shape shape) {
		super(shape);
	}
	
}
