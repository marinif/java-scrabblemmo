package game.client.gui;

import game.Scrabble;
import game.Tassello;
import javafx.scene.Node;
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
	
	// Scacchiera
	GridPane grid;
	final Scrabble game;
	PiecePane[][] pieces = new PiecePane[15][15];
	
	/** Colori delle caselle */
	private final Colore[][] coloriCaselle = new Colore[15][15];
	private static enum Colore { ROSSO, ROSA, VERDE, BIANCO, BLU };
	
	public BoardController(Scrabble game, GridPane disBoard, GridPane handGrid) {
		// ♫ We are maverick 救済なんていらない ♫
		this.game = game;
		grid = disBoard;
		
		hand = handGrid;
		
		// Inizializza i colori
		for(int x=0;x<15;x++)
			for(int y =0;y<15;y++){
				// ROSSO
				if((x==0 || x==7 || x==14) && (y==0 || y==7 || y==14) && (x != 7 && y != 7)){
					coloriCaselle[x][y] = Colore.ROSSO;
				}
				// BLU
				else if(((y==5 || y==9) && ((x-1)%4 == 0)) ||((x==5 || x==9) && ((y-1)%4 == 0)))
					coloriCaselle[x][y] = Colore.BLU;
				//BIANCO
				else if(((x==14 || x==0) && (y==3 || y==11)) || ((y==14 || y==0) && (x==3 || x==11)) || ((x==6 || x==8) &&(y==6 || y==8)) || ((x==6 || x==8) &&(y==2 || y==12)) || ((y==6 || y==8) && (x==2 || x==12)) || (x==7) && (y==3 || y==11) || (y==7) && (x==3 || x==11))
					coloriCaselle[x][y] = Colore.BIANCO;
				// ROSA
				else if((x == y) || (y == 14 - x))
					coloriCaselle[x][y] = Colore.ROSA;
				// VERDE
				else
					coloriCaselle[x][y] = Colore.VERDE;
			}
		
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 15; y++) {
				Pane pane = new AnchorPane();
				pane.setStyle(
						"-fx-background-color: " + convertiColore(coloriCaselle[x][y]) + ";" +
						"-fx-border-style: solid outside; -fx-border-width: 1; -fx-border-color: #222;"
				);
				
				grid.add(pane, x, y);
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
				
				pane.setOnDragOver(e -> {
		            e.acceptTransferModes(TransferMode.MOVE);
		            e.consume();
		        });  
				
				pane.setOnDragDropped(e -> {
					e.acceptTransferModes(TransferMode.MOVE);

					// Preleva posizione sorgente
					String pos1 = e.getDragboard().getString();
					int x1 = Integer.parseInt(pos1.substring(0, pos1.indexOf(',')));
					int y1 = Integer.parseInt(pos1.substring(pos1.indexOf(',')+1, pos1.length()));
					
					// Calcola posizione destinazione
					Position p = new Position(e, grid);
					int x2 = (int)Math.floor(p.x / 40);
					int y2 = (int)Math.floor(p.y / 40);
					
					System.out.println(x1 + "," + y1 + " -> " +x2 + "," + y2);
					
					try {
						// Gestisci il caso in cui il tassello viene aggiunto da fuori
						if(x1 >= 15 || y1 >= 15) {
							//add()
						}
						
						else if(get(x2, y2) == null) {
							move(x1, y1, x2, y2);
							e.setDropCompleted(true);
							e.consume();
						} else {
							e.setDropCompleted(true);
							e.consume();
						}
					} catch(IllegalAccessException e1) {};
						
					
					
					
				});
			}
	}

	/*
	 * 	Scacchiera
	 */
	
	private PiecePane getPiecePaneInstance(Tassello t) {
		PiecePane p = new PiecePane(t);
		p.setOnDragDetected(e -> {
			// Calcola la posizione nella griglia
			Position point = new Position(e, grid);
			int xx = (int)Math.floor(point.x / 40);
			int yy = (int)Math.floor(point.y / 40);
			System.out.println("Drag started");
			
			// Se puo' essere spostato, inserirlo nella dragboard
			//if(c != null && c.canMove()) {
				Dragboard db = p.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				//content.putString(Character.toString(c.lettera));
				content.putString(xx + "," + yy);
				db.setContent(content);
				
				
				// Imposta immagine drag
				WritableImage snap = new WritableImage(40, 40);
				SnapshotParameters prop = new SnapshotParameters();
				p.snapshot(prop, snap);
				db.setDragView(snap);
				p.setVisible(false);
				
				
				//game.removeTassello(x, y);
			//}
			
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

	public Tassello getTassello(int x, int y) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/*
	 * 	Mano
	 */
	public void addMano(Tassello t) {
		PiecePane p = getPiecePaneInstance(t);
		
		hand.getChildren().add(p);
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
