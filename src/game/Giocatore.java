package game;

public class Giocatore {
	private char[] mano= new char[7]; 		//salvo in un vettore di caratteri le 7 lettere estratte a caso per ogni giocatore
	
	public Giocatore(char[] m){
		for(int i =0;i<7;i++){
			mano[i]=m[i];
		}
	}
}
