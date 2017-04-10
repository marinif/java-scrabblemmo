package game.client;

import java.util.List;

import game.Giocatore;
import game.Scrabble.Azione;
import game.Tassello;
import game.client.gui.PiecePane;

public class GiocatoreLocale extends Giocatore {
	
	private Tassello[] rack = new Tassello[7];
	
	
	/*
	 * Controllo giocatore
	 */
	
	
	/*
	 * Controllo gioco
	 */

	public GiocatoreLocale(String nome) {
		super(nome);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void inizioPartita() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Azione faiMossa(Tassello[][] plancia, Tassello[] leggio) {
		rack = leggio;
		return null;
	}
	
	public Tassello[] richiediTasselli() {
		return rack;
	}

	@Override
	public void valutazioneMossa(int punti) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void finePartita(String motivo) {
		// TODO Auto-generated method stub
		
	}

}
