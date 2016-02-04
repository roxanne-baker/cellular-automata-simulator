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
    private boolean active=true;
    private double rate=0.05;
    Grid myGrid;
	Simulation newSimulation= null;
	KeyFrame myFrame;
	
	public UserInterface(){
	}
	public Scene setScene(){
		Group root=new Group();
    	myScene=new Scene(root, HSIZE, VSIZE);
    	setButtons(root);
    	setGrid(root);
    	return myScene;
	}
	
//	public void setGrid(Group root) {
//		Grid grid = new SquareGrid(root, 6, 6);
//		for(int i=0; i<6; i++) {
//			for (int j=0; j<6; j++) {
//				if ((i==1 || i==2) && (j==1 || j==2) &&  (!(i==2 && j==2))) {
//					grid.myCells[i][j].setState("live");					
//				}
//				else if ((i==3 ||i==4) && (j==3 || j==4) && (!(i==3 && j==3))) {
//					grid.myCells[i][j].setState("live");
//				}
//				else {
//					grid.myCells[i][j].setState("dead");
//				}
//			}
//		}
//		myGrid = grid;
//	}
	
	public void setGrid(Group root) {
		Grid grid = new SquareGrid(root, 6, 6);
		for(int i=0; i<6; i++) {
			for (int j=0; j<6; j++) {
				if ((i==1 || i==2) && (j==1 || j==2) &&  (!(i==2 && j==2))) {
					grid.myCells[i][j].setState("PREDATOR");					
				}
				else if ((i==3 ||i==4) && (j==3 || j==4) && (!(i==3 && j==3))) {
					grid.myCells[i][j].setState("PREY");
				}
				else {
					grid.myCells[i][j].setState("EMPTY");
				}
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
		animation.setRate(0.05);
		System.out.println(animation.getRate());
		Load.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    		@Override
			public void handle(MouseEvent event) {
    			setGrid(root);
    			Simulation newSimulation=new PredatorPreySimulation(myGrid.myCells, 10, 6, 2);
    			Start.setOnAction(new EventHandler<ActionEvent>(){
    	            @Override
    	            public void handle(ActionEvent event) {
    	            	
    	            	KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),new EventHandler<ActionEvent>(){
		            		public void handle(ActionEvent newEvent){
		            				newSimulation.update();
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
    					rate *= 2;
    						animation.setRate(rate);
    				}
    			});
    			SlowDown.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
    					rate /= 2;
						animation.setRate(rate);
    				}
    			});
    			Forward.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
						animation.play();
						newSimulation.update();
						animation.stop();
    				}
    			});
    			
    			
    		}
    	});
	}
}