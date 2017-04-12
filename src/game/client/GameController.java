package game.client;

import javafx.scene.control.Button;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import game.Parola;
import game.Scrabble;
import game.Scrabble.Azione;
import game.Scrabble.Colore;
import game.client.gui.PiecePane;
import game.client.gui.Position;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;

public class GameController implements Initializable {
	private final Object lock = new Object();
	
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
	
	/* Area scambio */
	char[] exchangeRack = new char[7];
	@FXML AnchorPane exchangeArea;
	ArrayList<PiecePane> exchangeRackPieces = new ArrayList<>(7);
	
	/* Bottoni */
	@FXML Button btnEndMove;
	@FXML Button btnSurrender;
	
	@FXML Button btnExchange;
	@FXML Button btnExchangeAll;
	
	/* Elenco parole */
	@FXML Label wordList;
	@FXML Label labelPoints;
	
	@Override
	public void initialize(URL fxmlFileLocation, ResourceBundle resources) {
		// Colori caselle sulla plancia
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
		
		// Colori caselle sul leggio
		for(int x = 0; x < 7; x++) {
			Pane pane = new AnchorPane();
			pane.setStyle(
					"-fx-background-color: #d8c991;" +
					"-fx-border-style: solid outside; -fx-border-width: 1; -fx-border-color: #222;"
			);
			
			gameRack.add(pane, x, 0);
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
		
		
		// Listener eventi plancia
		gameBoard.setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });  
		
		gameBoard.setOnDragDropped(e -> {
			e.acceptTransferModes(TransferMode.MOVE);

			// Preleva posizione sorgente
			String pos = e.getDragboard().getString();
			char origin = pos.charAt(0);
			int x1 = Integer.parseInt(pos.substring(2, pos.indexOf(',')));
			int y1 = Integer.parseInt(pos.substring(pos.indexOf(',')+1, pos.length()));
			
			// Calcola posizione destinazione
			Position p = new Position(e, gameBoard);
			int x2 = (int)Math.floor(p.x / 40);
			int y2 = (int)Math.floor(p.y / 40);
			
			System.out.println(x1 + "," + y1 + " -> " +x2 + "," + y2);
			
			if(board[x2][y2] == '\0' && boardMove[x2][y2]) {
				char c;
				
				if(origin == 'b') {
					c = board[x1][y1];
					board[x1][y1] = '\0';
					board[x2][y2] = c;
				} else if(origin == 'r') {
					c = rack[x1];
					rack[x1] = '\0';
					board[x2][y2] = c;
				} else {
					c = exchangeRack[x1];
					exchangeRack[x1] = '\0';
					board[x2][y2] = c;
				}
				
				PiecePane pp = newPiece(c);
				boardPieces.add(pp);
				gameBoard.add(pp, x2, y2);
				
				e.setDropCompleted(true);
				e.consume();
			} else {
				e.setDropCompleted(true);
				e.consume();
			}
		});
		
