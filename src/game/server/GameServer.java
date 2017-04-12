package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.Scrabble.Azione;

public class GameServer extends Thread {
	ArrayList<Character> bag = new ArrayList<>();
	char[][] board = new char[15][15];
	
	List<Parola> words = new ArrayList<>();
	
	Socket playerOne, playerTwo;
	
	boolean running;
	
	public GameServer(Socket a, Socket b) {
		playerOne = a;
		playerTwo = b;
		
		// Wait for playersß
		
		running = true;
	}
	
	@Override
	public void run() {
		try {
			Socket current = playerOne;
			while(running) {
				loop(current);
				
				//TODO: Trovare le condizioni per la fine della partita
				if(false) {
					// Invia messaggio fine a entrambi i giocatori
					
					// Chiudi le connessioni
				}
				
				// Swap players
				current = (current == playerOne ? playerTwo : playerOne);
			}
		} catch(Exception e) { e.printStackTrace(); }
	}
	
	public void loop(Socket p) throws IOException {
		ObjectInputStream objin = new ObjectInputStream(p.getInputStream());
		ObjectOutputStream objout = new ObjectOutputStream(p.getOutputStream());
		
		try {
			// Synchronize board
			objout.writeObject(board);
			
			// Ask if tiles needed
			int n = objin.readInt();
			
			// Send needed tiles
			char[] rack = new char[n];
			for(int i = 0; i < n; i++)
				rack[i] = randomTile();
			
			objout.writeObject(rack);
			
			// Await end of his turn
			// ...
			Azione action = (Azione) objin.readObject();
			String endReason = "";
			
			switch(action) {
			case ERRORE:
				endReason = "Opponent encountered an error";
			case RESA:
				endReason = "Opponent surrenderend";
				
				endMatch(endReason);
			default:
				objout.writeObject("continue");
				break;
				}
			
			// Ask board
			char[][] newBoard = (char[][])objin.readObject();
			
			// Find words
			List<Parola> newWords = findWords(newBoard);
			List<Parola> playerWords = new ArrayList<>();
			
			for(Parola word : newWords)
				if(!words.contains(word)) {
					words.add(word);
					playerWords.add(word);
				}
			
			// Swap boards
			board = newBoard;
			
			// Send points
			HashMap<String, Integer> points = new HashMap<>();
			for(Parola word : playerWords)
				points.put(word.toString(), word.points);
			
			objout.writeObject(points);
		}
		catch(ClassNotFoundException e1) { e1.printStackTrace(); }
	}
	
	private void endMatch(String reason) {
		try {
			ObjectOutputStream streamOne = new ObjectOutputStream(playerOne.getOutputStream());
			ObjectOutputStream streamTwo = new ObjectOutputStream(playerTwo.getOutputStream());
			
			streamOne.writeObject("end");
			streamTwo.writeObject("end");
			
			streamOne.writeObject(reason);
			streamTwo.writeObject(reason);
			
			streamOne.flush();
			streamTwo.flush();
			
			streamOne.close();
			streamTwo.close();
		}
		catch(IOException e) { e.printStackTrace(); }
		finally {
			try {
				playerOne.close();
				playerTwo.close();
			} catch (IOException e) {
				e.printStackTrace();
				System.exit(2);
			}
		}
		
	}
	
	public char randomTile() {
		int random = -1;
		
		// Si assicura che il numero random sia nel range accettabile (spesso il Math.random e' fuori dal range 0.0~1.0)
		while(!(random >= 0 && random < bag.size()))
			random = (int) (Math.round(Math.random() * bag.size()) - 1);
		
		return bag.remove(random);
	}
	
	public static List<Parola> findWords(char[][] board) {
		return null;
	}
}
