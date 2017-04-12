package game;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Scrabble {
	public static final String VERSIONE_GIOCO = "0.0.1";
	public static ArrayList<String> dizionario=new ArrayList<String>();
	
	public static enum Colore { ROSSO, ROSA, VERDE, BIANCO, BLU };
	
	/** Colori delle caselle */
	public static final Colore[][] coloriCaselle = new Colore[15][15];
	private static char[][] caselle = new char[15][15];
	
	// Inizializza i colori
	static {
		
		
		//le parole del dizionario nel file wordsita.txt vengono caricate in un array
		try{
		    Scanner wordn= new Scanner(new FileInputStream("res/wordsita.txt"));
		    
		    while(wordn.hasNext()){
		    	String s=wordn.nextLine();
		    	dizionario.add(s.toUpperCase());
		    }
		    wordn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
		
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
	
	public static enum Azione { INIZIO_MOSSA, FINE_MOSSA, CAMBIO, RESA, FINE, ERRORE };

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
	public static ArrayList<Parola> trovaParole(char[][] board){
		//lista di tutte le parole della griglia
				ArrayList<Parola> parole= new ArrayList<Parola>();
				
				for(int i=0;i<15;i++)
					for(int j=0;j<15;j++){
						if(board[i][j] != '\0'){ //se il tassello non è vuoto controllo se la parola c'era in precedenza
							//appena si incontra una casella non vuota si scorre sia a destra che in basso per leggere la parola
							//parola verticale
							if(board[i+1][j] != '\0' && board[i-1][j] == '\0'){
								int k = i;
								while(board[k][j] != '\0')
									k++;
								Parola p=new Parola(i,j,k,j,board);
								parole.add(p);
							}
							//parola orizzontale
							if(board[i][j+1] != '\0' && board[i][j-1] == '\0'){
								int k = j;
								while(board[i][k] != '\0')
									k++;
								Parola p=new Parola(i,j,i,k,board);
								parole.add(p);
							}
						}
					}
				return parole;
	}
	
	/** Verifica la correttezza delle parole 
	 * @param words parole da controllare
	 * @returns lista di parole scorrette*/
	public static List<Parola> verificaParole(List<Parola> words) {
		ArrayList<Parola> incorrectWords = new ArrayList<>();
		
		for(Parola p : words)
			if(!dizionario.contains(p.parola))
				incorrectWords.add(p);
		
		return incorrectWords;
	}
}
