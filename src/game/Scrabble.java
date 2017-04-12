package game;

import java.util.ArrayList;
import java.util.List;

public class Scrabble {
	/** Lista contenente tutti i tasselli utilizzabili nella partita */
	private ArrayList<Character> sacco = new ArrayList<>(120);
	
	private char[][] caselle = new char[15][15];
	private boolean[][] permessiCaselle = new boolean[15][15];
	
	public static enum Azione { INIZIO_MOSSA, FINE_MOSSA, RESA, FINE, ERRORE };
	
	Giocatore playerA, playerB;
	
	public Scrabble(Giocatore a, Giocatore b) {
		playerA = a;
		playerB = b;
		
		// Inizializza i tasselli nel sacco nelle quantita' permesse dal regolamento
		for(int i = 0; i < 14; i++)
			sacco.add('A');
		for(int i = 0; i < 3; i++)
			sacco.add('B');
		for(int i = 0; i < 6; i++)
			sacco.add('C');
		for(int i = 0; i < 3; i++)
			sacco.add('D');
		for(int i = 0; i < 11; i++)
			sacco.add('E');
		for(int i = 0; i < 3; i++)
			sacco.add('F');
		for(int i = 0; i < 2; i++)
			sacco.add('G');
		for(int i = 0; i < 2; i++)
			sacco.add('H');
		for(int i = 0; i < 12; i++)
			sacco.add('I');
		for(int i = 0; i < 1; i++)
			sacco.add('J');
		for(int i = 0; i < 1; i++)
			sacco.add('K');
		for(int i = 0; i < 5; i++)
			sacco.add('L');
		for(int i = 0; i < 5; i++)
			sacco.add('M');
		for(int i = 0; i < 5; i++)
			sacco.add('N');
		for(int i = 0; i < 15; i++)
			sacco.add('O');
		for(int i = 0; i < 3; i++)
			sacco.add('P');
		for(int i = 0; i < 1; i++)
			sacco.add('Q');
		for(int i = 0; i < 6; i++)
			sacco.add('R');
		for(int i = 0; i < 6; i++)
			sacco.add('S');
		for(int i = 0; i < 6; i++)
			sacco.add('T');
		for(int i = 0; i < 5; i++)
			sacco.add('U');
		for(int i = 0; i < 3; i++)
			sacco.add('V');
		for(int i = 0; i < 1; i++)
			sacco.add('W');
		for(int i = 0; i < 1; i++)
			sacco.add('X');
		for(int i = 0; i < 1; i++)
			sacco.add('Y');
		for(int i = 0; i < 2; i++)
			sacco.add('Z');
		for(int i = 0; i < 2; i++)
			sacco.add(' ');
	}
	
	/*
	 * 	GESTIONE PARTITA
	 */
	
	public void runGame() {
		boolean running = true;
		
		while(running) {
			String motivo = null;
			
			try {
				// Permetti mossa a giocatore A
				if(motivo == null) {
					motivo = calcolaMossa(playerA);
					bloccaCaselle();
					motivo = (motivo != null ? "Giocatore B: " + motivo : null);
				}
				
				
				// Permetti mossa a giocatore B
				if(motivo == null) {
					motivo = calcolaMossa(playerB);
					bloccaCaselle();
					motivo = (motivo != null ? "Giocatore B: " + motivo : null);
				}
			}
			catch(IllegalArgumentException e) {
				motivo = "Server: errore";
				e.printStackTrace();
			}
			
			// Verifica se partita terminata
			
			
			// Comunica fine partita se necessario
			if(motivo != null) {
				playerA.finePartita(motivo);
				playerB.finePartita(motivo);
			}
		}
	};
	
	private String calcolaMossa(Giocatore g) throws IllegalArgumentException {
		String motivoFine = null;
		
		Azione azione = g.faiMossa();
		
		switch(azione) {
		case ERRORE:
			motivoFine = "Errore di comunicazione";
			break;
		case FINE_MOSSA:
			break;
		case RESA:
			motivoFine = "Mi arrendo!";
			break;
		default:
			throw new IllegalArgumentException("Azione '" + azione.toString() + "' non permessa eseguita dal giocatore A");
		}
		
		return motivoFine;
	}
	
	/*
	 *	CALCOLO PUNTEGGIO
	 */
	
	public ArrayList<Parola> trovaParole(){
		//lista di tutte le parole della griglia
		ArrayList<Parola> parole= new ArrayList<Parola>();
		
		
		
		for(int i=0;i<15;i++)
			for(int j=0;j<15;j++){
				if(this.caselle[i][j] != '\0'){ //se il tassello non è vuoto controllo se la parola c'era in precedenza
					//appena si incontra una casella non vuota si scorre sia a destra che in basso per leggere la parola
					//parola verticale
					if(this.caselle[i+1][j] != '\0' && this.caselle[i-1][j] == '\0'){
						int k = i;
						while(caselle[k][j] != '\0')
							k++;
						Parola p=new Parola(i,j,k,j,caselle);
						parole.add(p);
					}
					//parola orizzontale
					if(this.caselle[i][j+1] != '\0' && this.caselle[i][j-1] == '\0'){
						int k = j;
						while(caselle[i][k] != '\0')
							k++;
						Parola p=new Parola(i,j,i,k,caselle);
						parole.add(p);
					}
				}
			}
		return parole;
	}
	
	/** Blocca tutte le caselle con i tasselli gia' presenti */
	private void bloccaCaselle() {
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 15; y++)
				permessiCaselle[x][y] = (caselle[x][y] == '\0');
	}
	
	/** Pesca un tassello random dal sacchetto */
	public char pescaTassello() {
		int random = -1;
		
		// Si assicura che il numero random sia nel range accettabile (spesso il Math.random e' fuori dal range 0.0~1.0)
		while(!(random >= 0 && random < sacco.size()))
			random = (int) (Math.round(Math.random() * sacco.size()) - 1);
		
		return sacco.remove(random);
	}
	
	/*
	 * 	AZIONI SECONDARIE
	 */
	
	public char getTassello(int x, int y) {
		return caselle[x][y];
	}
	
	public void spostaTassello(int x1, int y1, int x2, int y2) throws IllegalAccessException {
		if(caselle[x2][y2] != '\0')
			throw new IllegalAccessException("");
		caselle[x2][y2] = caselle[x1][y1];
		caselle[x1][y1] = '\0';
		
		permessiCaselle[x2][y2] = true;
		permessiCaselle[x1][y1] = true;
	}
	
	public boolean canMove(int x, int y) {
		return permessiCaselle[x][y];
	}
	
	public void putTassello(char t, int x, int y) {
		if(caselle[x][y] == '\0')
			caselle[x][y] = t;
		
		permessiCaselle[x][y] = true;
	}
	
	public char removeTassello(int x, int y) {
		char t = caselle[x][y];
		caselle[x][y] = '\0';
		return t;
	}
		
	public void riciclaTassello(char t) {
		sacco.add(t);
	}
	
}
