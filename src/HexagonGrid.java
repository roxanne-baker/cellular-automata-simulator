import java.util.Arrays;

import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Polygon;
import javafx.scene.shape.Shape;
import javafx.scene.shape.StrokeType;

public class HexagonGrid extends Grid {

	/*
	 * @author Roxanne Baker
	 * Creates a grid made up of squares
	 * based on the width and height specified
	 */
	private static final double gridSideLength = 350;
	private static final double gridWidthBuffer = 25;
	private static final double gridHeightBuffer = 30;
	
	public HexagonGrid(Group root, int width, int height) {
		super(root,width,height);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Grid#setGrid(int, int)
	 */
	public void setGrid(int height, int width) {
		myCells = new Cell[height][width];
		double cellWidth = gridSideLength/(width+0.5);
		double cellHeight = gridSideLength/(height*0.75+0.25);
		
		Double[] coords = new Double[]{
				gridWidthBuffer-cellWidth, 0.0,
				gridWidthBuffer-cellWidth/2, 0.0,
				gridWidthBuffer, 0.0,
				gridWidthBuffer, 0.0,
				gridWidthBuffer-cellWidth/2, 0.0,
				gridWidthBuffer-cellWidth, 0.0};
		
		for (int i=0; i<height; i++) {
			coords = getNextRowStartingCoords(cellWidth, cellHeight, i);
			for (int j=0; j<width; j++) {
				Shape cellShape = getHexagon(coords);
				myCells[i][j] = new Cell(cellShape);
				for (int k=0; k<coords.length; k += 2) {
					coords[k] += cellWidth;
				}
			}
		}
	}
	
	private Double[] getNextRowStartingCoords(double cellWidth, double cellHeight, int rowInt) {
		double row = (double) rowInt;
		double[] coords = new double[]{
				gridWidthBuffer, gridHeightBuffer+row*(cellHeight*0.75),
				gridWidthBuffer, gridHeightBuffer+row*(cellHeight*0.75),
				gridWidthBuffer, gridHeightBuffer+row*(cellHeight*0.75),
				gridWidthBuffer, gridHeightBuffer+row*(cellHeight*0.75),
				gridWidthBuffer, gridHeightBuffer+row*(cellHeight*0.75),
				gridWidthBuffer, gridHeightBuffer+row*(cellHeight*0.75)};
		
		if (row % 2 == 0) {
			coords[0] += 0.0;
			coords[2] += cellWidth/2;
			coords[4] += cellWidth;
			coords[6] += cellWidth;
			coords[8] += cellWidth/2;
			coords[10] += 0.0;
		}
		else {
			coords[0] += cellWidth/2;
			coords[2] += cellWidth;
			coords[4] += cellWidth*(1.5);
			coords[6] += cellWidth*(1.5);
			coords[8] += cellWidth;
			coords[10] += cellWidth/2;			
		}
		
		return new Double[]{
				coords[0], coords[1]+cellHeight*0.25,
				coords[2], coords[3]+0.0,
				coords[4], coords[5]+cellHeight*0.25,
				coords[6], coords[7]+cellHeight*0.75,
				coords[8], coords[9]+cellHeight,
				coords[10], coords[11]+cellHeight*0.75};
	}

	
	/*
	 * @return a Rectangle placed at the given location and of the 
	 * given width and height
	 * Default fill is white with a black border
	 */
	private Polygon getHexagon(Double[] coords) {
		System.out.println(Arrays.toString(coords));
		Polygon hexagon = new Polygon();
		hexagon.getPoints().addAll(coords);
		
		hexagon.setFill(Color.WHITE);
		hexagon.setStroke(Color.BLACK);
		hexagon.setStrokeType(StrokeType.INSIDE);
		hexagon.setStrokeWidth(1);
		
		return hexagon;
	}		
}
