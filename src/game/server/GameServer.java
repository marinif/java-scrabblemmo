package game.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import game.Parola;
import game.Scrabble;
import game.Scrabble.Azione;

public class GameServer extends Thread {
	/** Sacchetto dei tasselli dal quale pescare */
	SacchettoTasselli bag = new SacchettoTasselli();
	
	/** Parole che sono state utilizzate durante la partita*/
	List<Parola> words = new ArrayList<>();
	
	/** Plancia */
	char[][] board = new char[15][15];
	
	/** Oggetto giocatore */
	Giocatore playerOne, playerTwo;
	
	/** Flag che indica se la partita e' ancora in corso */
	boolean running;
	
	public GameServer(Giocatore a, Giocatore b) {
		playerOne = a;
		playerTwo = b;
		
		// Wait for playersß
		
		running = true;
	}
	
	
	@Override
	public void run() {
		try {
			Giocatore current = playerOne;
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
	
	public void loop(Giocatore p) throws IOException {
		Socket sock = p.socket;
		ObjectInputStream objin = new ObjectInputStream(sock.getInputStream());
		ObjectOutputStream objout = new ObjectOutputStream(sock.getOutputStream());
		
		try {
			// Synchronize board
			objout.writeObject(board);
			
			// Ask if tiles needed
			int n = objin.readInt();
			
			// Send needed tiles
			char[] rack = new char[n];
			for(int i = 0; i < n; i++)
				rack[i] = bag.pesca();
			
			objout.writeObject(rack);
			
			// Await end of his turn
			// ...
			Azione action = (Azione) objin.readObject();
			String endReason = "";
			
			switch(action) {
			case ERRORE:
				endReason = "Il giocatore '" + p.nome + "' ha riscontrato un errore";
			case RESA:
				endReason = "Il giocatore '" + p.nome + "' si e' arreso";
				endMatch(endReason);
				break;
				
			case CAMBIO:
				n = objin.readInt();
				if(n == 7)
					if(!p.cambioSecco)
						p.cambioSecco = true;
					else
						p.disconnetti("Rilevato uso di trucchi! Vietato compiere il cambio secco piu' di una volta per giocatore");
				
				// Rimetti i tasselli cambiati nel sacchetto
				rack = (char[]) objin.readObject();
				for(char c : rack)
					bag.rimetti(c);
				// Pescane altri
				rack = new char[n];
				for(int i = 0; i < n; i++)
					rack[i] = bag.pesca();
				
				objout.writeObject(rack); objout.flush();
					
				
			default:
				//objout.writeObject("continue");
				break;
				}
			
			// Ask board
			char[][] newBoard = (char[][])objin.readObject();
			
			// Find words
			List<Parola> newWords = Scrabble.trovaParole(newBoard);
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
			for(Parola word : playerWords) {
				points.put(word.toString(), word.punteggio);
				p.punteggio += word.punteggio;
			}
			
			objout.writeObject(points);
		}
		catch(ClassNotFoundException e1) { e1.printStackTrace(); }
	}
	
	private void endMatch(String reason) {
		running = false;
		
		try {
			playerOne.disconnetti(reason);
			playerTwo.disconnetti(reason);
		}
		catch(IOException e) { e.printStackTrace(); }
		
	}
}
