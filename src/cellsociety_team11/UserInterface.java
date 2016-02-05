package cellsociety_team11;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
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
    public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
    private ResourceBundle myResources;
    private ComboBox<String> myFiles;
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
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "Buttons");
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
		Button Load=new Button(myResources.getString("LoadButton"));
		newButtons[0]=Load;
		Load.setWrapText(true);
		Button Start=new Button(myResources.getString("StartButton"));
		newButtons[1]=Start;
		Start.setWrapText(true);
		Button Stop=new Button(myResources.getString("StopButton"));
		Stop.setWrapText(true);
		newButtons[2]=Stop;
		Button Resume=new Button(myResources.getString("ResumeButton"));
		Resume.setWrapText(true);
		newButtons[3]=Resume;
		Button Pause=new Button(myResources.getString("PauseButton"));
		Pause.setWrapText(true);
		newButtons[4]=Pause;
		Button Forward=new Button(myResources.getString("FastForwardButton"));
		Forward.setWrapText(true);
		newButtons[5]=Forward;
		Button SpeedUp=new Button(myResources.getString("SpeedUpButton"));
		SpeedUp.setWrapText(true);
		newButtons[6]=SpeedUp;
		Button SlowDown=new Button(myResources.getString("SlowDownButton"));
		SlowDown.setWrapText(true);
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
		myFiles=new ComboBox<String>();
		myFiles.getItems().addAll("GameOfLife","GameOfLife2","GameOfLife3");
		root.getChildren().add(myFiles);
		Load.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    		@Override
			public void handle(MouseEvent event) {
    			/**
    			setGrid(root);
    			**/
    			String myFile=myFiles.getValue();
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
	public Configuration setUpConfiguration(String file){
		Configuration newCon= new Configuration(file);
		return newCon;
	}
}
	

