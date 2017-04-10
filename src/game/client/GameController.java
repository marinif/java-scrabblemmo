package game.client;

import java.net.URL;
import java.util.ResourceBundle;

import game.Tassello;
import game.client.gui.BoardController;
import game.client.gui.PiecePane;
import game.client.gui.Position;
import game.Parola;
import game.Scrabble;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.input.TransferMode;

public class GameController implements Initializable {
	// Elementi JavaFX
	@FXML GridPane gameBoard;
	@FXML GridPane letterArray;
	
	@FXML Button btnRandom;
	@FXML Button btnFind;
	
	@FXML AnchorPane trashBin;
	@FXML Label wordList;
	
	// Meccaniche di gioco
	Scrabble game;
	BoardController board;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("ciao");
		
		// Imposta la griglia
		game = new Scrabble(new GiocatoreLocale("lul1"), new GiocatoreLocale("lul2"));
		board = new BoardController(game, gameBoard, letterArray);
		
		
		// DEBUG (commentare via in production)
		Tassello piece = new Tassello('T');
		board.add(piece, 4, 4);
		
		btnRandom.setOnMouseClicked(e -> {
			board.addMano(game.pescaTassello());
			e.consume();
		});
		
		btnFind.setOnMouseClicked(e -> {
			String words = "";
			
			for(Parola p : game.trovaParole())
				words += p + "\n";
			
			wordList.setText(words);
		});
		
		trashBin.setOnDragOver(e -> {
			e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
		});
		
		trashBin.setOnDragDropped(e -> {
			e.acceptTransferModes(TransferMode.MOVE);

			// Preleva posizione sorgente
			String pos = e.getDragboard().getString();
			char origin = pos.charAt(0);
			int x1 = Integer.parseInt(pos.substring(2, pos.indexOf(',')));
			int y1 = Integer.parseInt(pos.substring(pos.indexOf(',')+1, pos.length()));
			
			try {
				if(origin == 'g') {
					game.riciclaTassello(board.remove(x1, y1));
				}
				else {
					// Ottieni e rimuovi il tassello dal leggio
					PiecePane pp = board.handPieces[x1];
					board.handPieces[x1] = null;
					pp.removeFromParent();
					
					game.riciclaTassello(pp.piece);
				}
				
				e.setDropCompleted(true);
				e.consume();
			} catch(Exception e1) { e1.printStackTrace(); };
		});
	}
}
