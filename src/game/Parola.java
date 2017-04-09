package game;

public class Parola {
	public final int xi;
	public final int yi;
	public final int xf;
	public final int yf;
	private int punteggio;
	
	public Parola(int xi,int yi,int xf,int yf,Scrabble s){
		this.xi=xi;
		this.yi=yi;
		this.xf=xf;
		this.yf=yf;
		//orizzontale
		if(xi==xf){
			int tmp=yi;
			while(tmp<yf){
				this.punteggio += s.getTassello(xi,tmp).getPunteggio();
				tmp++;
			}
		}
		//verticale
		else if(yi==yf){
			int tmp=xi;
			while(tmp<xf){
				punteggio += s.getTassello(tmp,yi).getPunteggio();
				tmp++;
			}
		}
		else punteggio = -1;
	}
	
}
