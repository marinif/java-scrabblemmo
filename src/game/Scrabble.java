package game;

import java.util.ArrayList;
import java.util.List;

public class Scrabble {
	public static final String VERSIONE_GIOCO = "0.0.1";
	
	
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
	
	public static enum Azione { INIZIO_MOSSA, FINE_MOSSA, PASSO, CAMBIO, RESA, FINE, ERRORE };

	/*
	 *	CALCOLO PUNTEGGIO
	 */
	
	public static int getPunteggio(char lettera) {
		switch(lettera) {
		case 'A': return 1;
		case 'B': return 5;
		case 'C': return 2;
		case 'D': return 5;
		case 'E': return 1;
		case 'F': return 5;
		case 'G': return 8;
		case 'H': return 8;
		case 'I': return 1;
		case 'L': return 3;
		case 'M': return 3;
		case 'N': return 3;
		case 'O': return 1;
		case 'P': return 5;
		case 'Q': return 10;
		case 'R': return 2;
		case 'S': return 2;
		case 'T': return 2;
		case 'U': return 3;
		case 'V': return 5;
		case 'Z': return 8;
		case ' ': return 0;
		default: return 0;
		}
	}
	
	/** @returns lista di parole trovate nella plancia
	 * @param board plancia da controllare*/
	public static List<Parola> trovaParole(char[][] board){
		ArrayList<Parola> parole = new ArrayList<>();
		
		return parole;
	}
	
	/** Verifica la correttezza delle parole 
	 * @param words parole da controllare
	 * @returns lista di parole scorrette*/
	public static List<Parola> verificaParole(List<Parola> words) {
		ArrayList<Parola> incorrectWords = new ArrayList<>();
		
		return incorrectWords;
	}
}
