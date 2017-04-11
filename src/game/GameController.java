package game;

import java.awt.Button;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.HashMap;

import game.Scrabble.Azione;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

public class GameController {
	
	/* Plancia */
	@FXML GridPane gameBoard;
	char[][] board = new char[15][15];
	
	/* Leggio */
	@FXML GridPane gameRack;
	char[] rack = new char[7];
	
	/* Bottoni */
	@FXML Button btnEndMove;
	@FXML Button btnSurrender;
	
	/* Elenco parole */
	@FXML Label labelPoints;
	
	public GameController() {
		
	}
	
	public Azione move() {
		// Abilita l'interazione con la scacchiera
		gameBoard.setDisable(false);
		gameRack.setDisable(false);
		
		try {
			// Attendi la mossa del giocatore
			this.wait();
		} catch(InterruptedException e) { e.printStackTrace(); }
		
		// Disabilita l'interazione con la scacchiera
		gameBoard.setDisable(true);
		gameRack.setDisable(true);
		return null;
	}

	/*
	 *  Gestione plancia
	 */
	public void setBoard(char[][] board) {
		// TODO Auto-generated method stub
		
	}

	public Object getBoard() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 	Gestione leggio
	 */

	public void addRack(char[] rack) {
		// TODO Auto-generated method stub
		
	}

	public int getRackLength() {
		// TODO Auto-generated method stub
		return 0;
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

	public void exception(Exception ex) {
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
	

}
