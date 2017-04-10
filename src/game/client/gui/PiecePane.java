package game.client.gui;

import java.io.File;

import game.Tassello;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;

public class PiecePane extends StackPane {
	private static Image bgImage;
	
	static {
		File f = new File("res/piece.png");
		System.out.println(f.getAbsolutePath());
		bgImage = new Image(f.toURI().toString());
	}
	
	public PiecePane(Tassello t) {
		try {
		ImageView bg = new ImageView(bgImage);
		bg.setFitWidth(40);
		bg.setFitHeight(40);
		this.getChildren().add(bg);
		
		Label letterLabel = new Label(Character.toString(t.lettera));
		letterLabel.setFont(Font.font("System", 20));
		this.getChildren().add(letterLabel);
		
		AnchorPane pointPane = new AnchorPane();
		Label pointLabel = new Label(Integer.toString(t.getPunteggio()));
		pointLabel.setFont(Font.font("System", 10));
		pointPane.getChildren().add(pointLabel);
		AnchorPane.setRightAnchor(pointLabel, 4.0);
		AnchorPane.setBottomAnchor(pointLabel, 2.0);
		this.getChildren().add(pointPane);
		}
		catch(Exception e) { e.printStackTrace(); }
	}
}
