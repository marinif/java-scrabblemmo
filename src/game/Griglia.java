package game;

public class Griglia {
	private char[][] caselle;
	
	public Griglia(){
		for(int i =0;i<15;i++){
			for(int j =0;j<15;j++){
				caselle[i][j]=' ';
			}
		}
	}
}
