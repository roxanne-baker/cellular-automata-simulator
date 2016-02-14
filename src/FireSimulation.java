
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class FireSimulation extends Simulation {

	/*
	 * @author Roxanne Baker
	 * Contains logic for representing
	 * the Fire simulation
	 */
	private double probCatch;
	public static final String FIRE = "FIRE";
	public static final String TREE = "TREE";
	public static final String EMPTY = "EMPTY";
	private int numberOfStates=3;
	private Timeline myTime;
	private static final String STYLESHEET= "fire.css";
	
	public FireSimulation(Grid myGrid, double probCatchDecimal, Group root, Timeline animation, Border border, String myName) {
		super(myGrid, myName);
		probCatch = probCatchDecimal;
		border.setGridAndBorders(myCells, false);
		myTime=animation;
		addListeners(myCells, root);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#setStateNameToColor()
	 */
	public void setStateNameToColor() {
		List<String> stateNames = new ArrayList<String>(Arrays.asList(FIRE, TREE, EMPTY));
		List<Color> colorNames = new ArrayList<Color>(Arrays.asList(Color.FIREBRICK, Color.FORESTGREEN, Color.TAN));
		setStateNameToColor(stateNames, colorNames);
	}
	
	/*
	 * updates the state of a given cell
	 */
	public void updateCell(Cell cell) {
		if (cell.getState().equals(FIRE) && !cell.justUpdated) {
			Random random = new Random();
			cell.setState(EMPTY);
			for (Cell neighbor : cell.getMyNeighbours()) {
				if (neighbor.getState().equals(TREE)) {
					double treeCatch = random.nextDouble();
					if (treeCatch <= probCatch) {
						neighbor.setState(FIRE);
						neighbor.justUpdated = true;
					}
				}		
			}
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#updateCellStates()
	 */
	public void updateCellStates() {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				updateCell(myCells[i][j]);
			}
		}
	}

	public int getNumberOfStates(){
		return numberOfStates;
	}
	public void setValue(double threshold){
		probCatch=threshold;
	}

	public String returnStyleSheet(){
		return STYLESHEET;
	}
	public void addListeners(Cell[][]myCells, Group root){
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				Cell newCell;
				newCell=myCells[i][j];
				myCells[i][j].returnNode().addEventHandler(MouseEvent.MOUSE_CLICKED, e->changeState(root,newCell));
			}
		}
	}


	public double[] returnParameters(){
		double[]param =new double[3];
		param[0]=myCells.length;
		param[1]=myCells[0].length;
		param[2]=probCatch;
		return param;
	}

	/*
	 * (non-Javadoc)
	 * @see Simulation#update()
	 */
	public void update() {
		updateCellStates();
		setCellColor(myCells);
		resetJustUpdated();
	}
}