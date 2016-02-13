//package cellsociety_team11;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Slider;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import javafx.event.*;
/**
 * Sets the user interface
 * @author Zdravko Paskalev
 *
 */
public class UserInterface {
	public static final int HSIZE=400;
	public static final int VSIZE=750;
	public static final int LSIZE=100;
	public static final int SWIDTH=300;
	public static final int GWIDTH=350;
	public static final int VSLIDER=550;
	public static final int GHEIGHT=100;
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
    private LineChart<Number,Number> myChart;
    private Slider mySlider;
    private XYChart.Series [] series;
    private int iteration=0;
   
    
    
    
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
	/**
	 * Creates a new user interface and initializes the running simulation
	 */
	public UserInterface(){
		//RunningSimulation=new Simulation();
		myResources = ResourceBundle.getBundle(DEFAULT_RESOURCE_PACKAGE + "Buttons");
	}
	/**
	 * Sets the scene and creates the method to set up the buttons
	 * @return
	 */
	public Scene setScene(){
		Group root=new Group();
    	myScene=new Scene(root, HSIZE, VSIZE);
    	setButtons(root);
    	return myScene;
	}
	/**
	 * Sets up the grid for a simulation
	 * @param root Group to hold the grid cells
	 * @param height number of rows in the grid
	 * @param width number of columns in the grid
	 * @param cellStates ArrayList with the states of the cells in the grid
	 */
	public void setGrid(Group root, int height, int width, ArrayList<String> cellStates) {
		Grid grid = new TriangleGrid(root, height, width);
		for(int i=0; i<height; i++) {
			for (int j=0; j<width; j++) {
				grid.myCells[i][j].setState(cellStates.get(i*width+j));
			}
		}
		myGrid = grid;
	}
	/**
	 * Sets up a new simulation by calling a configuration to read the simulation parameters and type 
	 * from a file
	 * @param root
	 */
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
		newSimulation=getNewSimulation(myGrid,root,newConfiguration, name);
		
