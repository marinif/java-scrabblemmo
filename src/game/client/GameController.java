package game.client;

import java.net.URL;
import java.util.ResourceBundle;

import game.Tassello;
import game.client.gui.BoardController;
import game.client.gui.PiecePane;
import game.client.gui.Position;
import game.Scrabble;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.GridPane;
import javafx.scene.input.*;

public class GameController implements Initializable {
	// Elementi JavaFX
	@FXML GridPane gameBoard;
	@FXML GridPane letterArray;
	
	// Meccaniche di gioco
	Scrabble game;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		System.out.println("ciao");
		
		// Imposta la griglia
		game = new Scrabble();
		game.setGriglia(gameBoard);
		
		PiecePane piece = new PiecePane(new Tassello('T'));
		gameBoard.add(piece, 4, 4);
		
		/*
		 * 	Mouse listeners
		 */
		
		// Per tutte le caselle
		
		
		// Draggato da
		gameBoard.setOnDragDetected(e -> {
			// Calcola la posizione nella griglia
			Position p = new Position(e, gameBoard);
			int x = (int)Math.floor(p.x / 40);
			int y = (int)Math.floor(p.y / 40);
			System.out.print(x + ", " + y);
			
			// Preleva il pezzo
			Tassello c = game.getTassello(x, y);
			// Se puo' essere spostato, inserirlo nella dragboard
			//if(c != null && c.canMove()) {
				Dragboard db = gameBoard.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				//content.putString(Character.toString(c.lettera));
				content.putString("ciao");
				
				db.setContent(content);
				game.removeTassello(x, y);
			//}
			
			e.consume();
		});
		
		// Draggato su
		gameBoard.setOnDragOver(e -> {
			System.out.println("Dragged over");
			e.consume();
		});
		
		gameBoard.setOnDragDropped(e -> {
			// Calcola la posizione nella griglia
			Position p = new Position(e, gameBoard);
			int x = (int)Math.floor(p.x / 40);
			int y = (int)Math.floor(p.y / 40);
			System.out.println(" -> " +x + ", " + y);
			
			e.getDragboard().getContent(null);
			e.getDragboard().getString();
			
			e.consume();
		});
	}

}
