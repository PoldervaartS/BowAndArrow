import java.awt.Graphics2D;
import java.awt.Point;

public class Wall extends EngineObject {

	public Wall(Point p, int width, int height) {
		super(p, width, height);
	}
	
	@Override
	public void draw(Graphics2D g2, Point p){
		g2.setColor(c);
		g2.fillRect(p.x, p.y, getWidth(), getHeight());
	}

}
