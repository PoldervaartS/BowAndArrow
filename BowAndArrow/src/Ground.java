import java.awt.Point;

public class Ground extends EngineObject {

	public Ground(Point p, int width, int height) {
		super(p, width, height);
	}

	public Ground(Point p, int width, int height, double velX, double velY) {
		super(p, width, height, velX, velY);
	}

	
}
