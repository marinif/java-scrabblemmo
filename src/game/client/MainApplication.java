package game.client;

import com.sun.javafx.application.LauncherImpl;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApplication extends Application {
	public static Stage primaryStage;

	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainApplication.class, args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// Inizializzazione dell'applicazione
		this.primaryStage = stage;
		stage.setTitle("Scrabble MMO"); 
		stage.setMinWidth(1024);
		stage.setMinHeight(740);
		
		BorderPane root = null;
		
		try {
			root = (BorderPane)FXMLLoader.load(getClass().getResource("game.fxml"));
		} catch(Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
		
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.show();
		
		// Immagine in riempimento
		ImageView bg = (ImageView) scene.lookup("#backgroundImage");
		Pane parent = (Pane) bg.getParent();
		bg.fitWidthProperty().bind(parent.widthProperty()); 
		bg.fitHeightProperty().bind(parent.heightProperty());
	}
}
