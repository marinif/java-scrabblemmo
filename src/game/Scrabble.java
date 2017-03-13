package game;

public class Scrabble {
	private static enum Colore { ROSSO, ROSA, VERDE, BIANCO, BLU };
	private final Colore[][] coloriCaselle = new Colore[15][15];
	
	private char[][] caselle;
	
	public Scrabble(){
		for(int x=0;x<15;x++){
			for(int y =0;y<15;y++){
				// ROSSO
				if((x==0 || x==7 || x==14) && (y==0 || y==7 || y==14) && (x != 7 && y != 7)){
					coloriCaselle[x][y] = Colore.ROSSO;
				}
				// ROSA
				else if((x == y) && (y == 14 - x))
					coloriCaselle[x][y] = Colore.ROSA;
				// BLU
				else if(((y==5 || y==9) && ((x-1)%4 == 0)) ||((x==5 || x==9) && ((y-1)%4 == 0)))
					coloriCaselle[x][y] = Colore.BLU;
				//BIANCO
				else if()
					coloriCaselle[x][y] = Colore.BIANCO;
				// VERDE
				else
					coloriCaselle[x][y] = Colore.VERDE;
			}
		}
	}
}
