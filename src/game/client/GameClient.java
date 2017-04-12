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
import javafx.application.Platform;
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
		ObjectOutputStream objout = new ObjectOutputStream(server.getOutputStream());
		objout.flush();
		
		ObjectInputStream objin = new ObjectInputStream(server.getInputStream());
		
		try {
			// Receive status
			String status = objin.readUTF();
			
			switch(status) {
			case "continue":
				break;
			case "end":
				final Object lock = new Object();
				final String motivo = objin.readUTF();
				
				
				Platform.runLater(() -> {
					controller.alert("Partita terminata: " + motivo, AlertType.INFORMATION);
					try {
						Thread.sleep(500);
						synchronized(lock) {lock.notify();}
					}
					catch (InterruptedException e) { e.printStackTrace(); }
				});
				
				try { synchronized(lock) {lock.wait();} }
				catch(InterruptedException e) { e.printStackTrace(); }
				
				running = false;
				server.close();
				Platform.exit();
				return;
			}
			
			// Receive board
			char[][] board = (char[][]) objin.readObject();
			controller.setBoard(board);
			
			// Ask for missing tiles
			int tiles = 7 - controller.getRackLength();
			objout.writeInt(tiles);
			objout.flush();
			
			// Receive missing tiles
			char[] rack = (char[]) objin.readObject();
			controller.addRack(rack);
			
			// Allow player to play
			Azione action = controller.move();
			
			objout.writeObject(action);
			objout.flush();
			
			switch(action) {
			case ERRORE:
				break;
			case FINE_MOSSA:
				// Send board
				objout.writeObject(controller.getBoard());
				objout.flush();
				
				// Ask points
				@SuppressWarnings("unchecked")
				HashMap<String, Integer> points = (HashMap<String, Integer>) objin.readObject();
				controller.addPoints(points);
				break;
			case RESA:
				break;
			case CAMBIO:
				objout.writeInt(controller.getExchangeRackCount());
				objout.writeObject(controller.getExchangeRack());
				objout.flush();
				
				controller.addRack((char[]) objin.readObject());
				break;
			default:
				break;
			}
			
			
		} catch(ClassNotFoundException e) { GameController.exception(e); }
	}
}
