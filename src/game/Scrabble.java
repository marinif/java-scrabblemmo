package game;

import java.util.ArrayList;

import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;

public class Scrabble {
	/** Lista contenente tutti i tasselli utilizzabili nella partita */
	private ArrayList<Tassello> sacco = new ArrayList<>(120);
	
	/** Colori delle caselle */
	private final Colore[][] coloriCaselle = new Colore[15][15];
	private static enum Colore { ROSSO, ROSA, VERDE, BIANCO, BLU };
	
	private Tassello[][] caselle;
	
	public Scrabble() {
		// Inizializza i colori
		for(int x=0;x<15;x++){
			for(int y =0;y<15;y++){
				// ROSSO
				if((x==0 || x==7 || x==14) && (y==0 || y==7 || y==14) && (x != 7 && y != 7)){
					coloriCaselle[x][y] = Colore.ROSSO;
				}
				// BLU
				else if(((y==5 || y==9) && ((x-1)%4 == 0)) ||((x==5 || x==9) && ((y-1)%4 == 0)))
					coloriCaselle[x][y] = Colore.BLU;
				// ROSA
				else if((x == y) || (y == 14 - x))
					coloriCaselle[x][y] = Colore.ROSA;
				//BIANCO
				// TODO: placeholder, da inserire posizioni prefissate
				else if(false)
					coloriCaselle[x][y] = Colore.BIANCO;
				// VERDE
				else
					coloriCaselle[x][y] = Colore.VERDE;
			}
		}
	public void setGriglia(GridPane grid) {
		for(int x = 0; x < 15; x++)
			for(int y = 0; y < 15; y++) {
				Pane pane = new AnchorPane();
				pane.setStyle(
						"-fx-background-color: " + convertiColore(coloriCaselle[x][y]) + ";" +
						"-fx-border-style: solid outside; -fx-border-width: 1; -fx-border-color: #222;"
				);
				
				grid.add(pane, x, y);
				pane.applyCss();
			}	
	}
}
