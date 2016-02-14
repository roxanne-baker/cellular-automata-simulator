import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;

public class Ant {

	Map<String, List<ForagingAntsCell>> orientationToNeighbors;
	private boolean hasFood = false;
	private int turnsAlive = 0;
	private List<ForagingAntsCell> allNeighbors;
	private List<ForagingAntsCell> forwardNeighbors;
	private String orientation = "";
	private Border border;
	private ForagingAntsCell currentLoc;
	private ForagingAntsCell prevLoc = null;
	private ForagingAntsSimulation simulation;
	
	
	public Ant(ForagingAntsSimulation simulation, ForagingAntsCell startingLoc, Border border) {
		this.simulation = simulation;
		currentLoc = startingLoc;
		this.border = border;
	}
	
	
	public void forage() {
		if (this.hasFood) {
			returnToNest();
		}
		else {
			findFoodSource();
		}
		if (prevLoc != null) {
			prevLoc.getAnts().remove(this);
		}
		turnsAlive++;
	}

	
	private void returnToNest() {
		if (currentLoc.isFood()) {
			turnTowardsFoodOrHome(getMaxPheromones((ForagingAntsCell cell) -> getPheromonesHomeFunc(cell)));
		}
		ForagingAntsCell nextLoc = null;	
		if (forwardNeighbors.isEmpty()) {
			nextLoc = findNeighbor(forwardNeighbors, ((ForagingAntsCell cell) -> getPheromonesHomeFunc(cell)));
		}
		else {
			nextLoc = findNeighbor(allNeighbors, ((ForagingAntsCell cell) -> getPheromonesHomeFunc(cell)));		
		}
		if (nextLoc != null) {
			dropPheromones(this.currentLoc.isFood(), (Double maxPheromones) -> currentLoc.setFoodPheromones(maxPheromones),
					(ForagingAntsCell cell) -> getPheromonesFoodFunc(cell));
			moveToLoc(nextLoc);
			if (this.currentLoc.isNest()) {
				this.hasFood = false;
			}
		}
	}

		
	private void findFoodSource() {
		if (currentLoc.isNest()) {
			turnTowardsFoodOrHome(getMaxPheromones((ForagingAntsCell cell) -> getPheromonesFoodFunc(cell)));			
		}
		ForagingAntsCell nextLoc = null;	
		if (forwardNeighbors.isEmpty()) {
			nextLoc = selectLocation(forwardNeighbors);
		}
		if (nextLoc == null) {
			nextLoc = selectLocation(allNeighbors);
		}	
		if (nextLoc != null) {
			dropPheromones(this.currentLoc.isFood(), (Double maxPheromones) -> currentLoc.setFoodPheromones(maxPheromones),
					(ForagingAntsCell cell) -> getPheromonesHomeFunc(cell));
			
			moveToLoc(nextLoc);
			if (this.currentLoc.isFood()) {
				this.hasFood = true;
			}
		}
	}
	
	private void moveToLoc(ForagingAntsCell nextCell) {
		prevLoc = currentLoc;
		currentLoc = nextCell;
		mapOrientationToForwardNeighbors();
		forwardNeighbors = orientationToNeighbors.get(currentLoc);
		allNeighbors = currentLoc.getNeighbors(currentLoc.neighbours);
		currentLoc.getAnts().add(this);
	}
	
	
	private ForagingAntsCell selectLocation(List<ForagingAntsCell> neighbors) {
		List<ForagingAntsCell> availableCells = new ArrayList<ForagingAntsCell>();
		for (ForagingAntsCell neighbor : neighbors) {
			if (!neighbor.isObstacle() &&
					neighbor.getAnts().size() < simulation.getMaxAntsPerLocation()) {
				availableCells.add(neighbor);
			}
		}
		if (availableCells.isEmpty()) {
			return null;
		}
		else {
			return getNextLocFromAvailableCells(availableCells);
		}
	}
	
	private ForagingAntsCell getNextLocFromAvailableCells(List<ForagingAntsCell> availableCells) {
		Map<ForagingAntsCell, double[]> valueRangeMap = getValueRangeMap(availableCells);
		int maxValue = getMaxValue(availableCells);
		Random chooseLoc = new Random();
		int valueChosen = chooseLoc.nextInt(maxValue);
		for (ForagingAntsCell loc : valueRangeMap.keySet()) {
			if (valueChosen >= valueRangeMap.get(loc)[0] && valueChosen < valueRangeMap.get(loc)[1]) {
				return loc;
			}
		}
		return null;		
	}
	
