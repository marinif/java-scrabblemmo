package game.client;

import java.io.IOException;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Optional;
import java.util.Scanner;

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
	
	boolean connected = false;
	public static Socket serverSocket;
	String motd = null;

	public static void main(String[] args) {
		LauncherImpl.launchApplication(MainApplication.class, args);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		// Server connection
		final TextInputDialog d1 = new TextInputDialog("localhost");
		d1.setTitle("Scrabble MMO - Partita online");
		d1.setHeaderText("Inserisci l'indirizzo del server di gioco");
		d1.setContentText("Host e porta:");
		
		Optional<String> result = d1.showAndWait();
		if (!result.isPresent())
		    System.exit(0);
		
		// Connessione al server
		String socket = result.get();
		final String host = (socket.indexOf(':') >= 0 ? socket.substring(0, socket.indexOf(':')) : "localhost");
		final int port = (socket.indexOf(':') >= 0 ? Integer.parseInt( socket.substring(socket.indexOf(':') + 1, socket.length()) ) : MatchmakerServer.DEFAULT_PORT);

		final Alert d2 = new Alert(AlertType.CONFIRMATION);
		d2.setTitle("Scrabble MMO");
		d2.setHeaderText(null);
		d2.setContentText("Connessione al server in corso, attendere...");
		ButtonType btnCancel = new ButtonType("Annulla");
		d2.getButtonTypes().setAll(btnCancel);
		
		// Thread di connessione
		Thread gameThread = new Thread() {
			
			@Override
			public void run() {
				this.setName("ConnectionThread");
				
				try {
					serverSocket = new Socket(host, port);
					
					@SuppressWarnings("resource")
					Scanner in = new Scanner(serverSocket.getInputStream());
					PrintStream out = new PrintStream(serverSocket.getOutputStream());
					
					String auth = null;
					boolean authenticated = false;
					
					do {
						Thread.sleep(100);
						
						if(in.hasNextLine()) {
							auth = in.nextLine();
							System.out.println("Received line: " + auth);
							
							switch(auth) {
							case "auth:nome?":
								out.println("bullpup");
								out.flush();
								break;
								
							case "auth:versione?":
								out.println(Scrabble.VERSIONE_GIOCO);
								out.flush();
								break;
								
							case "auth:incompatibile!":
								Platform.runLater(() -> {
									Alert d4 = new Alert(AlertType.ERROR);
									d4.setTitle("Scrabble MMO");
									d4.setHeaderText("Errore di connessione");
									d4.setContentText("Il server e' incompatibile con la versione del gioco " + Scrabble.VERSIONE_GIOCO);
									
									d4.showAndWait();
									try { serverSocket.close(); }
									catch(IOException e) { e.printStackTrace(); }
									finally { System.exit(2); }
								});
								break;
								
							case "auth:motd!":
								if(in.hasNextLine()) {
									motd = in.nextLine();
									System.out.println("MOTD: " + motd);
								}
								break;
								
							case "auth:aspetta!":
								Platform.runLater(() -> {
									//d2.close();
									
									//Alert d5 = new Alert(AlertType.INFORMATION);
									d2.setTitle("Scrabble MMO");
									d2.setHeaderText(motd);
									d2.setContentText("Connesso, in attesa di giocatori...");
									//TODO: modificando il d2 invece di aprire un altro alert, l'alert e' buggato
								});
								break;
								
							case "auth:fatto!":
								authenticated = true;
								break;
							}
						}
					} while(!authenticated);
					
					System.out.println("Partita iniziata");
					connected = true;
					
				} catch(Exception e) {
					Platform.runLater(() -> {
						e.printStackTrace();
						//d2.close();
						
						Alert d3 = new Alert(AlertType.ERROR);
						d3.setTitle("Scrabble MMO");
						d3.setHeaderText("Errore di connessione");
						d3.setContentText(e.getClass().getSimpleName() + ": " + e.getMessage());
						
						d3.showAndWait();
						System.exit(2);
					});
					
				}
				finally { 
					// Chiudi alert di attesa
					Platform.runLater(() -> { if(d2.isShowing()) d2.close(); });
					
				}
			}
		};
		
		gameThread.start();
		d2.showAndWait();
		
		if(!connected) {
			try {
				if(serverSocket.isConnected())
					serverSocket.close();
				
				Alert d6 = new Alert(AlertType.ERROR);
				d6.setHeaderText("Errore di connessione");
				d6.setContentText("Il server ha terminato la connessione");
				d6.showAndWait();
			} catch(IOException e) { e.printStackTrace(); }
			finally { System.exit(0); }
		}
		
		
		// Inizializzazione dell'applicazione
		primaryStage = stage;
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
