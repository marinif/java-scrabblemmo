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
		case 'A':
			return 1;
		case 'B':
			return 5;
		case 'C':
			return 2;
		case 'D':
			return 5;
		case 'E':
			return 1;
		case 'F':
			return 5;
		case 'G':
			return 8;
		case 'H':
			return 8;
		case 'I':
			return 1;
		case 'L':
			return 3;
		case 'M':
			return 3;
		case 'N':
			return 3;
		case 'O':
			return 1;
		case 'P':
			return 5;
		case 'Q':
			return 10;
		case 'R':
			return 2;
		case 'S':
			return 2;
		case 'T':
			return 2;
		case 'U':
			return 3;
		case 'V':
			return 5;
		case 'Z':
			return 8;
		case ' ':
			return 0;
		default:
			return 0;
		}
	}
}
