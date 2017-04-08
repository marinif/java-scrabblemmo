package game;

import java.util.List;

import game.Scrabble.Azione;

public abstract class Giocatore {
	public final String nome;
	
	public Giocatore(String nome) {
		this.nome = nome;
	}
	
	/*
	 * 	Azioni riguardanti il gioco
	 */
	
	/** Comunica al giocatore l'inizio della partita */
	public abstract void inizioPartita();
	
	/** Permetti al giocatore di effettuare una mossa 
	 * @returns Azione causata dalla mossa del giocatore */
	public abstract Azione faiMossa();
	
	/** Aggiunge tasselli alla mano del giocatore
	 * @param tasselli la lista dei tasselli da dare al giocatore */
	public abstract void daiTasselli(List<Tassello> tasselli);
	
	/** Comunica al giocatore i punti guadagnati dalla sua mossa
	 * @param punti il numero di punti da attribuire all'ultima mossa del giocatore */
	public abstract void valutazioneMossa(int punti);
	
	/** Comunica ai giocatori la fine della partita
	 * @param motivo il motivo della fine della partita */
	public abstract void finePartita(String motivo);
	
	/* 
	 * 	Getter/setter
	 */
}