		// Listener per il leggio
		gameRack.setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });
		
		gameRack.setOnDragDropped(e -> {
			e.acceptTransferModes(TransferMode.MOVE);

			// Preleva posizione sorgente
			String pos = e.getDragboard().getString();
			char origin = pos.charAt(0);
			int x1 = Integer.parseInt(pos.substring(2, pos.indexOf(',')));
			int y1 = Integer.parseInt(pos.substring(pos.indexOf(',')+1, pos.length()));
			
			// Calcola posizione destinazione
			Position p = new Position(e, gameRack);
			int x2 = (int)Math.floor(p.x / 40);
			
			System.out.println(x1 + "," + y1 + " -> " +x2);
			
			try {
				if(rack[x2] == '\0') {
					char c;
					
					if(origin == 'b') {
						c = board[x1][y1];
						board[x1][y1] = '\0';
						rack[x2] = c;
					} else if(origin == 'r') {
						c = rack[x1];
						rack[x1] = '\0';
						rack[x2] = c;
					} else {
						c = exchangeRack[x1];
						exchangeRack[x1] = '\0';
						rack[x2] = c;
					}
					
					PiecePane pp = newPiece(c);
					rackPieces.add(pp);
					gameRack.add(pp, x2, 0);
					
					e.setDropCompleted(true);
					e.consume();
				} else {
					e.setDropCompleted(true);
					e.consume();
				}
			} catch(Exception e1) { e1.printStackTrace(); };
		});
		
		// Area di scambio
		exchangeArea.setOnDragOver(e -> {
            e.acceptTransferModes(TransferMode.MOVE);
            e.consume();
        });
		
		exchangeArea.setOnDragDropped(e -> {
			e.acceptTransferModes(TransferMode.MOVE);

			// Preleva posizione sorgente
			String pos = e.getDragboard().getString();
			char origin = pos.charAt(0);
			int x1 = Integer.parseInt(pos.substring(2, pos.indexOf(',')));
			int y1 = Integer.parseInt(pos.substring(pos.indexOf(',')+1, pos.length()));
			
			// Calcola posizione destinazione
			Position p = new Position(e, gameRack);
			int x2 = (int)Math.floor(p.x / 40);
			
			System.out.println(x1 + "," + y1 + " -> " +x2);
			
			try {
				if(rack[x2] == '\0') {
					char c;
					
					if(origin == 'b') {
						c = board[x1][y1];
						board[x1][y1] = '\0';
						exchangeRack[x2] = c;
					} else if(origin == 'r') {
						c = rack[x1];
						rack[x1] = '\0';
						exchangeRack[x2] = c;
					} else {
						c = exchangeRack[x1];
						exchangeRack[x1] = '\0';
						exchangeRack[x2] = c;
					}
					
					PiecePane pp = newPiece(c);
					rackPieces.add(pp);
					gameRack.add(pp, x2, 0);
					
					e.setDropCompleted(true);
					e.consume();
				} else {
					e.setDropCompleted(true);
					e.consume();
				}
			} catch(Exception e1) { e1.printStackTrace(); };
		});
		
		/*
		 * 	Listener bottoni
		 */
		btnEndMove.setOnMouseClicked(e -> {
			azione = Azione.FINE_MOSSA;
			synchronized(lock) { lock.notify(); }
		});
		
		btnSurrender.setOnMouseClicked(e -> {
			azione = Azione.RESA;
			synchronized(lock) { lock.notify(); }
		});
		
		btnExchange.setOnMouseClicked(e -> {
			azione = Azione.CAMBIO;
			synchronized(lock) { lock.notify(); }
		});
		
		System.out.println("Inzio partita...");
		
		// Thread gestore della partita
		GameClient client = new GameClient(MainApplication.serverSocket, this);
		client.start();
	}
	
	private Azione azione;
	
	public Azione move() {
		System.out.println("ciao!");
		
		azione = null;
		
		// Aggiorna plancia e leggio
		Platform.runLater(() -> {
			// Plancia
			ArrayList<PiecePane> tmp = new ArrayList<>();
			
			for(PiecePane p : boardPieces) {
				p.removeFromParent();
				tmp.add(p);
			}
			boardPieces.removeAll(tmp);
			tmp.clear();
			
			for(int x = 0; x < 15; x++)
				for(int y = 0; y < 15; y++)
					if(board[x][y] != '\0') {
						PiecePane p = newPiece(board[x][y]);
						gameBoard.add(p, x, y);
						boardPieces.add(p);
					}
			
			// Leggio
			for(PiecePane p : rackPieces) {
				p.removeFromParent();
				tmp.add(p);
			}
			rackPieces.removeAll(tmp);
			
			for(int x = 0; x < 7; x++)
				if(rack[x] != '\0') {
					PiecePane p = newPiece(rack[x]);
					gameRack.add(p, x, 0);
					rackPieces.add(p);
				}
			
			// Abilita l'interazione con la scacchiera
			blockingPane.setVisible(false);
			gameBoard.setDisable(false);
			gameRack.setDisable(false);
			// Svuota l'array di scambio
			for(int i = 0; i < 7; i++)
				exchangeRack[i] = '\0';
		});
		
		
		List<Parola> words = null;
		List<Parola> incorrectWords = null;
		do {
			if(incorrectWords != null && !incorrectWords.isEmpty()) {
				String error;
				
				if(incorrectWords.size() == 1)
					error = "La seguente parola non e' corretta: ";
				else
					error = "Le seguenti parole non sono corrette: ";
				
				for(Parola word : incorrectWords)
					error += " " + word.parola;
				
				final String error2 = error;
				
				final Object lock = new Object();
				Platform.runLater(() -> {
					alert(error2, AlertType.WARNING);
					try {
						Thread.sleep(500);
						synchronized(lock) {lock.notify();}
					}
					catch (InterruptedException e) { e.printStackTrace(); }
				});
				
				try { synchronized(lock) {
					lock.wait();
					Thread.sleep(500);
					} }
				catch(InterruptedException e) { e.printStackTrace(); }
			}
			
			try {
				// Attendi la mossa del giocatore
				synchronized(lock) { lock.wait(); }
			} catch(InterruptedException e) { e.printStackTrace(); }
			
			words = Scrabble.trovaParole(board);
			
		} while(!(incorrectWords = Scrabble.verificaParole(words)).isEmpty() && azione == Azione.FINE_MOSSA);
		
		// Disabilita l'interazione con la scacchiera
		Platform.runLater(() -> {
			// Svuota l'area di scambio
			for(PiecePane p : exchangeRackPieces)
				p.removeFromParent();
			
			gameBoard.setDisable(true);
			gameRack.setDisable(true);
			blockingPane.setVisible(true);
			
			synchronized(lock) { lock.notify(); }
		});
		
		try {
			synchronized(lock) { lock.wait(); }
		} catch(InterruptedException e) { e.printStackTrace(); }
		
		return azione;
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
	
	public char[] getExchangeRack() {
		return exchangeRack;
	}
	
	public int getExchangeRackCount() {
		return exchangeRackPieces.size();
	}
	
	/*
	 * 	Interfacciamento GUI
	 */
	
	public PiecePane newPiece(char lettera) {
		PiecePane p = new PiecePane(lettera);
		
		p.setOnDragDetected(e -> {
			// Trova l'origine del tassello
			//	'b' per la plancia
			//	'r' per il leggio
			char origin = (gameBoard.getChildren().contains(p) ? 'b' : 'r');
			
			// Calcola la posizione nella plancia
			Position point;
			
			if(origin == 'b')
				point = new Position(e, gameBoard);
			else
				point = new Position(e, gameRack);
			
			int xx = (int)Math.floor(point.x / 40);
			int yy = (int)Math.floor(point.y / 40);
			System.out.println("Drag started");
			
			// Se puo' essere spostato, inserirlo nella dragboard
			if(origin == 'r' || (origin == 'b' && boardMove[xx][yy])) {
				Dragboard db = p.startDragAndDrop(TransferMode.MOVE);
				ClipboardContent content = new ClipboardContent();
				content.putString(origin + ":" + xx + "," + yy);
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
			
			p.removeFromParent();
			boardPieces.remove(p);
			rackPieces.remove(p);
			
			e.consume();
		});
		
		return p;
	}
	
	public void addPoints(HashMap<String, Integer> points) {
		String all = wordList.getText();
		int total = 0;
		
		for(String word : points.keySet()) {
			all += "\n" + word + " - " + points.get(word) + " pt.";
			total += points.get(word);
		}
		
		wordList.setText(all);
		labelPoints.setText("Punti: " + total);
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
