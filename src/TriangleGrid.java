import java.util.Arrays;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class TriangleGrid extends Grid {

	/*
	 * @author Roxanne Baker
	 * Creates a grid made up of squares
	 * based on the width and height specified
	 */
	private static final double gridSideLength = 350;
	private static final double gridWidthBuffer = 25;
	private static final double gridHeightBuffer = 30;
	
	public TriangleGrid(Group root, int width, int height) {
		super(root,width,height);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Grid#setGrid(int, int)
	 */
	public void setGrid(int height, int width) {
		myCells = new Cell[height][width];
		double cellWidth = gridSideLength/(0.5+(width*0.5));
		double cellHeight = gridSideLength/height;
		Double[] coords = new Double[]{
				0.0, gridHeightBuffer-cellHeight,
				0.0, gridHeightBuffer,
				0.0, gridHeightBuffer-cellHeight};
		
		for (int i=0; i<height; i++) {
			coords = getNextRowStartingCoords(new Double[]{coords[1], coords[3], coords[5]}, cellWidth, cellHeight, i, (width % 2 == 1));
			for (int j=0; j<width; j++) {
				Shape cellShape = getTriangle(coords);
				myCells[i][j] = new Cell(cellShape);
				coords = new Double[]{
						coords[0]+cellWidth/2, coords[3],
						coords[2]+cellWidth/2, coords[5],
						coords[4]+cellWidth/2, coords[3]};
			}
		}
	}
	
	private Double[] getNextRowStartingCoords(Double[] yCoords, double cellWidth, double cellHeight, int row, boolean oddWidth) {
		if (oddWidth) {
			yCoords[0] += cellHeight;
			yCoords[1] += cellHeight;
			yCoords[2] += cellHeight;
		}
		else {
			if (row % 2 == 1) {
				yCoords[1] += 2*cellHeight;
			}
			else {
				yCoords[0] += 2*cellHeight;
				yCoords[2] += 2*cellHeight;
			}				
		}
		return new Double[]{gridWidthBuffer, yCoords[0],
				gridWidthBuffer+cellWidth/2, yCoords[1],
				gridWidthBuffer+cellWidth, yCoords[2]};
	}

	
	/*
	 * @return a Rectangle placed at the given location and of the 
	 * given width and height
	 * Default fill is white with a black border
	 */
	private Polygon getTriangle(Double[] coords) {
		Polygon triangle = new Polygon();
		triangle.getPoints().addAll(coords);
		
		triangle.setFill(Color.WHITE);
		triangle.setStroke(Color.BLACK);
		triangle.setStrokeType(StrokeType.INSIDE);
		triangle.setStrokeWidth(1);
		
		return triangle;
	}		
}
