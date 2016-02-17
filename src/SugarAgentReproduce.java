import java.util.Random;
import java.util.function.Function;

public class SugarAgentReproduce extends SugarAgent {
	
	/**
	 * true if the agent is male and false otherwise
	 */
	private boolean isMale;	
	
	/**
	 * The current 'age' of the agent
	 */
	private int age = 0;
	
	/**
	 * The maximum age the agent can be before dying
	 */
	private int maxAge;
	
	/**
	 * The lower age limit for an agent to be fertile
	 */
	private int fertileLowerLimit;
	
	/**
	 * The upper age limit for an agent to be fertile
	 */
	private int fertileUpperLimit;
	
	/**
	 * The simulation that the agent is a part of
	 */
	private SugarScapeReproduceSimulation simulation;
	
	/**
	 * Creates an agent given the simulation, border and
	 * lower/upper limits for fertility.
	 * Other traits of agent are randomized in super class
	 * @param sim
	 * @param border
	 * @param lowerLimit
	 * @param upperLimit
	 */
	public SugarAgentReproduce(SugarScapeReproduceSimulation sim, Border border, int lowerLimit, int upperLimit) {
		super(border);
		setSimulationAndFertilityRange(sim, lowerLimit, upperLimit);
		initializeMaxAgeAndSex();
	}
	
	/**
	 * Creates an agent with all properties already given
	 * except for max age and sex which are randomized
	 * @param sim
	 * @param border
	 * @param lowerLimit
	 * @param upperLimit
	 * @param initSugar
	 * @param metabolism
	 * @param visionRange
	 */
	private SugarAgentReproduce(SugarScapeReproduceSimulation sim, Border border, int lowerLimit, int upperLimit, int initSugar, int metabolism, int visionRange) {
		super(border, initSugar, metabolism, visionRange);
		setSimulationAndFertilityRange(sim, lowerLimit, upperLimit);
		initializeMaxAgeAndSex();
	}
	
	/**
	 * Function to set values for simulation and 
	 * fertility age limits to reduce duplicated
	 * code in the constructor
	 * @param sim
	 * @param lowerLimit
	 * @param upperLimit
	 */
	private void setSimulationAndFertilityRange(SugarScapeReproduceSimulation sim, int lowerLimit, int upperLimit) {
		simulation = sim;
		fertileLowerLimit = lowerLimit;
		fertileUpperLimit = upperLimit;
	}
	
	/**
	 * Randomly sets values for max age
	 * between 60 and 100 and sex
	 */
	private void initializeMaxAgeAndSex() {
		Random initValues = new Random();
		maxAge = initValues.nextInt(41)+61;
		int isMaleInt = initValues.nextInt(2);
		if (isMaleInt == 1) {
			isMale = true;
		}
		else {
			isMale = false;
		}
	}
	
	/**
	 * Increments age of agent by 1
	 */
	public void incrementAge() {
		age++;
	}
	
	/**
	 * @param otherParent
	 * @return a baby agent made between this
	 * instance of agent and otherParent passed
	 * in as parameter
	 */
	private SugarAgentReproduce getBabyAgent(SugarAgentReproduce otherParent) {
		int babyInitSugar = this.initialSugar/2 + otherParent.initialSugar/2;
		int babyMetabolism = getBabyStat(otherParent, (SugarAgentReproduce agent) -> agent.getSugarMetabolism());
		int babyVision = getBabyStat(otherParent, (SugarAgentReproduce agent) -> agent.getVision());

		return new SugarAgentReproduce(simulation, myBorder, fertileLowerLimit, fertileUpperLimit,
				babyInitSugar, babyMetabolism, babyVision);
	}
	
	/**
	 * reproduces with all neighboring agents
	 * if possible and reduces sugar values of
	 * this agent and other parent accordingly
	 */
	public void reproduce() {
		SugarScapeCell emptyCell = null;
		for (SugarScapeCell neighbor : currentLoc.getNeighbors()) {
			if (neighbor.getAgent() != null) {
				SugarAgentReproduce potentialMate = (SugarAgentReproduce) neighbor.getAgent();
				if (potentialMate.isFertile() && (this.isMale != potentialMate.isMale)) {
					emptyCell = getEmptyNeighboringCell(potentialMate);
				}
				if (emptyCell != null) {
					emptyCell.setAgent(getBabyAgent(potentialMate));
					this.sugar -= (initialSugar/2);
					potentialMate.sugar -= (potentialMate.initialSugar/2);
				}
			}
		}
	}
	
	/**
	 * @param cell
	 * @return an empty cell that neighbors the
	 * location of the given cell, or null if
	 * no such cell exists
	 */
	private SugarScapeCell getEmptyNeighboringCellFromLoc(SugarScapeCell cell) {
		SugarScapeCell emptyCell = null;
		for (SugarScapeCell neighbor : cell.getNeighbors()) {
			if (neighbor.getAgent() == null) {
				return emptyCell;
			}
		}
		return null;
	}
	
	/**
	 * @param mate
	 * @return empty cell neighboring the
	 * agent passed in
	 */
	private SugarScapeCell getEmptyNeighboringCell(SugarAgentReproduce mate) {
		SugarScapeCell emptyCell = null;
		emptyCell = getEmptyNeighboringCellFromLoc(currentLoc);
		if (emptyCell == null) {
			emptyCell = getEmptyNeighboringCellFromLoc(mate.currentLoc);
		}
		return emptyCell;
	}
	
	/**
	 * @param otherParent
	 * @param getStat
	 * @return randomized stat of 'baby' of this
	 * agent and passed in agent
	 * 25% chance stat is equal to this parents
	 * 25% chance stat is equal to other parents
	 * 50% chance stat is average between two
	 */
	private int getBabyStat(SugarAgentReproduce otherParent, Function<SugarAgentReproduce, Integer> getStat) {
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
	
	/**
	 * @return true if this agent can reproduce
	 * and false otherwise
	 */
	public boolean isFertile() {
		return (sugar >= initialSugar && age >= fertileLowerLimit && age < fertileUpperLimit);
	}
	
	/**
	 * kill the agent if its sugar is too low
	 * or if it is too old
	 */
	public void killAgentIfNeeded() {
		if (sugar <= 0 || age >= maxAge) {
			currentLoc.setAgent(null);
		}
	}
	
	/**
	 * @return true if male, false otherwise
	 */
	public boolean isMale() {
		return isMale;
	}

	/**
	 * sets value of 'isMale'
	 * @param isMale
	 */
	public void setMale(boolean isMale) {
		this.isMale = isMale;
	}
}
