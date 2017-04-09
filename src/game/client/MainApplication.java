package game.client;

import java.util.Optional;

import com.sun.javafx.application.LauncherImpl;

import game.Scrabble;
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
	private Scrabble game;

	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainApplication.class, args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		/*
		// Dialog
		Alert a1 = new Alert(AlertType.CONFIRMATION);
		a1.setTitle("Scrabble MMO");
		a1.setHeaderText("Inizio partita");
		a1.setContentText("In quale modalita' vorresti giocare?");
		
		ButtonType btnOnline = new ButtonType("Online");
		ButtonType btnLocale = new ButtonType("Locale");
		ButtonType btnEsci = new ButtonType("Esci");
		a1.getButtonTypes().setAll(btnOnline, btnLocale, btnEsci);
		
		
		Optional<ButtonType> r1 = a1.showAndWait();
		if(r1.get() == btnOnline) { alertOnline(); }
		else if(r1.get() == btnLocale) { alertLocale(); }
		else { Platform.exit(); return; }
		*/
		
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
	

	private void alertLocale() {
		String n1, n2;
		
		TextInputDialog d1 = new TextInputDialog();
		d1.setTitle("Scrabble MMO - partita locale");
		d1.setHeaderText("Giocatore 1");
		d1.setContentText("Inserisci il tuo nome:");
		Optional<String> r1 = d1.showAndWait();
		if(!r1.isPresent())
			Platform.exit();
		else
			n1 = r1.get();
		
		TextInputDialog d2 = new TextInputDialog();
		d2.setTitle("Scrabble MMO - partita locale");
		d2.setHeaderText("Giocatore 1");
		d2.setContentText("Inserisci il tuo nome:");
		Optional<String> r2 = d2.showAndWait();
		if(!r2.isPresent())
			Platform.exit();
		else
			n2 = r2.get();
	}

	private void alertOnline() {
		// TODO Auto-generated method stub
		
	}
}
