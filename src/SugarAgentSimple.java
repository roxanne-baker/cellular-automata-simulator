
import java.util.function.Function;

public class SugarAgentSimple extends SugarAgent {
	
	
	public SugarAgentSimple(SugarScapeSimulation sim, Border border) {
		super(border);
		simulation = sim;
	}
	
	public SugarAgentSimple(SugarScapeSimulation sim, Border border, int initSugar, int metabolism, int visionRange) {
		super(border, initSugar, metabolism, visionRange);
		simulation = sim;
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
