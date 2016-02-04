package cellsociety_team11;
import java.util.ArrayList;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;
import javafx.event.*;

public class UserInterface {
	public static final int HSIZE=400;
	public static final int VSIZE=500;
	private Scene myScene;
	public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private int count=0;
    private boolean forward=false;
    private boolean active=true;
    private double rate=1;
    private Configuration myconfig;
    private Simulation RunningSimulation;
    Grid myGrid;
	Timeline animation = new Timeline();
	Simulation newSimulation= null;
	KeyFrame myFrame;
	private String[] myPossible = { 
			"GameOfLife",
	        "Seggregation",
	        "PredatorPrey",
	        "Fire"
	    };
	public UserInterface(){
		myconfig=new Configuration();
		RunningSimulation=new Simulation();
	}
	public Scene setScene(){
		Group root=new Group();
    	myScene=new Scene(root, HSIZE, VSIZE);
    	setButtons(root);
    	setGrid(root, myconfig.getWidth(),myconfig.getHeight(), myconfig.getStates());
    	return myScene;
	}
	public void setGrid(Group root, int width, int height, ArrayList<String> cellStates) {
		Grid grid = new SquareGrid(root, width, height);
		for(int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
					grid.myCells[i][j].setState(cellStates.get(i*width+j));
			}
		}
		myGrid = grid;
	}
	public void setButtons(Group root){
		Button[] newButtons=new Button[8];
		Button Load=new Button("Load New Simulation");
		newButtons[0]=Load;
		Button Start=new Button("Start the Simulation");
		newButtons[1]=Start;
		Button Stop=new Button("Stop the Simulation");
		newButtons[2]=Stop;
		Button Resume=new Button("Resume the Simulation");
		newButtons[3]=Resume;
		Button Pause=new Button("Pause the Simulation");
		newButtons[4]=Pause;
		Button Forward=new Button("Fast Forward the Simulation");
		newButtons[5]=Forward;
		Button SpeedUp=new Button("Speed Up the Simulation");
		newButtons[6]=SpeedUp;
		Button SlowDown=new Button("Slow Down the Simulation");
		newButtons[7]=SlowDown;
		for(int i=0;i<newButtons.length;i++){
			if(i<4){
				newButtons[i].setTranslateX(i*HSIZE/4);
				newButtons[i].setTranslateY(8*VSIZE/10);
			}
			else{
				newButtons[i].setTranslateX((i-4)*HSIZE/4);
				newButtons[i].setTranslateY(9*VSIZE/10);
			}
			newButtons[i].setPrefHeight(VSIZE/10);
			newButtons[i].setPrefWidth(HSIZE/4);
			root.getChildren().add(newButtons[i]);
		}
		Timeline animation = new Timeline();
		Load.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    		@Override
			public void handle(MouseEvent event) {
    			/**
    			setGrid(root);
    			**/
    			if(myconfig.getName().equals("GameOfLife")){
    				Simulation newSimulation=new GameOfLifeSimulation(myGrid.myCells);
    				RunningSimulation=newSimulation;
    				
    			}
    			Start.setOnAction(new EventHandler<ActionEvent>(){
    	            @Override
    	            public void handle(ActionEvent event) {
    	            	
    	            	KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),new EventHandler<ActionEvent>(){
		            		public void handle(ActionEvent newEvent){
		            			RunningSimulation.update();
		            		}
		            	});
    	            	animation.setCycleCount(Timeline.INDEFINITE);
    	            	animation.getKeyFrames().add(frame);
    	            	animation.play();
    	            	
    	            }
    	        });
    			Stop.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
    					animation.stop();
    					active=false;
    				}
    			});
    			Pause.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
    					animation.stop();
    				}
    			});
    			Resume.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
    					if(active){
    						animation.play();
    					}
    				}
    			});
    			SpeedUp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
    						animation.setRate(rate*2);
    				}
    			});
    			SlowDown.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
						animation.setRate(rate/2);
    				}
    			});
    			Forward.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
						animation.play();
						RunningSimulation.update();
						animation.stop();
    				}
    			});
    			
    			
    		}
    	});
	}
}
	

