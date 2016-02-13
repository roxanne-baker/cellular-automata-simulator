import java.util.List;

import javafx.scene.shape.Shape;

public class SugarScapeCell extends Cell{

	double sugarAmount;
	double sugarMax;
	int sugarGrowBackRate;
	int sugarGrowBackInterval = 0;
	List<SugarScapeCell> neighbors;
	SugarAgent agent;
	
	int row;
	int col;
	
	public SugarScapeCell(Shape shape, int row, int col, int sugarGrowBackRate) {
		super(shape);
		this.sugarGrowBackRate = sugarGrowBackRate;
		this.row = row;
		this.col = col;
	}
	
	public void update() {
		sugarGrowBackInterval++;
		if (sugarGrowBackInterval >= sugarGrowBackRate) {
			sugarAmount = Math.min(sugarAmount+sugarGrowBackRate, sugarMax);
			sugarGrowBackInterval = 0;
		}
	}
	

	
	
	
}
