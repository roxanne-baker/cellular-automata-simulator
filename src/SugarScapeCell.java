import java.util.List;

import javafx.scene.shape.Shape;

public class SugarScapeCell extends Cell{

	private double sugarAmount;
	private double sugarMax;
	private int sugarGrowBackRate;
	private int sugarGrowBackInterval = 0;
	private List<SugarScapeCell> neighbors;
	private SugarAgent agent;
	
	private int row;
	private int col;

	public SugarScapeCell(Shape shape, int row, int col, int sugarMax, int sugarGrowBackRate) {
		super(shape);
		this.sugarMax = sugarMax;
		setSugarAmount(sugarMax);
		this.sugarGrowBackRate = sugarGrowBackRate;
		this.row = row;
		this.col = col;
	}
	
	public void update() {
		sugarGrowBackInterval++;
		if (sugarGrowBackInterval >= sugarGrowBackRate) {
			setSugarAmount(Math.min(getSugarAmount()+sugarGrowBackRate, sugarMax));
			sugarGrowBackInterval = 0;
		}
	}

	public SugarAgent getAgent() {
		return agent;
	}

	public void setAgent(SugarAgent agent) {
		this.agent = agent;
	}

	public List<SugarScapeCell> getNeighbors() {
		return neighbors;
	}
	
	public double getSugarRatio() {
		return getSugarAmount()/sugarMax;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public double getSugarAmount() {
		return sugarAmount;
	}

	public void setSugarAmount(double sugarAmount) {
		this.sugarAmount = sugarAmount;
	}
	
	
	
}
