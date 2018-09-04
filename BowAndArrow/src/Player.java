import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;

public class Player extends EngineObject{
	final int MAXHEALTH = 1000; final static int PLAYERHT = 300; static final int PLAYERWT = 40; static final int HEADDIAMETER = 40;
	int theta,health;
	LastShot lS;
	private long shotT;
	
	public Player(Point p){
		super(p, PLAYERWT, PLAYERHT);
		setHealth(MAXHEALTH);
		setShotT(0);
		Polygon poly = new Polygon();
		poly.addPoint(getX(), getY());
		poly.addPoint(getX() + PLAYERWT/3, getY());
		poly.addPoint(getX() + PLAYERWT/3, getY() + PLAYERHT/2 + HEADDIAMETER);
		poly.addPoint(getX(), getY() + PLAYERHT/2 + HEADDIAMETER);
		setPoly(poly);
	}
	
	@Override
	public void draw(Graphics2D g2, Point p){
		g2.setColor(getColor());
		g2.fillOval(p.x, p.y, HEADDIAMETER, HEADDIAMETER);
		g2.fillOval(p.x + PLAYERWT/3, p.y + HEADDIAMETER , PLAYERWT/3, PLAYERHT/2);
		
		//drawing the last shot
		if(lS != null){
			g2.setColor(Color.GRAY);
			Point m1 = lS.getM1();
			Point m2 = lS.getM2();
			g2.drawLine(p.x + m1.x, p.y + m1.y, p.x + m2.x, p.y + m2.y);
			g2.drawString("@ " + lS.getTheta() * -1 + " degrees", p.x + m1.x, p.y + m1.y);
		}
	}
	
	public int getHealth(){
		return health;
	}
	
	public void setHealth(int health){
		this.health = health;
	}
	
	public void setLastShot(Point m1, Point m2, double theta){
		lS = new LastShot(new Point(m1.x - getX(), m1.y - getY()),new Point(m2.x - getX(), m2.y - getY()),theta);
	}
	
	public void setLastShot(Object o){
		lS = (LastShot)o;
	}

	public long getShotT() {
		return shotT;
	}

	public void setShotT(long shotT) {
		this.shotT = shotT;
	}
	
}
