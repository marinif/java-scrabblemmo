package game.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.HashMap;
import java.util.List;

import game.Parola;
import game.Scrabble;
import game.Scrabble.Azione;
import javafx.scene.control.Alert.AlertType;

public class GameClient extends Thread {
	Socket server;
	boolean running;
	
	GameController controller;
	
	public GameClient(Socket server, GameController controller) {
		this.server = server;
		this.controller = controller;
		
		// Say hello to server
	}
	
	public void run() {
		running = true;
		
		try {
			while(running)
				loop();
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	
	public void loop() throws IOException {
		ObjectInputStream objin = new ObjectInputStream(server.getInputStream());
		ObjectOutputStream objout = new ObjectOutputStream(server.getOutputStream());
		
		try {
			// Receive board
			char[][] board = (char[][]) objin.readObject();
			controller.setBoard(board);
			
			// Ask for missing tiles
			int tiles = 7 - controller.getRackLength();
			objout.writeInt(tiles);
			
			// Receive missing tiles
			char[] rack = (char[]) objin.readObject();
			controller.addRack(rack);
			
			// Allow player to play
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
						error += " " + word.toString();
					
					controller.alert(error, AlertType.WARNING);
				}
				
				Azione action = controller.move();
				
				switch(action) {
				case ERRORE:
					break;
				case FINE_MOSSA:
					break;
				case RESA:
					break;
				case CAMBIO:
					break;
				default:
					break;
				
				}
			} while(!(incorrectWords = Scrabble.verificaParole(words)).isEmpty());
			
			// Send board
			objout.writeObject(controller.getBoard());
			
			// Ask points
			@SuppressWarnings("unchecked")
			HashMap<String, Integer> points = (HashMap<String, Integer>) objin.readObject();
			controller.addPoints(points);
			
			
		} catch(ClassNotFoundException e) { GameController.exception(e); }
	}
}
