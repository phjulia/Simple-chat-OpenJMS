package zad1;
	
import java.io.IOException;
import java.net.URL;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;

public class Main extends Application {
	private static Scene theScene;
	private static Stage stage;
	
	@Override
	public void start(Stage primaryStage) {
		stage=primaryStage;
        primaryStage.setTitle("Chat");
        FXMLLoader loader = new FXMLLoader();
        try {
        loader.setLocation(new URL("file:/Users/rachel_green/eclipse-workspace/TPO4_LY_S19516/src/zad1/Login.fxml"));
        ChatController controller = new ChatController();
        loader.setController(controller);
        Pane pane = loader.<Pane>load();

        theScene = new Scene(pane);
        setScene(theScene, primaryStage);
        primaryStage.setScene(theScene);
        primaryStage.show();

        }catch( IOException e) {
        	e.printStackTrace();
        	
        }
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	public static void setScene(Scene scene, Stage primaryStage) {
    	theScene=scene;
    	 stage.setScene(theScene);
    	 stage.show();
    }
	public static Stage getStage() {
    	return stage;
    }
    public static Scene getScene() {
    	return theScene;
    }
}
