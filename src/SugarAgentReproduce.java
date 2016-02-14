import java.util.Random;
import java.util.function.Function;

public class SugarAgentReproduce extends SugarAgent {
	
	int isMale;		// 0 if untrue, 1 if true
	int age = 0;
	int maxAge;
	int fertileLowerLimit = 15;
	int fertileUpperLimit = 70;
	SugarScapeReproduceSimulation simulation;
	
	public SugarAgentReproduce(SugarScapeReproduceSimulation sim, Border border) {
		super(border);
		simulation = sim;
		initializeMaxAgeAndSex();
	}
	
	public SugarAgentReproduce(SugarScapeReproduceSimulation sim, Border border, int initSugar, int metabolism, int visionRange) {
		super(border, initSugar, metabolism, visionRange);
		simulation = sim;
		initializeMaxAgeAndSex();
	}
	
	public void initializeMaxAgeAndSex() {
		Random initValues = new Random();
		maxAge = initValues.nextInt(40)+60;
		isMale = initValues.nextInt(2);
	}
	
	public void incrementAge() {
		age++;
	}
	
	public SugarAgentReproduce getBabyAgent(SugarAgentReproduce otherParent) {
		int babyInitSugar = this.initialSugar/2 + otherParent.initialSugar/2;
		int babyMetabolism = getBabyStat(otherParent, (SugarAgentReproduce agent) -> getAgentMetabolism(agent));
		int babyVision = getBabyStat(otherParent, (SugarAgentReproduce agent) -> getAgentVision(agent));

		return new SugarAgentReproduce(simulation, myBorder, babyInitSugar, babyMetabolism, babyVision);
	}
	
	public void reproduce() {
		SugarScapeCell emptyCell = null;
		for (SugarScapeCell neighbor : currentLoc.neighbors) {
			if (neighbor.agent != null) {
				SugarAgentReproduce potentialMate = (SugarAgentReproduce) neighbor.agent;
				if (potentialMate.isFertile() && (this.isMale != potentialMate.isMale)) {
					emptyCell = getEmptyCell(potentialMate);
				}
				if (emptyCell != null) {
					emptyCell.agent = getBabyAgent(potentialMate);
					this.sugar -= (initialSugar/2);
					potentialMate.sugar -= (initialSugar/2);
				}
			}
		}
	}
	
	public SugarScapeCell getEmptyCellFromLoc(SugarScapeCell cell) {
		SugarScapeCell emptyCell = null;
		for (SugarScapeCell neighbor : cell.neighbors) {
			if (neighbor.agent == null) {
				return emptyCell;
			}
		}
		return null;
	}
	
	public SugarScapeCell getEmptyCell(SugarAgentReproduce mate) {
		SugarScapeCell emptyCell = null;
		emptyCell = getEmptyCellFromLoc(currentLoc);
		if (emptyCell == null) {
			emptyCell = getEmptyCellFromLoc(mate.currentLoc);
		}
		return emptyCell;
	}
	
	public int getBabyStat(SugarAgentReproduce otherParent, Function<SugarAgentReproduce, Integer> getStat) {
		Random babyStats = new Random();
		int chooseStat = babyStats.nextInt(4);
		if (chooseStat == 0) {
			return getStat.apply(this);
		}
		else if (chooseStat == 3) {
			return getStat.apply(otherParent);
		}
		else {
			return (int) (getStat.apply(this)*0.5 + getStat.apply(otherParent)*0.5);
		}		
	}
	
	public int getAgentMetabolism(SugarAgentReproduce agent) {
		return agent.getSugarMetabolism();
	}
	
	public int getAgentVision(SugarAgentReproduce agent) {
		return agent.getVision();
	}
	
	public boolean isFertile() {
		return (sugar >= initialSugar && age >= fertileLowerLimit && age < fertileUpperLimit);
	}
	
	@Override
	public void killAgentIfNeeded() {
		if (sugar <= 0 || age >= maxAge) {
			currentLoc.agent = null;
		}
	}
	
	public void setIsMale(boolean isMale) {
		if (isMale) {
			this.isMale = 0;
		}
		else {
			this.isMale = 1;
		}
	}
	
}
