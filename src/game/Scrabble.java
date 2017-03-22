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
	
	private Tassello[][] caselle = new Tassello[15][15];
	
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
				//BIANCO
				else if(((x==14 || x==0) && (y==3 || y==11)) || ((y==14 || y==0) && (x==3 || x==11)) || ((x==6 || x==8) &&(y==6 || y==8)))
					coloriCaselle[x][y] = Colore.BIANCO;
				// ROSA
				else if((x == y) || (y == 14 - x))
					coloriCaselle[x][y] = Colore.ROSA;
				// VERDE
				else
					coloriCaselle[x][y] = Colore.VERDE;
			}
		}
		
		
		
		// Inizializza i tasselli nel sacco nelle quantita' permesse dal regolamento
		// TODO: impostare la quantita' corretta
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('A'));
		for(int i = 0; i < 3; i++)
			sacco.add(new Tassello('B'));
		for(int i = 0; i < 6; i++)
			sacco.add(new Tassello('C'));
		for(int i = 0; i < 3; i++)
			sacco.add(new Tassello('D'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('E'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('F'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('G'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('H'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('I'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('J'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('K'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('L'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('M'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('N'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('O'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('P'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('Q'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('R'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('S'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('T'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('U'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('V'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('W'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('X'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('Y'));
		for(int i = 0; i < 14; i++)
			sacco.add(new Tassello('Z'));
		for(int i = 0; i < 2; i++)
			sacco.add(new Tassello(' '));
	}
	
	public Tassello getTassello(int x, int y) {
		return caselle[x][y];
	}
	
	public void putTassello(Tassello t, int x, int y) {
		if(caselle[x][y] == null)
			caselle[x][y] = t;
		//else
	}
	
	public void removeTassello(int x, int y) {
		caselle[x][y] = null;
	}
	
	private String convertiColore(Colore c) {
		switch(c){
		case ROSSO:
			return "#d62b3c";
		case ROSA:
			return "#dba287";
		case VERDE:
			return "#068f6e";
		case BIANCO:
			return "#a5c7d2";
		case BLU:
			return "#1c7dd8";
		default:
			return "#000000";
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