	private Map<ForagingAntsCell, double[]> getValueRangeMap(List<ForagingAntsCell> availableCells) {
		Map<ForagingAntsCell, double[]> cellToValueRange = new HashMap<ForagingAntsCell, double[]>();
		double currLimit = 0;
		for (ForagingAntsCell availableCell : availableCells) {
			cellToValueRange.put(availableCell, new double[]{currLimit, currLimit+availableCell.getFoodPheromones()});
			currLimit += availableCell.getFoodPheromones();
		}
		return cellToValueRange;
	}
	
	private int getMaxValue(List<ForagingAntsCell> availableCells) {
		double totalFoodPheromones = 0;
		for (ForagingAntsCell availableCell : availableCells) {
			totalFoodPheromones += availableCell.getFoodPheromones();
		}
		return (int) totalFoodPheromones;
	}
	
	private double getPheromonesFoodFunc(ForagingAntsCell cell) {
		return cell.getFoodPheromones();
	}
	
	private double getPheromonesHomeFunc(ForagingAntsCell cell) {
		return cell.getHomePheromones();
	}

	private void dropPheromones(boolean nestOrFood, Consumer<Double> setPheromones, Function<ForagingAntsCell, Double> getPheromones) {
		if (nestOrFood) {
			setPheromones.accept(simulation.getMaxPheromone());
		}
		else {
			double pheromonesToDrop = 0;
			for (ForagingAntsCell neighbor : allNeighbors) {
				if (getPheromones.apply(neighbor) > pheromonesToDrop) {
					pheromonesToDrop = getPheromones.apply(neighbor) - 2 - getPheromones.apply(currentLoc);
				}
			}
			if (pheromonesToDrop > 0) {
				double totalPheromones = Math.min(simulation.getMaxPheromone(), getPheromones.apply(currentLoc) + pheromonesToDrop);
				setPheromones.accept(totalPheromones);
			}
		}		
	}
	
	
	private ForagingAntsCell getMaxPheromones(Function<ForagingAntsCell, Double> getPheromones) {
		ForagingAntsCell maxPheromoneNeighbor = null;
		for (ForagingAntsCell neighbor : allNeighbors) {
			if (maxPheromoneNeighbor == null) {
				maxPheromoneNeighbor = neighbor;
			}
			else {
				if (getPheromones.apply(neighbor) > getPheromones.apply(maxPheromoneNeighbor)) {
					maxPheromoneNeighbor = neighbor;
				}
			}
		}		
		return maxPheromoneNeighbor;		
	}
	
	private void turnTowardsFoodOrHome(ForagingAntsCell maxPheromoneNeighbor) {
		int[] positionChange = new int[]{
				maxPheromoneNeighbor.position[0] - currentLoc.position[0],
				maxPheromoneNeighbor.position[1] - currentLoc.position[1]};
		for (int i=0; i<2; i++) {
			if (positionChange[i] > 1) {
				positionChange[i] = -1;
			}
			else if (positionChange[i] < 1) {
				positionChange[i] = 1;
			}
		}
		setOrientation(positionChange);
		List<ForagingAntsCell> orientationNeighbors = orientationToNeighbors.get(orientation);
		for (ForagingAntsCell neighbor : orientationNeighbors) {
			if (neighbor.isObstacle() || neighbor.getAnts().size() >= simulation.getMaxAntsPerLocation()) {
				forwardNeighbors.add(neighbor);
			}
		}		
	}	
	
	
	private ForagingAntsCell findNeighbor(List<ForagingAntsCell> neighbors, Function<ForagingAntsCell, Double> getPheromones) {
		ForagingAntsCell nextLoc = null;
		for (ForagingAntsCell neighbor : neighbors) {
			if (nextLoc == null) {
				nextLoc = neighbor;
			}
			else if (getPheromones.apply(neighbor) > getPheromones.apply(nextLoc)) {
				nextLoc = neighbor;
			}
		}
		return nextLoc;
	}
	
