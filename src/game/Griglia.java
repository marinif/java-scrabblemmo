package game;

public class Griglia {
	private Casella[][] caselle;
	
	public Griglia(){
		for(int i =0;i<15;i++){
			for(int j =0;j<15;j++){
				caselle[i][j]=new Casella();
				if((i==0 || i==7 || i==14) && (j==0 || j==7 || j==14) && (i != 7 && j != 7)){
					caselle[i][j].setCasella("ROSSO");
				}
				else if(i == j)
					caselle[i][j].setCasella("GIALLO");
				else
					caselle[i][j].setCasella("VERDE");
				
			}
		}
		//PRIMA RIGA
		caselle[0][3].setCasella("BIANCO");
		caselle[0][11].setCasella("BIANCO");
		//SECONDA RIGA
		caselle[1][5].setCasella("BLU");
		caselle[1][9].setCasella("BLU");
		caselle[1][13].setCasella("GIALLO");
		//TERZA RIGA
		caselle[2][6].setCasella("BIANCO");
		caselle[2][8].setCasella("BIANCO");
		caselle[2][12].setCasella("GIALLO");
		//QUARTA RIGA
		caselle[3][0].setCasella("BIANCO");
		caselle[3][7].setCasella("BIANCO");
		caselle[3][11].setCasella("GIALLO");
		caselle[3][14].setCasella("BIANCO");
		//QUINTA RIGA
		
		
		
		
	}
}
