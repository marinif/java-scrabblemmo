package game;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import game.Scrabble.Azione;

public class GameServer extends Thread {
	ArrayList<Tassello> bag = new ArrayList<>();
	Tassello[][] board = new Tassello[15][15];
	
	List<Parola> words = new ArrayList<>();
	
	Socket playerOne, playerTwo;
	
	boolean running;
	
	public GameServer(Socket a, Socket b) {
		playerOne = a;
		playerTwo = b;
		
		// Wait for players
		
		running = true;
	}
	
	@Override
	public void run() {
		try {
			Socket current = playerOne;
			while(running) {
				loop(current);
				
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
			Tassello[] rack = new Tassello[n];
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
			Tassello[][] newBoard = (Tassello[][])objin.readObject();
			
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
			int points = 0;
			for(Parola word : playerWords)
				points += 0;
				//points += word.points;
			
			objout.writeInt(points);
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
	
	public Tassello randomTile() {
		int random = -1;
		
		// Si assicura che il numero random sia nel range accettabile (spesso il Math.random e' fuori dal range 0.0~1.0)
		while(!(random >= 0 && random < bag.size()))
			random = (int) (Math.round(Math.random() * bag.size()) - 1);
		
		return bag.remove(random);
	}
	
	public static List<Parola> findWords(Tassello[][] board) {
		return null;
	}
}
