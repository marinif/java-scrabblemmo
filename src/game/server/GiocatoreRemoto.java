package game.server;

import java.net.Socket;
import java.util.List;

import game.Giocatore;
import game.Scrabble.Azione;
import game.Tassello;

public class GiocatoreRemoto extends Giocatore {
	private final Socket sock;
	
	public GiocatoreRemoto(String nome, Socket socket) {
		super(nome);
		this.sock = socket;
	}

	@Override
	public void inizioPartita() {
		// TODO Auto-generated method stub
	}

	@Override
	public Azione faiMossa() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void daiTasselli(List<Tassello> tasselli) {
		// TODO Auto-generated method stub
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
