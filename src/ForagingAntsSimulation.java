import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

import javafx.scene.paint.Color;

public class ForagingAntsSimulation extends Simulation {

	private ForagingAntsCell[][] myCells;
	private Border myBorder;
	private int maxAntsTotal;
	private int maxAntsPerLocation;
	private int antLifespan;

	private int antsPerStep = 2;
	private double evapRatio = 0.01;
	private double diffusionRatio = 0.01;
	
	private double maxPheromone;
	private List<Ant> allAnts;


	public void ForagingAntsSimulation(Cell[][] grid, Border border) {
		setMyCells(grid);
		myBorder = border;
		setAllAnts(new ArrayList<Ant>());
		initiateAnts();
	}
	
	
	private void setMyCells(Cell[][] newCells) {
		myCells = new ForagingAntsCell[newCells.length][newCells[0].length];
		for (int i=0; i<newCells.length; i++) {
			for (int j=0; j<newCells[0].length; j++) {
				myCells[i][j] = new ForagingAntsCell(newCells[i][j].shape, this, i, j);
				myCells[i][j].setState(newCells[i][j].getState());
			}
		}
	}
	
	private void initiateAnts() {
		if (getAllAnts().size() < maxAntsTotal) {
			for (int i=0; i<myCells.length; i++) {
				for (int j=0; j<myCells[0].length; j++) {
					for (int k=0; k<antsPerStep; k++) {
						if (myCells[i][j].isNest() && myCells[i][j].getAnts().size() < maxAntsPerLocation) {
							getAllAnts().add(new Ant(this, myCells[i][j], myBorder));
						}
						else {
							break;
						}
					}
				}
			}				
		}	
	}
	
	
	public void update() {
		updateAnts((Ant ant) -> callForageOnAnt(ant));
		updateAnts((Ant ant) -> callKillAntIfNeededOnAnt(ant));
		updatePheromones((ForagingAntsCell cell) -> getCellFoodPheromones(cell),
				(ForagingAntsCell cell, Double pheromoneAmount) -> setCellFoodPheromones(cell, pheromoneAmount));
		updatePheromones((ForagingAntsCell cell) -> getCellHomePheromones(cell),
				(ForagingAntsCell cell, Double pheromoneAmount) -> setCellHomePheromones(cell, pheromoneAmount));
		
		initiateAnts();
	}
	
	
	private void updateAnts(Consumer<Ant> antMethod) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				for (Ant ant : getAllAnts()) {
					antMethod.accept(ant);
				}
			}
		}		
	}
	
	private void updatePheromones(Function<ForagingAntsCell, Double> getPheromones, BiConsumer<ForagingAntsCell, Double> setPheromones) {
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				double currFoodPheromone = getPheromones.apply(myCells[i][j]);
				double evapFoodAmount = currFoodPheromone*evapRatio;
				double diffuseFoodAmount = currFoodPheromone*diffusionRatio;
				for (ForagingAntsCell neighbor : myCells[i][j].getNeighbors(myCells[i][j].getMyNeighbours())) {
					if (!neighbor.isObstacle()) {
						setPheromones.accept(neighbor, Math.min(getPheromones.apply(neighbor)+diffuseFoodAmount, maxPheromone));
					}					
				}
				currFoodPheromone -= evapFoodAmount - diffuseFoodAmount*(myCells[i][j].getMyNeighbours().size());
				setPheromones.accept(myCells[i][j], Math.max(0.0,currFoodPheromone));
			}
		}		
	}
	
	private void callForageOnAnt(Ant ant) {
		ant.forage();
	}
	
	private void callKillAntIfNeededOnAnt(Ant ant) {
		ant.killAntIfNeeded();
	}
	
	private double getCellFoodPheromones(ForagingAntsCell cell) {
		return cell.getFoodPheromones();
	}
	
	private double getCellHomePheromones(ForagingAntsCell cell) {
		return cell.getHomePheromones();
	}

	private void setCellFoodPheromones(ForagingAntsCell cell, Double pheromoneAmount) {
		cell.setFoodPheromones(pheromoneAmount);
	}
	
	private void setCellHomePheromones(ForagingAntsCell cell, Double pheromoneAmount) {
		cell.setHomePheromones(pheromoneAmount);
	}
	
	public ForagingAntsCell[][] getMyCells() {
		return myCells;
	}

	public void setMyCells(ForagingAntsCell[][] myCells) {
		this.myCells = myCells;
	}
	
	public int getMaxAntsPerLocation() {
		return maxAntsPerLocation;
	}


	public void setMaxAntsPerLocation(int maxAntsPerLocation) {
		this.maxAntsPerLocation = maxAntsPerLocation;
	}
	
	public double getMaxPheromone() {
		return maxPheromone;
	}


	public void setMaxPheromone(double maxPheromone) {
		this.maxPheromone = maxPheromone;
	}
	
	public List<Ant> getAllAnts() {
		return allAnts;
	}


	public void setAllAnts(List<Ant> allAnts) {
		this.allAnts = allAnts;
	}


	@Override
	public String returnStyleSheet() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public HashMap<Color, Number> returnProportion() {
		// TODO Auto-generated method stub
		return null;
	}


	public int getAntLifespan() {
		return antLifespan;
	}


	public void setAntLifespan(int antLifespan) {
		this.antLifespan = antLifespan;
	}
	
	
	
}