		RunningSimulation=newSimulation;
		setLineChart(root);
		setSlider(root, name);
	}
	public Simulation getNewSimulation(Grid myGrid, Group root, Configuration newConfiguration, String name){
		Simulation newSimulation;
		// ADD BORDER TYPE
		Border myBorder = new ToroidalBorder();
		if(name.equals(myPossibilities[0])){
			newSimulation=new GameOfLifeSimulation(myGrid,root, animation, myBorder);
		}
		else if (name.equals(myPossibilities[1])){
			newSimulation=new SegregationSimulation(myGrid, newConfiguration.getThreshold(),root, animation, myBorder);
		}
		
		else if(name.equals(myPossibilities[2])){
		    newSimulation=new PredatorPreySimulation(myGrid, newConfiguration.getPredatorStarve(), newConfiguration.getPredatorBreed(),
		                                             newConfiguration.getPreyBreed(),root, animation, myBorder);
		                                             
		}
		
		else{
			newSimulation=new FireSimulation(myGrid, newConfiguration.getProbabilityCatch(),root, animation, myBorder);			
		}
		return newSimulation;
	}
	public void setSlider(Group root, String name){
		Slider slider;
		if (name.equals(myPossibilities[1]) || name.equals(myPossibilities[3])) {
			slider = new Slider(0, 1, 0.3);
			mySlider=slider;
			slider.setMajorTickUnit(0.25f);
			slider.setBlockIncrement(0.1f);
			addSliderHandler(mySlider, root);
		}
	}
	public void addSliderHandler(Slider slider, Group root){
		
		slider.setShowTickMarks(true);
		slider.setShowTickLabels(true);
		slider.setPrefWidth(SWIDTH);
		root.getChildren().add(slider);
		slider.setTranslateY(VSLIDER);
		slider.setTranslateX(HSIZE/2-SWIDTH/2);
			EventHandler<MouseEvent>sliderHandler=new EventHandler<MouseEvent>(){
				public void handle(MouseEvent event) {
					while(slider.isValueChanging()==true){}
						RunningSimulation.setValue(slider.getValue());
						System.out.println(slider.getValue());
				}
			};
		slider.addEventHandler(MouseEvent.MOUSE_CLICKED, sliderHandler);
	}
	/**
	 * Removes the cell nodes from the scene
	 * @param root
	 */
	public void clearMyGrid(Group root){
		for(int i=0;i<myGrid.myCells.length;i++){
			for( int j=0; j<myGrid.myCells[0].length;j++){
				root.getChildren().remove(myGrid.myCells[i][j].shape);
			}
		}
		root.getChildren().remove(myChart);
		root.getChildren().remove(mySlider);
		myScene.getStylesheets().remove(DEFAULT_RESOURCE_PACKAGE + RunningSimulation.returnStyleSheet());
	}
	/**
	 * Creates a Button given its name and initializes an array with all the Buttons
	 * @param buttonString
	 * @param buttonNum
	 * @return
	 */
	public Button createButton(String buttonString, int buttonNum) {
		Button myButton = new Button(myResources.getString(buttonString));
		myButton.setWrapText(true);
		allButtons[buttonNum] = myButton;
		return myButton;
	}
	/**
	 * Calls createButton to initialize the buttons of the user interface 
	 */
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
	/**
	 * Places the buttons on the grid
	 * @param root
	 */
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
	/**
	 * Calls methods to create the Buttons and sets up the Combo box
	 * @param root
	 */
	public void setButtons(Group root){
		createAllButtons();
		placeButtons(root);
		myFiles=new ComboBox<String>();
		myFiles.getItems().addAll("gameoflife.xml", "gameoflife2.xml", "gameoflife3.xml", "segregation.xml", "segregation2.xml", "cornerFire.xml", "centerFire.xml", 
		                          "patchyFire.xml", "segregation3.xml", "predatorprey.xml", "predatorprey2.xml", "predatorprey3.xml");
		root.getChildren().add(myFiles);
		loadHandler(root);
		
	}
	/**
	 * Adds handler to the load button
	 * @param root
	 */
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
	/**
	 * Calls methods to set and add new handlers to the Buttons
	 * @param root
	 */
	public void setNewHandlers(Group root){
		setHandlers(root);
		addHandlers();
	}
	/**
	 * Calls methods to set eventhandlers to the buttons
	 * @param root
	 */
	private void setHandlers(Group root) {
		setStartHandler();
		setStopHandler(root);
		setPauseHandler();
		setResumeHandler();
		setSpeedUpHandler();
		setSlowDownHandler();
		setForwardHandler();
	}
	/**
	 * adds event handlers to the buttons
	 */
	private void addHandlers() {
		Start.addEventHandler(ActionEvent.ACTION, startHandler);
		Pause.addEventHandler(MouseEvent.MOUSE_CLICKED, pauseHandler);
		Stop.addEventHandler(MouseEvent.MOUSE_CLICKED, stopHandler);
		Resume.addEventHandler(MouseEvent.MOUSE_CLICKED, resumeHandler);
		SpeedUp.addEventHandler(MouseEvent.MOUSE_CLICKED, speedUpHandler);
		SlowDown.addEventHandler(MouseEvent.MOUSE_CLICKED, slowDownHandler);
		Forward.addEventHandler(MouseEvent.MOUSE_CLICKED, forwardHandler);
	}
	/**
	 * Sets up the eventhandler for the start button
	 */
	private void setStartHandler() {
		startHandler=new EventHandler<ActionEvent>(){
			public void handle(ActionEvent event) {
        	
        	myFrame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),new EventHandler<ActionEvent>(){
        		public void handle(ActionEvent newEvent){
        			RunningSimulation.update();
        			updateChart(RunningSimulation);
        		}
        	});
        	animation.setCycleCount(Timeline.INDEFINITE);
        	animation.getKeyFrames().add(myFrame);
        	animation.play();
        	
        }
    };
	}
	/**
	 * Sets event handler for the forward button
	 */
	private void setForwardHandler() {
		forwardHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(active){
					animation.play();
					RunningSimulation.update();
					updateChart(RunningSimulation);
					animation.stop();
				}
			}
		};
	}
	/**
	 * Sets Event handler for the slow down button
	 */
	private void setSlowDownHandler() {
		slowDownHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				currentRate = Math.min(0, currentRate - 0.025);
				animation.setRate(currentRate);
			}
		};
	}
	/**
	 * Sets up event handler for the speed up button
	 */
	private void setSpeedUpHandler() {
		speedUpHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
					currentRate += 0.025;
					animation.setRate(currentRate);
			}
		};
	}
	/**
	 * Sets up event handler for the resume button
	 */
	private void setResumeHandler() {
		resumeHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				if(active){
					animation.play();
				}
			}
		};
	}
	/**
	 * Sets event handler for the pause button
	 */
	private void setPauseHandler() {
		pauseHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				animation.stop();
			}
		};
	}
	/**
	 * Sets event handler for the stop button
	 */
	private void setStopHandler(Group root) {
		stopHandler=new EventHandler<MouseEvent>(){
			public void handle(MouseEvent event){
				animation.stop();
				clearMyGrid(root);
				active=false;
			}
		};
	}
	/**
	 * Removes event handlers from the scene
	 */
	public void removeHandlers(){
		Pause.removeEventHandler(MouseEvent.MOUSE_CLICKED, pauseHandler);
		Stop.removeEventHandler(MouseEvent.MOUSE_CLICKED, stopHandler);
		Resume.removeEventHandler(MouseEvent.MOUSE_CLICKED, resumeHandler);
		SpeedUp.removeEventHandler(MouseEvent.MOUSE_CLICKED, speedUpHandler);
		SlowDown.removeEventHandler(MouseEvent.MOUSE_CLICKED, slowDownHandler);
		Forward.removeEventHandler(MouseEvent.MOUSE_CLICKED,forwardHandler);
	}
	public void setLineChart(Group root){
		final NumberAxis xAxis = new NumberAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setForceZeroInRange(true);
        iteration=0;
		myChart=new LineChart<Number, Number>(xAxis,yAxis);
		myChart.setCreateSymbols(false);
		int j=RunningSimulation.getNumberOfStates();
		series = new XYChart.Series[j];
		int i=0;
		HashMap<Color, Number> newReturn=new HashMap<Color, Number>();
		newReturn=RunningSimulation.returnProportion();
		myScene.getStylesheets().add(DEFAULT_RESOURCE_PACKAGE + RunningSimulation.returnStyleSheet());
		for(Color s:newReturn.keySet()){
			series[i]=new XYChart.Series();
			myChart.getData().add(series[i]);
			i++;
		}
		myChart.setTranslateY(HSIZE);
		myChart.setPrefHeight(GHEIGHT);
		myChart.setPrefWidth(7*HSIZE/8);
		root.getChildren().add(myChart);
	}
	public void updateChart(Simulation newS){
		int j=RunningSimulation.getNumberOfStates();
		HashMap<Color,Number>newProportions=new HashMap<Color,Number>();
		newProportions=RunningSimulation.returnProportion();
		int i=0;
		for(Color s:newProportions.keySet()){
			double k=(double)newProportions.get(s);
			series[i].getData().add(new XYChart.Data(iteration,k));
			i++;
			iteration++;
		}
		
		
	}
}