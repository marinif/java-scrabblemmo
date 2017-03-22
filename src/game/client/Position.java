package game.client;

import javafx.scene.input.*;
import javafx.geometry.Bounds;
import javafx.scene.*;

public class Position {
	public double x, y;
	
	/** Position relative to window */
	public Position(MouseEvent e) {
		x = e.getScreenX() - MainApplication.primaryStage.getX();
		y = e.getScreenY() - MainApplication.primaryStage.getY();
	}

	/** Position relative to Node */
	public Position(MouseEvent e, Node n) {
		Bounds bounds = n.localToScreen(n.getBoundsInLocal());
		x = e.getScreenX() - bounds.getMinX();
		y = e.getScreenY() - bounds.getMinY();
	}
	
	/** Position relative to Node */
	public Position(DragEvent e, Node n) {
		Bounds bounds = n.localToScreen(n.getBoundsInLocal());
		x = e.getScreenX() - bounds.getMinX();
		y = e.getScreenY() - bounds.getMinY();
	}
}
