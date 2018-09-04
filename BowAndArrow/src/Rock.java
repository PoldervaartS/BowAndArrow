import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class Rock extends EngineObject {

	static final int R = 70;
	static final int RANDMAX = R/3;
	
	public Rock(Point p, int width, int height) {
		super(p, 2 * R,  2 * R);
		setColor(new Color(135,12,12));
	}
	
	public Rock(Point p, int width, int height, int numSides) {
		super(p, 2 * R,  2 * R);
		setPoly(p,numSides);
		setColor(new Color(135,12,12));
	}

	public Rock(Point p, int width, int height, double velX, double velY) {
		super(p, 2 * R,  2 * R, velX, velY);
		setPoly(p, (int)(Math.random()*5) + 3);
		setColor(new Color(135,12,12));
	}
	
	@Override
	public void draw(Graphics2D g2, Point p){
		g2.setColor(getColor());
		//translate the polygon over the difference in position, then move it back. should be an easier way but idk how
		poly.translate(-1*(getX() - p.x), -1* (getY() -p.y));
		g2.fillPolygon(poly);
		poly.translate(getX() - p.x, getY() -p.y);
	}
	
	public void setPoly(int x, int y, int numSides){
		//the number of degrees difference between each point
		int degreeDif = 360/numSides;
		Point center = new Point(x + R,y + R);
		poly = new Polygon();
		for(int i = 0; i < numSides; i++){
			//randomly assign positive and negative for adding a rand amount,
			//randomly assign 0-4
			if(Math.random() <= .5){
				poly.addPoint(center.x + (int)(R * Math.cos(i * Math.toRadians(degreeDif)) + Math.random()*RANDMAX),
						center.y + (int)(R * Math.sin(i * Math.toRadians(degreeDif)) + Math.random()*RANDMAX));
			}else{
				poly.addPoint(center.x + (int)(R * Math.cos(i * Math.toRadians(degreeDif)) - Math.random()*RANDMAX),
						center.y + (int)(R * Math.sin(i * Math.toRadians(degreeDif)) - Math.random()*RANDMAX));
			}
		}
	}
	
	public void setPoly(Point p, int numSides){
		setPoly(p.x,p.y,numSides);
	}
	
	
}
