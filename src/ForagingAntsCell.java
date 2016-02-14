import java.util.ArrayList;
import java.util.List;

import javafx.scene.shape.Shape;

public class ForagingAntsCell extends Cell{

	private boolean isFood = false;
	private boolean isNest = false;
	private boolean isObstacle = false;
	private List<Ant> ants;
	private double homePheromones;
	private double foodPheromones;
	

	ForagingAntsSimulation simulation;
	final int[] position;
	
	public ForagingAntsCell(Shape shape, ForagingAntsSimulation simulation, int i, int j) {
		super(shape);
		this.simulation = simulation;
		position = new int[]{i, j};
	}
	
	public double getFoodPheromones() {
		return foodPheromones;
	}

	public void setFoodPheromones(double foodPheromones) {
		this.foodPheromones = foodPheromones;
	}
	
	public double getHomePheromones() {
		return homePheromones;
	}

	public void setHomePheromones(double homePheromones) {
		this.homePheromones = homePheromones;
	}
	
	public List<ForagingAntsCell> getNeighbors(List<Cell> neighbors) {
		List<ForagingAntsCell> foragingAntsNeighbours = new ArrayList<ForagingAntsCell>();
		for (Cell neighbour : neighbors) {
			foragingAntsNeighbours.add((ForagingAntsCell) neighbour);				
		}		
		return foragingAntsNeighbours;
	}

	public boolean isFood() {
		return isFood;
	}

	public void setFood(boolean isFood) {
		this.isFood = isFood;
	}

	public boolean isNest() {
		return isNest;
	}

	public void setNest(boolean isNest) {
		this.isNest = isNest;
	}

	public boolean isObstacle() {
		return isObstacle;
	}

	public void setObstacle(boolean isObstacle) {
		this.isObstacle = isObstacle;
	}

	public List<Ant> getAnts() {
		return ants;
	}

	public void setAnts(List<Ant> ants) {
		this.ants = ants;
	}
	
}
