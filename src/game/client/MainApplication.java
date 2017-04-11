package game.client;

import java.util.Optional;

import com.sun.javafx.application.LauncherImpl;

import game.Scrabble;
import game.server.MatchmakerServer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;

public class MainApplication extends Application {
	public static Stage primaryStage;
	Scrabble game;

	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainApplication.class, args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// Dialog
		TextInputDialog dialog = new TextInputDialog("localhost");
		dialog.setTitle("Scrabble MMO");
		dialog.setHeaderText("Look, a Text Input Dialog");
		dialog.setContentText("Host e porta:");

		// Traditional way to get the response value.
		Optional<String> result = dialog.showAndWait();
		if (!result.isPresent())
		    System.exit(0);
		
		// Connessione al server
		String socket = result.get();
		String host = "localhost";
		int port = MatchmakerServer.DEFAULT_PORT;
		
		if(socket.indexOf(':')) {
		String host = socket.substring(0, socket.indexOf(':'));
		int port = Integer.parseInt( socket.substring(socket.indexOf(':') + 1, socket.length()) );
		}
		
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
