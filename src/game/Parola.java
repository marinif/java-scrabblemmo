package game;

public class Parola {
	private int xi;
	private int yi;
	private int xf;
	private int yf;
	private int punteggio;
	
	public Parola(int xi,int yi,int xf,int yf,int punteggio){
		this.xi=xi;
		this.yi=yi;
		this.xf=xf;
		this.yf=yf;
		this.punteggio=punteggio;
	}
	public int getXi(){
		return xi;
	}
	
	public int getXf(){
		return xf;
	}
	public int getYi(){
		return yi;
	}
	public int getYf(){
		return yf;
	}
}
