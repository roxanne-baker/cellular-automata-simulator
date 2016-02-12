//package cellsociety_team11;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Consumer;

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
    private Simulation RunningSimulation = null;
    public static final String DEFAULT_RESOURCE_PACKAGE = "resources/";
    public static final String DEFAULT_DIRECTORY = "src/resources/";
    private ResourceBundle myResources;
    private ComboBox<String> myFiles;
    private Button[] allButtons;
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
	        "Segregation",
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
	
	public void setGrid(Group root, int height, int width, ArrayList<String> cellStates) {
		Grid grid = new TriangleGrid(root, height, width);
		for(int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				grid.myCells[i][j].setState(cellStates.get(i*width+j));
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
		
		String name=newConfiguration.getName();

		setGrid(root, height, width, states);
		Simulation newSimulation;
		Border myBorder = new ToroidalBorder();
		if(name.equals(myPossibilities[0])){
			newSimulation=new GameOfLifeSimulation(myGrid, myBorder);
		}
		else if (name.equals(myPossibilities[1])){
			newSimulation=new SegregationSimulation(myGrid, myBorder, newConfiguration.getThreshold());
		}
		
		else if(name.equals(myPossibilities[2])){
		    newSimulation=new PredatorPreySimulation(myGrid, myBorder, newConfiguration.getPredatorStarve(), newConfiguration.getPredatorBreed(),
		                                             newConfiguration.getPreyBreed());                                            
		}
		else{
			newSimulation=new FireSimulation(myGrid, myBorder, newConfiguration.getProbabilityCatch());			
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
	
	public Button createButton(String buttonString, int buttonNum) {
		Button myButton = new Button(myResources.getString(buttonString));
		myButton.setWrapText(true);
		allButtons[buttonNum] = myButton;
		return myButton;
	}
	
	public void createAllButtons() {
		allButtons=new Button[8];
		Load = createButton("LoadButton", 0);
		Start = createButton("StartButton", 1);
		Stop = createButton("StopButton", 2);
		Resume = createButton("ResumeButton", 3);
		Pause = createButton("PauseButton", 4);
		Forward = createButton("FastForwardButton", 5);
		SpeedUp = createButton ("SpeedUpButton", 6);
		SlowDown = createButton("SlowDownButton", 7);
	}
	
	public void placeButtons(Group root) {
		for(int i=0;i<allButtons.length;i++){
			if(i<4){
				allButtons[i].setTranslateX(i*HSIZE/4);
				allButtons[i].setTranslateY(8*VSIZE/10);
			}
			else{
				allButtons[i].setTranslateX((i-4)*HSIZE/4);
				allButtons[i].setTranslateY(9*VSIZE/10);
			}
			allButtons[i].setPrefHeight(VSIZE/10);
			allButtons[i].setPrefWidth(HSIZE/4);
			root.getChildren().add(allButtons[i]);
		}
	}
	
	public void setButtons(Group root){
		createAllButtons();
		placeButtons(root);
		myFiles=new ComboBox<String>();
		myFiles.getItems().addAll("test.xml", "test2.xml", "test3.xml", "segregation.xml", "segregation2.xml", "cornerFire.xml", "centerFire.xml", 
		                          "patchyFire.xml", "segregation3.xml", "predatorprey.xml", "predatorprey2.xml", "predatorprey3.xml");
		root.getChildren().add(myFiles);
		loadHandler(root);
		
	}
	private void loadHandler(Group root) {
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
	
	public void setNewHandlers(Group root){
		setHandlers(root);
		addHandlers();
	}
	
	private void setHandlers(Group root) {
		setStartHandler();
		setStopHandler(root);
		setPauseHandler();
		setResumeHandler();
		setSpeedUpHandler();
		setSlowDownHandler();
		setForwardHandler();
	}
	
	private void addHandlers() {
		Start.addEventHandler(ActionEvent.ACTION, startHandler);
		Pause.addEventHandler(MouseEvent.MOUSE_CLICKED, pauseHandler);
		Stop.addEventHandler(MouseEvent.MOUSE_CLICKED, stopHandler);
		Resume.addEventHandler(MouseEvent.MOUSE_CLICKED, resumeHandler);
		SpeedUp.addEventHandler(MouseEvent.MOUSE_CLICKED, speedUpHandler);
		SlowDown.addEventHandler(MouseEvent.MOUSE_CLICKED, slowDownHandler);
		Forward.addEventHandler(MouseEvent.MOUSE_CLICKED, forwardHandler);
	}
	
	private void setStartHandler() {
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
	}
	
	private void setForwardHandler() {
		forwardHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(active){
					animation.play();
					RunningSimulation.update();
					animation.stop();
				}
			}
		};
	}
	
	private void setSlowDownHandler() {
		slowDownHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				currentRate = Math.max(0, currentRate - 0.025);
				animation.setRate(currentRate);
			}
		};
	}
	
	private void setSpeedUpHandler() {
		speedUpHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
					currentRate += 0.025;
					animation.setRate(currentRate);
			}
		};
	}
	
	private void setResumeHandler() {
		resumeHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(active){
					animation.play();
				}
			}
		};
	}
	
	private void setPauseHandler() {
		pauseHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				animation.stop();
			}
		};
	}
	
	private void setStopHandler(Group root) {
		stopHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				animation.stop();
				clearMyGrid(root);
				active=false;
			}
		};
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
	

