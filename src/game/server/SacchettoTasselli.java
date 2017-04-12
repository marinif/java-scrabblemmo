package game.server;

import java.util.ArrayList;


public class SacchettoTasselli {
	private ArrayList<Character> sacco = new ArrayList<>();
	
	public SacchettoTasselli() {
		// Inizializza i tasselli nel sacco nelle quantita' permesse dal regolamento
			for(int i = 0; i < 14; i++)
				sacco.add('A');
			for(int i = 0; i < 3; i++)
				sacco.add('B');
			for(int i = 0; i < 6; i++)
				sacco.add('C');
			for(int i = 0; i < 3; i++)
				sacco.add('D');
			for(int i = 0; i < 11; i++)
				sacco.add('E');
			for(int i = 0; i < 3; i++)
				sacco.add('F');
			for(int i = 0; i < 2; i++)
				sacco.add('G');
			for(int i = 0; i < 2; i++)
				sacco.add('H');
			for(int i = 0; i < 12; i++)
				sacco.add('I');
			for(int i = 0; i < 1; i++)
				sacco.add('J');
			for(int i = 0; i < 1; i++)
				sacco.add('K');
			for(int i = 0; i < 5; i++)
				sacco.add('L');
			for(int i = 0; i < 5; i++)
				sacco.add('M');
			for(int i = 0; i < 5; i++)
				sacco.add('N');
			for(int i = 0; i < 15; i++)
				sacco.add('O');
			for(int i = 0; i < 3; i++)
				sacco.add('P');
			for(int i = 0; i < 1; i++)
				sacco.add('Q');
			for(int i = 0; i < 6; i++)
				sacco.add('R');
			for(int i = 0; i < 6; i++)
				sacco.add('S');
			for(int i = 0; i < 6; i++)
				sacco.add('T');
			for(int i = 0; i < 5; i++)
				sacco.add('U');
			for(int i = 0; i < 3; i++)
				sacco.add('V');
			for(int i = 0; i < 1; i++)
				sacco.add('W');
			for(int i = 0; i < 1; i++)
				sacco.add('X');
			for(int i = 0; i < 1; i++)
				sacco.add('Y');
			for(int i = 0; i < 2; i++)
				sacco.add('Z');
			for(int i = 0; i < 2; i++)
				sacco.add(' ');
	}
	
	public char pesca() {
		int random = -1;
		
		// Si assicura che il numero random sia nel range accettabile (spesso il Math.random e' fuori dal range 0.0~1.0)
		while(!(random >= 0 && random < sacco.size()))
			random = (int) (Math.round(Math.random() * sacco.size()) - 1);
		
		return sacco.remove(random);
	}
	
	public void rimetti(char c) {
		sacco.add(c);
	}
}
