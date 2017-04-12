package game;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

import game.Scrabble.Colore;

public class Parola {
	public final int xi;
	public final int yi;
	public final int xf;
	public final int yf;
	public int punteggio;
	public final String parola;
	public static ArrayList<String> dizionario=new ArrayList<String>();
	
	static{
		//le parole del dizionario nel file wordsita.txt vengono caricate in un array
		try{
		    Scanner wordn= new Scanner(new FileInputStream("res/wordsita.txt"));
		    
		    while(wordn.hasNext()){
		    	String s=wordn.nextLine();
		    	dizionario.add(s);
		    }
		    wordn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	    
	}
	
	public Parola(int xi,int yi,int xf,int yf,char[][] s){
		this.xi=xi;
		this.yi=yi;
		this.xf=xf;
		this.yf=yf;
		String temporanea="";
		int temporanea2 = 0;
		boolean rosso=false;
		boolean rosa=false;
		//orizzontale
		if(xi==xf){
			int tmp=yi;
			while(tmp<yf){	//scorrendo sui tasselli
				if(Scrabble.coloriCaselle[xi][tmp]== Colore.BLU)
					temporanea2 += (Scrabble.getPunteggio(s[xi][tmp])) * 3;
				else if(Scrabble.coloriCaselle[xi][tmp]== Colore.BIANCO)
					temporanea2 += (Scrabble.getPunteggio(s[xi][tmp])) * 2;
				else if(Scrabble.coloriCaselle[xi][tmp]== Colore.ROSSO)
					rosso=true;
				else if(Scrabble.coloriCaselle[xi][tmp]== Colore.ROSA)
					rosa=true;
				temporanea2 += Scrabble.getPunteggio(s[xi][tmp]);	//si calcola il punteggio
				temporanea += s[xi][tmp]; //e si concatenano le lettere per formare la parola
				tmp++;
			}
		}
		//verticale
		else if(yi==yf){
			int tmp=yi;
			while(tmp<xf){ //scorrendo sui tasselli
				if(Scrabble.coloriCaselle[tmp][yi]== Colore.BLU)
					temporanea2 += (Scrabble.getPunteggio(s[tmp][yi])) * 3;
				else if(Scrabble.coloriCaselle[tmp][yi]== Colore.BIANCO)
					temporanea2 += (Scrabble.getPunteggio(s[xi][tmp])) * 2;
				else if(Scrabble.coloriCaselle[tmp][yi]== Colore.ROSSO)
					rosso=true;
				else if(Scrabble.coloriCaselle[tmp][yi]== Colore.ROSA)
					rosa=true;
				temporanea2 += Scrabble.getPunteggio(s[tmp][yi]); //si calcola il punteggio
				temporanea += s[tmp][yi]; //e si concatenano le lettere per formare la parola
				tmp++;
			}
		}
		else temporanea2 = -1;
		
		//calcolo punteggio in base ai colori rosa e rosso
		if(rosa)
			temporanea2 *= 2;
		if(rosso)
			temporanea2 *= 3;
			
		punteggio=temporanea2 ;
		parola=temporanea;
	}
	
	@Override
	public boolean equals(Object p){
		if(p instanceof Parola){
			Parola p2 =(Parola)p;
			if(this.xi == p2.xi && this.xf == p2.xf && this.yi == p2.yi && this.yf == p2.yf)
				return true;
			else return false;
		}
		else return false;
	}
	
}
