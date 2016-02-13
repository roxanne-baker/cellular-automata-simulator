
import java.util.HashMap;
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
	
	public FireSimulation(Grid myGrid, double probCatchDecimal, Group root, Timeline animation) {
		super(myGrid);
		probCatch = probCatchDecimal;
		myGrid.addAllNeighbors(myCells, (grid, position) -> myGrid.addCardinalNeighbors(grid, position));
		myTime=animation;
		addListeners(myCells, root);
	}
	
	/*
	 * (non-Javadoc)
	 * @see Simulation#setStateNameToColor()
	 */
	public void setStateNameToColor() {
		stateNameToColor = new HashMap<String, Color>();
		stateNameToColor.put(FIRE, Color.FIREBRICK);
		stateNameToColor.put(TREE, Color.FORESTGREEN);
		stateNameToColor.put(EMPTY, Color.TAN);
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
	public HashMap<Color, Number> returnProportion(){
		int countFire=0;
		int countTree=0;
		int countEmpty=0;
		int total=0;
		HashMap<Color, Number> proportions=new HashMap<Color, Number>();
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				if(myCells[i][j].getState().equals(FIRE)){
					countFire++;
				}
				else if(myCells[i][j].getState().equals(TREE)){
					countTree++;
				}
				else if(myCells[i][j].getState().equals(EMPTY)){
					countEmpty++;
				}
				total++;
			}
		}
		double prop1=(double)countFire/total;
		double prop2=(double)countTree/total;
		double prop3=(double)countEmpty/total;
		proportions.put(Color.FIREBRICK, prop1);
		proportions.put(Color.FORESTGREEN, prop2);
		proportions.put(Color.TAN, prop3);
		return proportions;
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
	public void changeState(Group root, Cell myCell){
		myTime.stop();
		if(myCell.getState().equals(FIRE)){
			myCell.setState(EMPTY);
			myCell.shape.setFill(Color.TAN);
		}
		else if(myCell.getState().equals(TREE)){
			myCell.setState(FIRE);
			myCell.shape.setFill(Color.FIREBRICK);
		}
		else if(myCell.getState().equals(EMPTY)){
			myCell.setState(TREE);
			myCell.shape.setFill(Color.FORESTGREEN);
		}
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