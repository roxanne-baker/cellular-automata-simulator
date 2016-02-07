//package cellsociety_team11;
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
	public static final double STARTING_RATE = 0.075;
	private Scene myScene;
	public static final int FRAMES_PER_SECOND = 60;
    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
    private boolean active=true;
    private double currentRate=STARTING_RATE;
    private Simulation RunningSimulation;
    public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
    public static final String DEFAULT_DIRECTORY = "src/resources/";
    private ResourceBundle myResources;
    private ComboBox<String> myFiles;
    private Button Load;
    private Button Start;
    private Button Resume;
    private Button Forward;
    private Button SpeedUp;
    private Button SlowDown;
    private Button Stop;
    private Button Pause;
    private EventHandler<MouseEvent> forwardHandler;
    private EventHandler<MouseEvent> stopHandler;
    private EventHandler<MouseEvent> resumeHandler;
    private EventHandler<ActionEvent> startHandler;
    private EventHandler<MouseEvent> speedUpHandler;
    private EventHandler<MouseEvent> slowDownHandler;
    private EventHandler<MouseEvent> pauseHandler;
    
    private boolean firsttime=true;
    Grid myGrid;
	Timeline animation = new Timeline();
	Simulation newSimulation= null;
	KeyFrame myFrame;
	private String[] myPossibilities = { 
			"GameOfLife",
	        "Seggregation",
	        "PredatorPrey",
	        "Fire"
	    };
	public UserInterface(){
		RunningSimulation=new Simulation();
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "Buttons");
	}
	public Scene setScene(){
		Group root=new Group();
    	myScene=new Scene(root, HSIZE, VSIZE);
    	setButtons(root);
    	return myScene;
	}
	public void setGrid(Group root, int width, int height, ArrayList<String> cellStates, Simulation simName) {
		Grid grid = new SquareGrid(root, width, height);
		for(int i=0; i<width; i++) {
			for (int j=0; j<height; j++) {
				System.out.println(i+" "+j);
				grid.myCells[i][j].setState(cellStates.get(i*height+j));
			}
		}
		myGrid = grid;
	}
	public void setNewSimulation(Group root){
		animation.setRate(STARTING_RATE);
		active=true;
		String myFile=DEFAULT_DIRECTORY+myFiles.getValue();
		Configuration newConfiguration=new Configuration(myFile);
		int width=newConfiguration.getWidth();
		int height=newConfiguration.getHeight();
		ArrayList<String>states=new ArrayList<String>();
		states=newConfiguration.getStates();
		
		Object simClass = null;
		String name=newConfiguration.getName();
		System.out.println(name);
		try {
			simClass = Class.forName(name+"Simulation").cast(simClass);
		} catch (ClassNotFoundException e) {
			System.out.println("Class "+name+"Simulation NotFound");
			System.out.println("Please load another file.");
		}
		Simulation simCast = (Simulation) simClass;
		setGrid(root, width, height, states, simCast);
		Simulation newSimulation;
		if(name.equals(myPossibilities[0])){
			newSimulation=new GameOfLifeSimulation(myGrid);
		}
		else{
			newSimulation=new SegregationSimulation(myGrid, newConfiguration.getThreshold());
		}
		RunningSimulation=newSimulation;
	}
	public void clearMyGrid(Group root){
		for(int i=0;i<myGrid.myCells.length;i++){
			for( int j=0; j<myGrid.myCells[0].length;j++){
				root.getChildren().remove(myGrid.myCells[i][j].shape);
			}
		}
	}
	public void setButtons(Group root){
		Button[] newButtons=new Button[8];
		Load=new Button(myResources.getString("LoadButton"));
		newButtons[0]=Load;
		Load.setWrapText(true);
		Start=new Button(myResources.getString("StartButton"));
		newButtons[1]=Start;
		Start.setWrapText(true);
		Stop=new Button(myResources.getString("StopButton"));
		Stop.setWrapText(true);
		newButtons[2]=Stop;
		Resume=new Button(myResources.getString("ResumeButton"));
		Resume.setWrapText(true);
		newButtons[3]=Resume;
		Pause=new Button(myResources.getString("PauseButton"));
		Pause.setWrapText(true);
		newButtons[4]=Pause;
		Forward=new Button(myResources.getString("FastForwardButton"));
		Forward.setWrapText(true);
		newButtons[5]=Forward;
		SpeedUp=new Button(myResources.getString("SpeedUpButton"));
		SpeedUp.setWrapText(true);
		newButtons[6]=SpeedUp;
		SlowDown=new Button(myResources.getString("SlowDownButton"));
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
		myFiles=new ComboBox<String>();
		myFiles.getItems().addAll("test.xml", "test2.xml","test3.xml", "segregation.xml", "segregation2.xml");
		root.getChildren().add(myFiles);
		Load.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    		@Override
			public void handle(MouseEvent event) {
    			if(firsttime==false){
    				animation.stop();
    				clearMyGrid(root);
    				animation.getKeyFrames().remove(myFrame);
    				removeHandlers();
    				Start.removeEventHandler(ActionEvent.ACTION, startHandler);
    			}
    			firsttime=false;
    			setNewSimulation(root);
    			setNewHandlers(root);
    			
    		}
    	});
	}
	public Configuration setUpConfiguration(String file){
		Configuration newCon= new Configuration(file);
		return newCon;
	}
	public void setNewHandlers(Group root){
		startHandler=new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
        	
        	myFrame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),new EventHandler<ActionEvent>(){
        		public void handle(ActionEvent newEvent){
        			RunningSimulation.update();
        		}
        	});
        	animation.setCycleCount(Timeline.INDEFINITE);
        	animation.getKeyFrames().add(myFrame);
        	animation.play();
        	
        }
    };
    
		stopHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				animation.stop();
				clearMyGrid(root);
				active=false;
			}
		};
		pauseHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				animation.stop();
			}
		};
		resumeHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(active){
					animation.play();
				}
			}
		};
		speedUpHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
					currentRate += 0.025;
					animation.setRate(currentRate);
			}
		};
		slowDownHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				currentRate = Math.min(0, currentRate - 0.025);
				animation.setRate(currentRate);
			}
		};
		forwardHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(active){
					animation.play();
					RunningSimulation.update();
					animation.stop();
				}
			}
		};
		Start.addEventHandler(ActionEvent.ACTION, startHandler);
		Pause.addEventHandler(MouseEvent.MOUSE_CLICKED, pauseHandler);
		Stop.addEventHandler(MouseEvent.MOUSE_CLICKED, stopHandler);
		Resume.addEventHandler(MouseEvent.MOUSE_CLICKED, resumeHandler);
		SpeedUp.addEventHandler(MouseEvent.MOUSE_CLICKED, speedUpHandler);
		SlowDown.addEventHandler(MouseEvent.MOUSE_CLICKED, slowDownHandler);
		Forward.addEventHandler(MouseEvent.MOUSE_CLICKED, forwardHandler);
	}
	public void removeHandlers(){
		Pause.removeEventHandler(MouseEvent.MOUSE_CLICKED, pauseHandler);
		Stop.removeEventHandler(MouseEvent.MOUSE_CLICKED, stopHandler);
		Resume.removeEventHandler(MouseEvent.MOUSE_CLICKED, resumeHandler);
		SpeedUp.removeEventHandler(MouseEvent.MOUSE_CLICKED, speedUpHandler);
		SlowDown.removeEventHandler(MouseEvent.MOUSE_CLICKED, slowDownHandler);
		Forward.removeEventHandler(MouseEvent.MOUSE_CLICKED,forwardHandler);
	}
}
	

