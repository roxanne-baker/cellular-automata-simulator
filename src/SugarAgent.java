import java.util.Random;
import java.util.function.Function;

public abstract class SugarAgent {

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
	
	
	
	public abstract void killAgentIfNeeded();

	
	public void updateSugar() {
		SugarScapeCell nextLoc = getNextLoc();
		currentLoc.setAgent(null);
		
		currentLoc = nextLoc;
		currentLoc.setAgent(this);
		
		sugar += currentLoc.getSugarAmount();
		sugar -= sugarMetabolism;
		currentLoc.setSugarAmount(0);
	}

	public SugarScapeCell getLoc(SugarScapeCell lookingAtLoc, SugarScapeCell maxSugarLoc,
			Function<SugarScapeCell, SugarScapeCell> getNeighbor) {
		for (int i=0; i<vision; i++) {
			lookingAtLoc = getNeighbor.apply(lookingAtLoc);
			if (lookingAtLoc.getSugarAmount() > maxSugarLoc.getSugarAmount()) {
				maxSugarLoc = lookingAtLoc;
			}
			else if (lookingAtLoc.getSugarAmount() == maxSugarLoc.getSugarAmount() &&
					distanceFrom(lookingAtLoc) < distanceFrom(maxSugarLoc)) {
				maxSugarLoc = lookingAtLoc;
			}
		}
		return maxSugarLoc;		
	}
	
	public SugarScapeCell getNextLoc() {
		SugarScapeCell maxSugarLoc = currentLoc.getNeighbors().get(0);
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getNorthLoc(cell)));	
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getEastLoc(cell)));	
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getSouthLoc(cell)));
		maxSugarLoc = getLoc(maxSugarLoc, maxSugarLoc, (SugarScapeCell cell) -> (getWestLoc(cell)));	
		return maxSugarLoc;
	}
	
	public int distanceFrom(SugarScapeCell cell) {
		return (Math.max(Math.abs(currentLoc.getRow()-cell.getRow()), Math.abs(currentLoc.getCol()-cell.getCol())));
	}
	
	public SugarScapeCell getNorthLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getNorthNeighbor(simulation.getMyCells(), cell.getRow(), cell.getCol()));
	}
	
	public SugarScapeCell getSouthLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getSouthNeighbor(simulation.getMyCells(), cell.getRow(), cell.getCol()));
	}
	
	public SugarScapeCell getEastLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getRightNeighbor(simulation.getMyCells(), cell.getRow(), cell.getCol()));
	}
	
	public SugarScapeCell getWestLoc(SugarScapeCell cell) {
		return (SugarScapeCell) (myBorder.getLeftNeighbor(simulation.getMyCells(), cell.getRow(), cell.getCol()));
	}
	
	public int getSugarMetabolism() {
		return sugarMetabolism;
	}

	public int getVision() {
		return vision;
	}

	
}
