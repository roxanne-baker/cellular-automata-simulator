import java.util.List;

import javafx.scene.paint.Color;

public class GameOfLifeSimulation extends Simulation {
	private Cell[][] myCells;
	private int count=0;
	public GameOfLifeSimulation(Cell[][] newCells){
		myCells=newCells;
		setAllNeighbors();
		setCellColor();
	}
	@Override
	public void updateState() {
		// TODO Auto-generated method stub
		System.out.println("Count:" + count);
		count++;
		String[][] newState=new String[myCells.length][myCells[0].length];
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				newState[i][j]=myCells[i][j].getState();
			}
		}
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				int k=myCells[i][j].getMyNeighbours().size();
				int count=0;
				List<Cell> neighbours = myCells[i][j].getMyNeighbours();
				for(int l=0;l<k;l++){
					if(neighbours.get(l).getState().equals("live")){
						count++;
					}
				}
				if((count<2 || count>3) && myCells[i][j].getState().equals("live")){
					newState[i][j]="dead";
				}
				if(count==3 && myCells[i][j].getState().equals("dead")){
					newState[i][j]="live";
				}
			}
		}
		for(int i=0;i<myCells.length;i++){
			for(int j=0;j<myCells[0].length;j++){
				myCells[i][j].setState(newState[i][j]);
			}
		}
	}
	public void setCellColor(){
		System.out.println("In method 2");
		for (int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				if (myCells[i][j].getState().equals("live")) {
					myCells[i][j].shape.setFill(Color.WHITE);
				}
				else if (myCells[i][j].getState().equals("dead")) {
					myCells[i][j].shape.setFill(Color.BLACK);
				}
			}
		}
	}
	public void setAllNeighbors() {
		for(int i=0; i<myCells.length; i++) {
			for (int j=0; j<myCells[0].length; j++) {
				setNeighbors(myCells[i][j], i, j);
			}
		}
	}
	
	public void setNeighbors(Cell cell, int row, int col) {
		boolean isFirstRow = (row == 0);
		boolean isLastRow = (row == myCells.length-1);
		boolean isFirstCol = (col == 0);
		boolean isLastCol = (col == myCells[0].length-1);
		if (!isFirstRow) {
			if (!isFirstCol) {
				cell.add(myCells[row-1][col-1]);
			}
			cell.add(myCells[row-1][col]);
			if (!isLastCol) {
				cell.add(myCells[row-1][col+1]);
			}
		}
		if (!isFirstCol) {
			cell.add(myCells[row][col-1]);
		}
		if (!isLastCol) {
			cell.add(myCells[row][col+1]);
		}
		if (!isLastRow) {
			if (!isFirstCol) {
				cell.add(myCells[row+1][col-1]);
			}
			cell.add(myCells[row+1][col]);
			if (!isLastCol) {
				cell.add(myCells[row+1][col+1]);
			}
		}
	}
	public void update() {
		updateState();
		setCellColor();
	}
	
}