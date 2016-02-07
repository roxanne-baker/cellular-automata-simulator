import javafx.application.Application;
import javafx.scene.Scene;
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
		primaryStage.show();
	}
	
}