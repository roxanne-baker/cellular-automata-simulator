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
//    private static final int MILLISECOND_DELAY = 1000 / FRAMES_PER_SECOND;
//    private Button Load;
//    private Button Start;
//    private Button Stop;
//    private Button Resume;
//    private Button Forward;
//    private Button SpeedUp;
//    private Button SlowDown;
//    private Button Pause;
//    private int count=0;
    private boolean forward=false;
    private boolean active=true;
    private boolean runningAnimation = false;
    private boolean loaded = false;
    private double frameRate = 150;
//    private double rate=1;
    Grid myGrid;
	Timeline animation = new Timeline();
	Simulation newSimulation= null;
    
	public UserInterface(){
		
	}
	
	public Scene setScene(){
		Group root=new Group();
    	myScene=new Scene(root, HSIZE, VSIZE);
    	setButtons(root);
    	setGrid(root);
    	return myScene;
	}
	
	// for testing purposes until able to parse XML
	public void setGrid(Group root) {
		Grid grid = new SquareGrid(root, 5, 5);
		for(int i=0; i<5; i++) {
			for (int j=0; j<5; j++) {
				if (j==0 && (i != 1) ||
						(i == 3 && j != 1) ||
						(i==0 && j!= 2 && j != 4) ||
						(i == 2 && j == 1)) {
					grid.myGrid[j][i].state = "BLUE";					
				}
				else if (j==1 || (i==1 && j != 0) ||
						(j==2 || j==4) && (i==0 || i==4)) {
					grid.myGrid[j][i].state = "RED";
				}
				else {
					grid.myGrid[j][i].state = "EMPTY";
				}
			}
		}
		myGrid = grid;
	}
	
	public boolean playAnimation(double elapsedTime) {
		if (loaded && runningAnimation && active && elapsedTime > frameRate) {
//    			if(!forward){
    				newSimulation.update();
//    				newSimulation.updateNeighbours();
//    			}
//    			else{
//    				while(count<5){
//    					newSimulation.update();
////    					newSimulation.updateNeighbours();
//    					count++;
//    				}
//    				forward=false;	
//    			}	
    			return true;
		}
		else if (loaded && forward) {
			newSimulation.update();
			forward = false;
		}
		return false;
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
		//will need to change once XML configuration added	
		Load.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    		@Override
			public void handle(MouseEvent event) {
//    			animation = new Timeline();
    			newSimulation=new SegregationSimulation(myGrid.myGrid, 30);
    			loaded = true;
    			active = true;
    		}
		});
		
        Start.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    	    @Override
    	    public void handle(MouseEvent event) {
    	        runningAnimation = true;
		    }
		});

        Stop.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
        	public void handle(MouseEvent event){
        		runningAnimation = false;
        		active=false;
        	}
        });
        Pause.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
        	public void handle(MouseEvent event){
        		runningAnimation = false;
        	}
        });
        Resume.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
        	public void handle(MouseEvent event){
        		if(active){
        			runningAnimation = true;
        		}
        	}
        });
        SpeedUp.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
        	public void handle(MouseEvent event){
        		frameRate /= 2;
        	}
        });
        SlowDown.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
        	public void handle(MouseEvent event){
        		frameRate *= 2;
        	}
        });
        Forward.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
        	public void handle(MouseEvent event){
        		forward=true;
        	}
        });		

	}
}