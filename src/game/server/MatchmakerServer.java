package game.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.Scanner;

import game.GameServer;
import game.Giocatore;
import game.Scrabble;

public abstract class MatchmakerServer {
	public static final String VERSIONE_GIOCO = "0.0.1";
	public static final int DEFAULT_PORT = 4010;
	public static final int MAX_PARTITE = 100;
	
	static ArrayList<Thread> partite;
	public static boolean running;
	
	public static void main(String[] args) throws IOException {
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
		
		// Apri thread di matchmaking
		MatchmakingThread mm = new MatchmakingThread();
		
		// Attendi giocatori
		while(running) {
			try {
				Socket s = server.accept();
				mm.aggiungiConnessione(s);
				
			} catch(SocketTimeoutException e) {}
		}
		
	}
	
	private static class MatchmakingThread extends Thread {
		private ArrayList<Giocatore> players;
		
		public void aggiungiConnessione(Socket s) throws IOException {
			PrintWriter out = new PrintWriter(s.getOutputStream());
			Scanner in = new Scanner(s.getInputStream());
			
			// Verifica versione
			out.println("auth:versione?"); out.flush();
			if(in.nextLine() != VERSIONE_GIOCO) {
				out.println("auth:incompatibile!"); out.flush();
				
				out.close();
				in.close();
				s.close();
				return;
			}
			
			// Richiedi nome
			out.println("auth:nome?"); out.flush();
			String nome = in.nextLine();
			
			// Attendi partita
			out.println("auth:aspetta!"); out.flush();
			players.add(new GiocatoreRemoto(nome, s));
			
			this.notify();
		}
		
		@Override
		public void run() {
			while(running)
				try {
					// Attendi nuove connessioni
					this.wait();
					
					if(players.size() >= 2) {
						final Giocatore a = players.remove(0);
						final Giocatore b = players.remove(0);
						
						partite.add(new Thread() {
							@Override
							public void run() {
								GameServer game = new GameServer(a, b);
								game.run();
							}
						});
					}
						
					
				} catch (InterruptedException e) {
					running = false;
				}
			}
	}
}
