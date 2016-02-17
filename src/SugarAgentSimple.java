
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
			currentLoc.setAgent(null);
		}
	}	
}
