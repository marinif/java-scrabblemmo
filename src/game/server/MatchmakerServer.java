package game.server;

import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

import game.Scrabble;

public abstract class MatchmakerServer {
	public static final int DEFAULT_PORT = 4010;
	public static final int MAX_PARTITE = 100;
	
	static ArrayList<Thread> partite = new ArrayList<>();
	public static boolean running;
	
	public static void main(String[] args) throws IOException {
		System.out.println("Scrabble MMO v" + Scrabble.VERSIONE_GIOCO);
		System.out.println("Server di matchmaking, avvio...");
		
		
		int porta = DEFAULT_PORT;
		
		// Trova parametro porta, altrimenti usa quella default
		for(int i = 0; i < args.length; i++)
			if(args[i].equals("-p") && i < (args.length - 1)) {
				porta = Integer.parseInt(args[i+1]);
				break;
			}
		
		// Apri il server
		ServerSocket server = new ServerSocket(porta);
		server.setSoTimeout(1000);
		System.out.println("Server in ascolto sulla porta " + porta + "\n");
		
		// Apri thread di matchmaking
		MatchmakingThread mm = new MatchmakingThread();
		
		// Attendi giocatori
		running = true;
		mm.start();
		
		while(running) {
			try {
				Socket s = server.accept();
				mm.aggiungiConnessione(s);
				
			} catch(SocketTimeoutException e) {}
		}
		
		server.close();
	}
	
	private static class MatchmakingThread extends Thread {
		private volatile ArrayList<Giocatore> players = new ArrayList<>();
		
		public void aggiungiConnessione(Socket s) throws IOException {
			@SuppressWarnings("resource")
			Scanner in = new Scanner(s.getInputStream());
			PrintWriter out = new PrintWriter(s.getOutputStream());
			
			out.println("auth:versione?"); out.flush();
			if(in.hasNextLine() && !in.nextLine().equals(Scrabble.VERSIONE_GIOCO)) {
				out.println("auth:incompatibile!"); out.flush();
				
				out.close();
				in.close();
				s.close();
				return;
			}
			
			// Richiedi nome
			out.println("auth:nome?"); out.flush();
			String nome = null;
			if(in.hasNextLine()) nome = in.nextLine();
			
			// Invia MotD
			out.println("auth:motd!");
			out.println("Benvenuti nel server di Kerber e Marini!");
			out.flush();
			
			// Attendi partita
			out.println("auth:aspetta!"); out.flush();
			players.add(new Giocatore(nome, s));
			
			System.out.println("Nuovo giocatore in attesa: " + nome);
			
			// Verifica se ci sono giocatori sconnessi
			for(Giocatore g : players)
				if(!g.socket.isConnected())
					players.remove(g);
			
			// Notifica il thread di matchmaking
			synchronized(this) { notify(); }
		}
		
		@Override
		public void run() {
			while(running)
				try {
					// Attendi nuove connessioni
					synchronized(this) { this.wait(); }
					
					System.out.println("Thread risvegliato");
					if(players.size() >= 2) {
						System.out.println("Trovati due giocatori");
						
						final Giocatore a = players.remove(0);
						final Giocatore b = players.remove(0);
						
						// Verifica se i giocatori sono ancora connessi
						boolean connectedA = a.socket.isConnected();
						boolean connectedB = b.socket.isConnected();
						
						if(!connectedA || !connectedB) {
							if(connectedA) {
								players.add(a);
								System.out.println("Giocatore " + a.nome + " sconnesso");
							} else if(connectedB) {
								players.add(b);
								System.out.println("Giocatore " + a.nome + " sconnesso");
							}
							
							continue;
						}
						
						Thread partita = new Thread() {
							@Override
							public void run() {
								// Notifica i due giocatori
								try {
									PrintStream printA = new PrintStream(a.socket.getOutputStream());
									PrintStream printB = new PrintStream(b.socket.getOutputStream());
									
									printA.println("auth:fatto!");
									printB.println("auth:fatto!");
								} catch(IOException e) { e.printStackTrace(); }
								
								GameServer game = new GameServer(a, b);
								game.start();
							}
						};
						
						partite.add(partita);
						partita.start();
					}
				} catch (InterruptedException e) {
					running = false;
				}
			}
	}
}
