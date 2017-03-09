package game;

public class Casella {
	private char lettera;
	private String colore;	//le caselle hanno dei colori diversi per un punteggio diverso
	
	public Casella(){
		lettera=' ';
	}
	
	public void setCasella(String s){
		this.colore=s;
	}
}
