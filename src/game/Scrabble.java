package game;

import java.util.Scanner;
import java.io.*;
import java.util.ArrayList;

public class Scrabble {
	/** Lista contenente tutti i tasselli utilizzabili nella partita */
	private ArrayList<Tassello> sacco = new ArrayList<>(120);
	private Tassello[][] caselle = new Tassello[15][15];
	
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
					motivo = (motivo != null ? "Giocatore B: " + motivo : null);
				}
				// Permetti mossa a giocatore B
				if(motivo == null) {
					motivo = calcolaMossa(playerB);
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
	}
	
	public void putTassello(Tassello t, int x, int y) {
		if(caselle[x][y] == null)
			caselle[x][y] = t;
		//else
	}
	
	public void removeTassello(int x, int y) {
		caselle[x][y] = null;
	}
	
}
