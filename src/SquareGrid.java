import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class SquareGrid extends Grid {

	public SquareGrid(Group root, int width, int height) {
		super(root,width,height);
	}
	
	public void setGrid(int width, int height) {
		
		myCells = new Cell[width][height];
		int cellWidth = 350/width;
		int cellHeight = 350/height;
		for (int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				Shape cellShape = getSquare(i*cellWidth+25, j*cellHeight+30, cellWidth, cellHeight);
				myCells[i][j] = new Cell(cellShape);
			}
		}
	}
	
	public Rectangle getSquare(int leftX, int topY, int width, int height) {
		Rectangle r = new Rectangle();
		r.setX(leftX);
		r.setY(topY);
		r.setWidth(width);
		r.setHeight(height);
		
		r.setFill(Color.WHITE);
		r.setStroke(Color.BLACK);
		r.setStrokeType(StrokeType.INSIDE);
		r.setStrokeWidth(1);
		
		return r;
	}	
}