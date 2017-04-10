package game.client.gui;

import game.Scrabble;
import game.Scrabble.Colore;
import game.Tassello;
import javafx.scene.SnapshotParameters;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class BoardController {
	// Mano
	GridPane hand;
	public PiecePane[] handPieces = new PiecePane[7];
	
	// Scacchiera
	GridPane grid;
	final Scrabble game;
	PiecePane[][] pieces = new PiecePane[15][15];
	
	
	
	public BoardController(Scrabble game, GridPane disBoard, GridPane handGrid) {
		// ♫ We are maverick 救済なんていらない ♫
		this.game = game;
		grid = disBoard;
		
		hand = handGrid;
		
		// Viene aggiunto un Pane colorato per ogni casella della plancia
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 15; y++) {
				Pane pane = new AnchorPane();
				pane.setStyle(
						"-fx-background-color: " + convertiColore(Scrabble.coloriCaselle[x][y]) + ";" +
						"-fx-border-style: solid outside; -fx-border-width: 1; -fx-border-color: #222;"
				);
				
				grid.add(pane, x, y);
				pane.applyCss();
				pane.setOpacity(0.8);
				
				// Effetto illuminazione
				
				pane.setOnDragEntered(e -> {
					pane.setOpacity(1.0);
					e.consume();
				});
				
				pane.setOnDragExited(e -> {
					pane.setOpacity(0.8);
					e.consume();
				});
			}
		
		// Drag'n'drop di tasselli sulla plancia
		
		grid.setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });  
		
		grid.setOnDragDropped(e -> {
			
		});
		
		// Drag'n'drop di tasselli sul leggio
		
		hand.setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });  
		
		hand.setOnDragDropped(e -> {
			
		});
	}

	/*
	 * 	Plancia
	 */
	
	private PiecePane getPiecePaneInstance(Tassello t) {
		PiecePane p = new PiecePane(t);
		p.setOnDragDetected(e -> {
			// Trova l'origine del tassello
			//	'g' per la griglia di gioco
			//	'h' per la
			char origin = (grid.getChildren().contains(p) ? 'g' : 'h');
			
			
			// Calcola la posizione nella plancia
			Position point;
			
			if(origin == 'g')
				point = new Position(e, grid);
			else
				point = new Position(e, hand);
			
			int xx = (int)Math.floor(point.x / 40);
			int yy = (int)Math.floor(point.y / 40);
			System.out.println("Drag started");
			
			// Se puo' essere spostato, inserirlo nella dragboard
			if(origin == 'h' || (origin == 'g' && game.canMove(xx, yy))) {
				
				Dragboard db = p.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.put(Tassello.OBJECT_FORMAT, p);
				db.setContent(content);
				
				
				// Imposta immagine drag
				WritableImage snap = new WritableImage(40, 40);
				SnapshotParameters prop = new SnapshotParameters();
				p.snapshot(prop, snap);
				db.setDragView(snap);
				p.setVisible(false);
			}
			
			e.consume();
		});
		
		p.setOnDragDone(e -> {
			System.out.println("Drag done");
			p.setVisible(true);
			e.consume();
		});
		
		return p;
	}
	
	public void add(Tassello t, int x, int y) {
		game.putTassello(t, x, y);
		
		PiecePane p = getPiecePaneInstance(t);
		pieces[x][y] = p;
		grid.add(p, x, y);
	}

	public PiecePane get(int x, int y) {
		return pieces[x][y];
	}

	public void move(int x1, int y1, int x2, int y2) throws IllegalAccessException {
		game.spostaTassello(x1, y1, x2, y2);
		
		PiecePane p = pieces[x1][y1];
		
		grid.getChildren().remove(p);
		pieces[x1][y1] = null;
		
		grid.add(p, x2, y2);
		pieces[x2][y2] = p;		
	}
	
	public Tassello remove(int x, int y) {
		grid.getChildren().remove(pieces[x][y]);
		return game.removeTassello(x, y);
	}
	
	/*
	 * 	Leggio
	 */
	public boolean addMano(Tassello t) {
		PiecePane p = getPiecePaneInstance(t);
		
		for(int i = 0; i < 7; i++) {
			if(handPieces[i] == null) {
				handPieces[i] = p;
				hand.add(p, i, 0);
				
				return true;
			}
		}
		
		game.riciclaTassello(t);
		return false;
	}
	
	
	/*
	 * Utils
	 */
	
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
