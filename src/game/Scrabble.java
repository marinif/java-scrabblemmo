package game;

import java.util.ArrayList;
import java.util.List;

public class Scrabble {
	public static enum Colore { ROSSO, ROSA, VERDE, BIANCO, BLU };
	
	/** Colori delle caselle */
	public static final Colore[][] coloriCaselle = new Colore[15][15];
	
	// Inizializza i colori
	static {
		for(int x=0;x<15;x++)
			for(int y =0;y<15;y++)
				// ROSSO
				if((x==0 || x==7 || x==14) && (y==0 || y==7 || y==14) && (x != 7 && y != 7))
					coloriCaselle[x][y] = Colore.ROSSO;
				// BLU
				else if(((y==5 || y==9) && ((x-1)%4 == 0)) ||((x==5 || x==9) && ((y-1)%4 == 0)))
					coloriCaselle[x][y] = Colore.BLU;
				//BIANCO
				else if(((x==14 || x==0) && (y==3 || y==11)) || ((y==14 || y==0) && (x==3 || x==11)) || ((x==6 || x==8) &&(y==6 || y==8)) || ((x==6 || x==8) &&(y==2 || y==12)) || ((y==6 || y==8) && (x==2 || x==12)) || (x==7) && (y==3 || y==11) || (y==7) && (x==3 || x==11))
					coloriCaselle[x][y] = Colore.BIANCO;
				// ROSA
				else if((x == y) || (y == 14 - x))
					coloriCaselle[x][y] = Colore.ROSA;
				// VERDE
				else
					coloriCaselle[x][y] = Colore.VERDE;
	}
	
	/** Lista contenente tutti i tasselli utilizzabili nella partita */
	private ArrayList<Tassello> sacco = new ArrayList<>(120);
	
	private Tassello[][] caselle = new Tassello[15][15];
	private boolean[][] permessiCaselle = new boolean[15][15];
	
	public static enum Azione { INIZIO_MOSSA, FINE_MOSSA, RESA, FINE, ERRORE };
	
	Giocatore playerA, playerB;
	
	public Scrabble(Giocatore a, Giocatore b) {
		playerA = a;
		playerB = b;
		
		// Inizializza i tasselli nel sacco nelle quantita' permesse dal regolamento
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('A'));
		for(int i = 0; i < 3; i++)
			sacco.add(new Tassello('B'));
		for(int i = 0; i < 6; i++)
			sacco.add(new Tassello('C'));
		for(int i = 0; i < 3; i++)
			sacco.add(new Tassello('D'));
		for(int i = 0; i < 11; i++)
			sacco.add(new Tassello('E'));
		for(int i = 0; i < 3; i++)
			sacco.add(new Tassello('F'));
		for(int i = 0; i < 2; i++)
			sacco.add(new Tassello('G'));
		for(int i = 0; i < 2; i++)
			sacco.add(new Tassello('H'));
		for(int i = 0; i < 12; i++)
			sacco.add(new Tassello('I'));
		for(int i = 0; i < 1; i++)
			sacco.add(new Tassello('J'));
		for(int i = 0; i < 1; i++)
			sacco.add(new Tassello('K'));
		for(int i = 0; i < 5; i++)
			sacco.add(new Tassello('L'));
		for(int i = 0; i < 5; i++)
			sacco.add(new Tassello('M'));
		for(int i = 0; i < 5; i++)
			sacco.add(new Tassello('N'));
		for(int i = 0; i < 15; i++)
			sacco.add(new Tassello('O'));
		for(int i = 0; i < 3; i++)
			sacco.add(new Tassello('P'));
		for(int i = 0; i < 1; i++)
			sacco.add(new Tassello('Q'));
		for(int i = 0; i < 6; i++)
			sacco.add(new Tassello('R'));
		for(int i = 0; i < 6; i++)
			sacco.add(new Tassello('S'));
		for(int i = 0; i < 6; i++)
			sacco.add(new Tassello('T'));
		for(int i = 0; i < 5; i++)
			sacco.add(new Tassello('U'));
		for(int i = 0; i < 3; i++)
			sacco.add(new Tassello('V'));
		for(int i = 0; i < 1; i++)
			sacco.add(new Tassello('W'));
		for(int i = 0; i < 1; i++)
			sacco.add(new Tassello('X'));
		for(int i = 0; i < 1; i++)
			sacco.add(new Tassello('Y'));
		for(int i = 0; i < 2; i++)
			sacco.add(new Tassello('Z'));
		for(int i = 0; i < 2; i++)
			sacco.add(new Tassello(' '));
	}

	/*
	 *	CALCOLO PUNTEGGIO
	 */
	
	public static List<Parola> findWords(char[][] board){
		ArrayList<Parola> parole = new ArrayList<>();
		
		return parole;
	}
	
	public static List<Parola> checkWords(List<Parola> words) {
		ArrayList<Parola> incorrectWords = new ArrayList<>();
		
		return incorrectWords;
	}
	
	/** Blocca tutte le caselle con i tasselli gia' presenti */
	private void bloccaCaselle() {
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 15; y++)
				permessiCaselle[x][y] = (caselle[x][y] == null);
	}
	
	/** Pesca un tassello random dal sacchetto */
	public Tassello pescaTassello() {
		int random = -1;
		
		// Si assicura che il numero random sia nel range accettabile (spesso il Math.random e' fuori dal range 0.0~1.0)
		while(!(random >= 0 && random < sacco.size()))
			random = (int) (Math.round(Math.random() * sacco.size()) - 1);
		
		return sacco.remove(random);
	}
	
	/*
	 * 	AZIONI SECONDARIE
	 */
	
	public Tassello getTassello(int x, int y) {
		return caselle[x][y];
	}
	
	public void spostaTassello(int x1, int y1, int x2, int y2) throws IllegalAccessException {
		if(caselle[x2][y2] != null)
			throw new IllegalAccessException("");
		caselle[x2][y2] = caselle[x1][y1];
		caselle[x1][y1] = null;
		
		permessiCaselle[x2][y2] = true;
		permessiCaselle[x1][y1] = true;
	}
	
	public boolean canMove(int x, int y) {
		return permessiCaselle[x][y];
	}
	
	public void putTassello(Tassello t, int x, int y) {
		if(caselle[x][y] == null)
			caselle[x][y] = t;
		
		permessiCaselle[x][y] = true;
	}
	
	public Tassello removeTassello(int x, int y) {
		Tassello t = caselle[x][y];
		caselle[x][y] = null;
		return t;
	}
	
	public void riciclaTassello(Tassello t) {
		sacco.add(t);
	}
	
}
