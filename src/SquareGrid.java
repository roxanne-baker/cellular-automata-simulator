import java.util.function.BiConsumer;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;

public class SquareGrid extends Grid {

	/*
	 * @author Roxanne Baker
	 * Creates a grid made up of squares
	 * based on the width and height specified
	 */
	private static final int gridSideLength = 350;
	private static final int gridWidthBuffer = 25;
	private static final int gridHeightBuffer = 30;
	
	public SquareGrid(Group root, int width, int height) {
		super(root,width,height);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Grid#setGrid(int, int)
	 */
	public void setGrid(int height, int width) {
		myCells = new Cell[height][width];
		int cellWidth = gridSideLength/width;
		int cellHeight = gridSideLength/height;
		for (int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				Shape cellShape = getSquare(j*cellWidth+gridWidthBuffer, i*cellHeight+gridHeightBuffer, cellWidth, cellHeight);
				myCells[i][j] = new Cell(cellShape);
			}
		}
	}
	
	/*
	 * @return a Rectangle placed at the given location and of the 
	 * given width and height
	 * Default fill is white with a black border
	 */
	private Rectangle getSquare(int leftX, int topY, int width, int height) {
		Rectangle rectangle = new Rectangle();
		rectangle.setX(leftX);
		rectangle.setY(topY);
		rectangle.setWidth(width);
		rectangle.setHeight(height);
		
		rectangle.setFill(Color.WHITE);
		rectangle.setStroke(Color.BLACK);
		rectangle.setStrokeType(StrokeType.INSIDE);
		rectangle.setStrokeWidth(1);
		
		return rectangle;
	}	
}