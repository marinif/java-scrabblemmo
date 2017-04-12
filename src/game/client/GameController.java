package game.client;

import javafx.scene.control.Button;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import game.Scrabble;
import game.Scrabble.Azione;
import game.Scrabble.Colore;
import game.client.gui.PiecePane;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class GameController implements Initializable {
	
	@FXML AnchorPane blockingPane;
	
	/* Plancia */
	char[][] board = new char[15][15];
	boolean[][] boardMove = new boolean[15][15];
	@FXML GridPane gameBoard;
	ArrayList<PiecePane> boardPieces = new ArrayList<>(15*15);
	
	/* Leggio */
	char[] rack = new char[7];
	@FXML GridPane gameRack;
	ArrayList<PiecePane> rackPieces = new ArrayList<>(7);
	
	/* Bottoni */
	@FXML Button btnEndMove;
	@FXML Button btnSurrender;
	
	/* Elenco parole */
	@FXML Label labelPoints;
	
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 15; y++) {
				Pane pane = new AnchorPane();
				pane.setStyle(
						"-fx-background-color: " + convertiColore(Scrabble.coloriCaselle[x][y]) + ";" +
						"-fx-border-style: solid outside; -fx-border-width: 1; -fx-border-color: #222;"
				);
				
				gameBoard.add(pane, x, y);
				pane.applyCss();
				pane.setOpacity(0.8);
				
				pane.setOnDragEntered(e -> {
					pane.setOpacity(1.0);
					e.consume();
				});
				
				pane.setOnDragExited(e -> {
					pane.setOpacity(0.8);
					e.consume();
				});
				
			}
		
		System.out.println("Inzio partita...");
		
		// Thread gestore della partita
		GameClient client = new GameClient(MainApplication.serverSocket, this);
		client.start();
	}
	
	public Azione move() {
		// Aggiorna plancia e leggio
		Platform.runLater(() -> {
			// Plancia
			for(PiecePane p : boardPieces)
				p.removeFromParent();
			for(int x = 0; x < 15; x++)
				for(int y = 0; y < 15; y++)
					if(board[x][y] != '\0') {
						PiecePane p = new PiecePane(board[x][y]);
						gameBoard.add(p, x, y);
						boardPieces.add(p);
					}
			
			// Leggio
			for(PiecePane p : rackPieces)
				p.removeFromParent();
			for(int x = 0; x < 7; x++)
				if(rack[x] != '\0') {
					PiecePane p = new PiecePane(rack[x]);
					gameRack.add(p, x, 0);
					rackPieces.add(p);
				}
			
			// Abilita l'interazione con la scacchiera
			blockingPane.setVisible(false);
			gameBoard.setDisable(false);
			gameRack.setDisable(false);
		});
		
		try {
			// Attendi la mossa del giocatore
			this.wait();
		} catch(InterruptedException e) { e.printStackTrace(); }
		
		// Disabilita l'interazione con la scacchiera
		Platform.runLater(() -> {
			gameBoard.setDisable(true);
			gameRack.setDisable(true);
			blockingPane.setVisible(true);
		});
		
		return null;
	}

	/*
	 *  Gestione plancia
	 */
	protected void setBoard(char[][] board) {
		this.board = board;
		
		// Imposta permessi spostamento
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 15; y++)
				boardMove[x][y] = (board[x][y] == '\0');
	}

	protected char[][] getBoard() {
		return board;
	}
	
	/*
	 * 	Gestione leggio
	 */

	public void addRack(char[] rack) {
		int c = 0;
		for(int i = 0; i < 7; i++)
			if(this.rack[i] == '\0') {
				char lettera = rack[c++];
				
				
				this.rack[i] = lettera;
			}
	}

	public int getRackLength() {
		int n = 0;
		
		for(int i = 0; i < 7; i++)
			if(this.rack[i] != '\0')
				n++;
				
		return n;
	}
	
	
	/*
	 * 	Interfacciamento GUI
	 */
	
	public void addPoints(HashMap<String, Integer> points) {
		String total = labelPoints.getText();
		
		for(String word : points.keySet())
			total += "\n" + word + " - " + points.get(word) + " pt.";
		
		labelPoints.setText(total);
	}

	public void alert(String message, AlertType type) {
		Alert alert = new Alert(type);
		alert.setTitle("Attenzione!");
		alert.setHeaderText(null);
		alert.setContentText(message);

		alert.showAndWait();
	}

	public static void exception(Exception ex) {
		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Java exception");
		alert.setHeaderText(ex.getClass().getSimpleName());
		alert.setContentText(ex.getMessage());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		ex.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("Stack trace:");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		alert.showAndWait();
	}
	

	private static String convertiColore(Colore c) {
		switch(c){
		case ROSSO:
			return "#d62b3c";
		case ROSA:
			return "#dba287";
		case VERDE:
			return "#068f6e";
		case BIANCO:
			return "#a5c7d2";
		case BLU:
			return "#1c7dd8";
		default:
			return "#000000";
		}
	}
}
