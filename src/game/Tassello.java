package game;

public class Tassello {
	public final char lettera;
	private boolean moveable;
	
	public Tassello(char lettera) {
		this.lettera = lettera;
	}
	
	public boolean canMove() {
		return moveable;
	}
	
	public int getPunteggio() {
		switch(lettera) {
		// TODO: inserire tutti i punteggi corrispondenti a lettere
		default:
			return 0;
		}
	}
}
