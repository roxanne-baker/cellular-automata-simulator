import javafx.*;
import javafx.scene.Group;

public abstract class Grid {

	public Cell[][] myGrid;
	
	public Grid(Group root, int width, int height) {
		setGrid(width, height);
		showGrid(root, width, height);
	}
	
	public abstract void setGrid(int width, int height);
	
	public void showGrid(Group root, int width, int height) {
		for (int i=0;i<width; i++) {
			for (int j=0; j<height; j++) {
				root.getChildren().add(myGrid[i][j].shape);
			}
		}
	}
}
