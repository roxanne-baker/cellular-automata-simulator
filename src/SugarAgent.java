import java.util.Random;
import java.util.function.Function;

public class SugarAgent {

	int initialSugar;
	int sugar;
	int sugarMetabolism;
	int vision;
	SugarScapeCell currentLoc;
	Border myBorder;
	SugarScapeSimulation simulation;
	
	public SugarAgent() {
		Random startingVals = new Random();
		initialSugar = startingVals.nextInt(21)+5;
		sugar = initialSugar;
		sugarMetabolism = startingVals.nextInt(4)+1;
		vision = startingVals.nextInt(6)+1;
	}
	
	public SugarAgent(SugarScapeSimulation sim, Border border) {
		simulation = sim;
		myBorder = border;
	}
	
	public SugarAgent(Border border) {
		this();
		myBorder = border;
	}
	
	
	public SugarAgent(Border border, int initSugar, int metabolism, int visionRange) {
		myBorder = border;
		
		initialSugar = initSugar;
		sugar = initialSugar;
		sugarMetabolism = metabolism;
		vision = visionRange;
	}
	
	
	
	public void killAgentIfNeeded() {
		if (sugar <= 0) {
			currentLoc.agent = null;
		}
	}
	
	public void updateSugar() {
		SugarScapeCell nextLoc = getNextLoc();
		currentLoc.agent = null;
		
		currentLoc = nextLoc;
		currentLoc.agent = this;
		
		sugar += currentLoc.sugarAmount;
		sugar -= sugarMetabolism;
		currentLoc.sugarAmount = 0;
	}

	public SugarScapeCell getLoc(SugarScapeCell lookingAtLoc, SugarScapeCell maxSugarLoc,
			Function<SugarScapeCell, SugarScapeCell> getNeighbor) {
		for (int i=0; i<vision; i++) {
			lookingAtLoc = getNeighbor.apply(lookingAtLoc);
			if (lookingAtLoc.sugarAmount > maxSugarLoc.sugarAmount) {
				maxSugarLoc = lookingAtLoc;
			}
			else if (lookingAtLoc.sugarAmount == maxSugarLoc.sugarAmount &&
					distanceFrom(lookingAtLoc) < distanceFrom(maxSugarLoc)) {
				maxSugarLoc = lookingAtLoc;
			}
		}
		return maxSugarLoc;		
	}
	
	public SugarScapeCell getNextLoc() {
		SugarScapeCell maxSugarLoc = currentLoc.neighbors.get(0);
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getNorthLoc(cell)));	
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getEastLoc(cell)));	
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getSouthLoc(cell)));
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getWestLoc(cell)));	
		return maxSugarLoc;
	}
	
	public int distanceFrom(SugarScapeCell cell) {
		return (Math.max(Math.abs(currentLoc.row-cell.row), Math.abs(currentLoc.col-cell.col)));
	}
	
	public SugarScapeCell getNorthLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getNorthNeighbor(simulation.getMyCells(), cell.row, cell.col));
	}
	
	public SugarScapeCell getSouthLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getSouthNeighbor(simulation.getMyCells(), cell.row, cell.col));
	}
	
	public SugarScapeCell getEastLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getRightNeighbor(simulation.getMyCells(), cell.row, cell.col));
	}
	
	public SugarScapeCell getWestLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getLeftNeighbor(simulation.getMyCells(), cell.row, cell.col));
	}
	
	public int getSugarMetabolism() {
		return sugarMetabolism;
	}

	public int getVision() {
		return vision;
	}

	
}
