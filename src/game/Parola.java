package game;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Scanner;

public class Parola {
	public final int xi;
	public final int yi;
	public final int xf;
	public final int yf;
	private int punteggio;
	public final String parola;
	
	static{
		//le parole del dizionario nel file wordsita.txt vengono caricate in un array
		try{
		    Scanner wordn= new Scanner(new FileInputStream("res/wordsita.txt"));
		    
		    while(wordn.hasNext()){
		    	String s=wordn.nextLine();
		    	ArrayList<String> lista= new ArrayList<String>();
		    	lista.add(s);
		    }
		    wordn.close();
		}catch(Exception e){
			e.printStackTrace();
		}
	    
	}
	
	public Parola(int xi,int yi,int xf,int yf,Scrabble s){
		this.xi=xi;
		this.yi=yi;
		this.xf=xf;
		this.yf=yf;
		String temporanea="";
		//orizzontale
		if(xi==xf){
			int tmp=yi;
			while(tmp<yf){	//scorrendo sui tasselli
				this.punteggio += s.getTassello(xi,tmp).getPunteggio();	//si calcola il punteggio
				temporanea += s.getTassello(xi,tmp).lettera; //e si concatenano le lettere per formare la parola
				tmp++;
			}
		}
		//verticale
		else if(yi==yf){
			int tmp=xi;
			while(tmp<xf){ //scorrendo sui tasselli
				punteggio += s.getTassello(tmp,yi).getPunteggio(); //si calcola il punteggio
				temporanea += s.getTassello(tmp,yi).lettera; //e si concatenano le lettere per formare la parola
				tmp++;
			}
		}
		else punteggio = -1;
		
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
