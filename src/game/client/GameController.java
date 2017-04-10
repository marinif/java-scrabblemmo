package game.client;

import java.net.URL;
import java.util.ResourceBundle;

import game.Tassello;
import game.client.gui.BoardController;
import game.Scrabble;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

public class GameController implements Initializable {
	// Elementi JavaFX
	@FXML GridPane gameBoard;
	@FXML GridPane letterArray;
	
	@FXML Button btnRandom;
	
	// Meccaniche di gioco
	Scrabble game;
	BoardController board;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("ciao");
		
		// Imposta la griglia
		game = new Scrabble(new GiocatoreLocale("lul1"), new GiocatoreLocale("lul2"));
		board = new BoardController(game, gameBoard, letterArray);
		
		Tassello piece = new Tassello('T');
		board.add(piece, 4, 4);
		
		btnRandom.setOnMouseClicked(e -> {
			board.addMano(game.pescaTassello());
			e.consume();
		});
	}
}
