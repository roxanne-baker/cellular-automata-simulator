package cellsociety_team11;
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
    private Button Load;
    private Button Start;
    private Button Stop;
    private Button Resume;
    private Button Forward;
    private Button SpeedUp;
    private Button SlowDown;
    private Button Pause;
	public UserInterface(){
		
	}
	public Scene setScene(){
		Group root=new Group();
    	myScene=new Scene(root, HSIZE, VSIZE);
    	setButtons(root);
    	return myScene;
	}
	public void setButtons(Group root){
		Button[] newButtons=new Button[8];
		Load=new Button("Load New Simulation");
		newButtons[0]=Load;
		Start=new Button("Start the Simulation");
		newButtons[1]=Start;
		Stop=new Button("Stop the Simulation");
		newButtons[2]=Stop;
		Resume=new Button("Resume the Simulation");
		newButtons[3]=Resume;
		Pause=new Button("Pause the Simulation");
		newButtons[4]=Pause;
		Forward=new Button("Fast Forward the Simulation");
		newButtons[5]=Forward;
		SpeedUp=new Button("Speed Up the Simulation");
		newButtons[6]=SpeedUp;
		SlowDown=new Button("Slow Down the Simulation");
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
		Load.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    		@Override
			public void handle(MouseEvent event) {
    			Timeline animation = new Timeline();
    			Simulation newSimulation=new Simulation();
    			Start.setOnAction(new EventHandler<ActionEvent>(){
    	            @Override
    	            public void handle(ActionEvent event) {
    	            	KeyFrame frame = new KeyFrame(Duration.millis(MILLISECOND_DELAY),new EventHandler<ActionEvent>(){
		            		public void handle(ActionEvent newEvent){
		            			newSimulation.update();
		            		}
		            	});
    	            }
    	        });
    			Stop.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
    					animation.stop();
    				}
    			});
    			Resume.addEventHandler(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
    				public void handle(MouseEvent event){
    					animation.play();
    				}
    			});
    		}
    	});
		
		
	}
}
	

