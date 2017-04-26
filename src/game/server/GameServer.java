package game.server;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
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
	
	public int nTurniVuoti; // conteggio dei turni a vuoto
	
	public GameServer(Giocatore a, Giocatore b) {
		playerOne = a;
		playerTwo = b;
		nTurniVuoti=0;
		
		running = true;
	}
	
	
	@Override
	public void run() {
		// Come per regolamento, viene aggiunto un tassello iniziale
		board[7][7] = bag.pesca();
		
		// Inizia gioco
		System.out.println("Inizia il gioco! " + playerOne.nome + " VS " + playerTwo.nome);
		
		// Loop gioco
		try {
			Giocatore current = playerOne;
			
			while(running) {
				loop(current);
				
				//conto quanti turni vengono fatti senza mettere parole sulla plancia
				//condizioni per la fine della partita
				if(bag.isEmpty() || nTurniVuoti == 6) {	//manca la condizione sul tempo scaduto
					// Invia messaggio fine a entrambi i giocatori con i punteggi e il vincitore
					Giocatore vincitore = (playerOne.punteggio > playerTwo.punteggio ? playerOne :
											(playerOne.punteggio == playerTwo.punteggio ? null : playerTwo)
											);
					
					String messaggio;
					if(vincitore == null)
						messaggio = "Fine partita, pareggio!";
					else
						messaggio = "Fine partita, ha vinto '" + vincitore.nome + "' con " + vincitore.punteggio + " punti";
					
					// Chiudi le connessioni
					endMatch(messaggio);
					running = false;
				}
				
				// Swap players
				current = (current == playerOne ? playerTwo : playerOne);
			}
		} catch(IOException e) {
			e.printStackTrace();
			
			System.out.println("Disconnessione giocatori in corso...");
			playerOne.disconnetti(e.getMessage());
			playerTwo.disconnetti(e.getMessage());
		}
	}
	
	public void loop(Giocatore p) throws IOException {
		Socket sock = p.socket;
		
		ObjectInputStream objin = new ObjectInputStream(sock.getInputStream());
		ObjectOutputStream objout = new ObjectOutputStream(sock.getOutputStream());
		objout.flush();
		System.out.print(p.nome + ": connessione ObjectStream aperta");
		
		try {
			objout.writeUTF("continue");
			
			// Synchronize board
			objout.writeObject(board);
			objout.flush();
			
			// Ask if tiles needed
			int n = objin.readInt();
			
			// Send needed tiles
			char[] rack = new char[n];
			for(int i = 0; i < n; i++)
				rack[i] = bag.pesca();
			
			objout.writeObject(rack);
			objout.flush();
			
			// Await end of his turn
			// ...
			Azione action = (Azione) objin.readObject();
			String endReason = "";
			
			char[][] newBoard;
			List<Parola> newWords; 
			List<Parola> playerWords;
			
			switch(action) {
			case ERRORE:
				endReason = "Il giocatore '" + p.nome + "' ha riscontrato un errore";
			case RESA:
				endReason = "Il giocatore '" + p.nome + "' si e' arreso";
				endMatch(endReason);
				break;
				
			case CAMBIO:
				nTurniVuoti++; //conto i turni che vanno a vuoto
				
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
				n = Math.min(n, bag.rimasti());
				rack = new char[n];
				for(int i = 0; i < n; i++)
					rack[i] = bag.pesca();
				
				objout.writeObject(rack);
				objout.flush();
				break;
			case FINE_MOSSA:
				newBoard = (char[][])objin.readObject();
				newWords = Scrabble.trovaParole(newBoard);
				playerWords= new ArrayList<>();
				
				if(playerWords.isEmpty())
					nTurniVuoti++; 	//conto i turni che vanno a vuoto
				else
					nTurniVuoti=0;	//si riazzerano i turni che vanno a vuoto
				
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
					points.put(word.parola, word.punteggio);
					p.punteggio += word.punteggio;
				}
				
				objout.writeObject(points);
				objout.flush();
				break;
			default:
				//objout.writeObject("continue");
				break;
			}
		}
		catch(ClassNotFoundException e1) { e1.printStackTrace(); }
	}
	
	private void endMatch(String reason) {
		running = false;
		
		playerOne.disconnetti(reason);
		playerTwo.disconnetti(reason);
	}
}
