import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.event.*;

import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
	private Scene myScene;
	public static void main(String[] args) {
		// TODO Auto-generated constructor stub
		launch(args);
	}

	@Override
	public void start(Stage primaryStage){
		// TODO Auto-generated method stub
		UserInterface newInterface=new UserInterface();
		myScene=newInterface.setScene();
		primaryStage.setScene(myScene);
		
        long lastNanoTime = System.nanoTime();
        new AnimationTimer() {
            double elapsedTime = 0;
            public void handle(long currentNanoTime) {
                elapsedTime += (currentNanoTime - lastNanoTime) / 1000000000.0;
        		boolean update = newInterface.playAnimation(elapsedTime);
        		if (update) {
        			elapsedTime = 0;
        		}
            }
        }.start();
		primaryStage.show();
	}
	
}