	public void killAntIfNeeded() {
		if (turnsAlive > simulation.getAntLifespan()) {
			simulation.getAllAnts().remove(this);
		}
	}
	
	
	private void setOrientation(int[] position) {
		String verticalString = "";
		String horizontalString = "";
		if (position[0] > 0) {
			verticalString = "S";
		}
		else if (position[0] < 0) {
			verticalString = "N";
		}
		if (position[1] > 0) {
			horizontalString = "E";
		}
		else if (position[1] < 0) {
			horizontalString = "W";
		}		
		orientation = verticalString+horizontalString;
	}
	
	private List<ForagingAntsCell> getNeighbors(Consumer<List<Cell>> direction1, Consumer<List<Cell>> direction2, Consumer<List<Cell>> direction3) {
		List<Cell> neighbors = new ArrayList<Cell>();
		direction1.accept(neighbors);
		direction2.accept(neighbors);
		direction3.accept(neighbors);
		return currentLoc.getNeighbors(neighbors);
	}
	
	
	private List<ForagingAntsCell> getNorthNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborNW(neighbors), (List<Cell> neighbors) -> addNeighborN(neighbors),
				(List<Cell> neighbors) -> addNeighborNE(neighbors));
	}
	
	private List<ForagingAntsCell> getSouthNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborSW(neighbors), (List<Cell> neighbors) -> addNeighborS(neighbors),
				(List<Cell> neighbors) -> addNeighborSE(neighbors));
	}
	
	private List<ForagingAntsCell> getLeftNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborW(neighbors), (List<Cell> neighbors) -> addNeighborNW(neighbors),
				(List<Cell> neighbors) -> addNeighborSW(neighbors));
	}
	
	private List<ForagingAntsCell> getRightNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborNE(neighbors), (List<Cell> neighbors) -> addNeighborE(neighbors),
				(List<Cell> neighbors) -> addNeighborSE(neighbors));
	}
	
	private List<ForagingAntsCell> getUpperLeftNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborNW(neighbors), (List<Cell> neighbors) -> addNeighborN(neighbors),
				(List<Cell> neighbors) -> addNeighborW(neighbors));
	}
	
	private List<ForagingAntsCell> getUpperRightNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborNE(neighbors), (List<Cell> neighbors) -> addNeighborN(neighbors),
				(List<Cell> neighbors) -> addNeighborE(neighbors));
	}
	
	private List<ForagingAntsCell> getLowerLeftNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborW(neighbors), (List<Cell> neighbors) -> addNeighborSW(neighbors),
				(List<Cell> neighbors) -> addNeighborS(neighbors));
	}
	
	public List<ForagingAntsCell> getLowerRightNeighbors() {
		return getNeighbors((List<Cell> neighbors) -> addNeighborE(neighbors), (List<Cell> neighbors) -> addNeighborSE(neighbors),
				(List<Cell> neighbors) -> addNeighborS(neighbors));
	}
	
	
	private void addNeighborNW(List<Cell> neighbors) {
		neighbors.add(border.getUpperLeftNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void addNeighborN(List<Cell> neighbors) {
		neighbors.add(border.getNorthNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void addNeighborNE(List<Cell> neighbors) {
		neighbors.add(border.getUpperRightNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void addNeighborE(List<Cell> neighbors) {
		neighbors.add(border.getRightNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void addNeighborSE(List<Cell> neighbors) {
		neighbors.add(border.getLowerRightNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void addNeighborS(List<Cell> neighbors) {
		neighbors.add(border.getSouthNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void addNeighborSW(List<Cell> neighbors) {
		neighbors.add(border.getLowerLeftNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void addNeighborW(List<Cell> neighbors) {
		neighbors.add(border.getLeftNeighbor(simulation.getMyCells(), currentLoc.position[0], currentLoc.position[1]));
	}
	
	private void mapOrientationToForwardNeighbors() {
		orientationToNeighbors = new HashMap<String, List<ForagingAntsCell>>();
		orientationToNeighbors.put("N", getNorthNeighbors());
		orientationToNeighbors.put("NE", getUpperRightNeighbors());
		orientationToNeighbors.put("E", getRightNeighbors());
		orientationToNeighbors.put("SE", getLowerRightNeighbors());
		orientationToNeighbors.put("S", getSouthNeighbors());
		orientationToNeighbors.put("SW", getLowerLeftNeighbors());
		orientationToNeighbors.put("W", getLeftNeighbors());
		orientationToNeighbors.put("NW", getUpperLeftNeighbors());
	}	
}
