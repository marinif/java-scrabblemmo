package game.server;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Giocatore {
	public final String nome;
	public final Socket socket;
	
	public int punteggio = 0;
	public boolean cambioSecco = false;
	
	public Giocatore(String nome, Socket socket) {
		this.nome = nome;
		this.socket = socket;
		
	}
	
	public boolean disconnetti(String motivo) {
		try {
			if(!socket.isConnected()) return true;
			
			ObjectOutputStream streamOne = new ObjectOutputStream(socket.getOutputStream());
			
			streamOne.writeObject("end");
			streamOne.writeObject(motivo);
			
			streamOne.flush();
			streamOne.close();
			
			return true;
		} catch(IOException e) {
			e.printStackTrace();
			return false;
		}
	}
}
