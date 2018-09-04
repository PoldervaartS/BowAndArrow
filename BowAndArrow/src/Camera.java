import java.awt.Graphics2D;
import java.awt.Point;

public class Camera {
	
	int canvasW, canvasH;
	Point p;
	private EngineObject lockedOn = null;
	
	//TENATIVE CAMERA SPEED IDK WHAT TO PUT IT AT
	final int camSpeed = 20;
	
	public Camera(int x, int y,int canvasW, int canvasH){
		this.canvasW = canvasW;
		this.canvasH = canvasH;
		p = new Point(x,y);
	}
	
	public Camera(Point point, int canvasW, int canvasH){
		p.x = point.x;
		p.y = point.y;
		this.canvasW = canvasW;
		this.canvasH = canvasH;
	}
	
	public void moveCamera(Point point){
		if(lockedOn == null){
			if(Math.abs(point.x - p.x) <= camSpeed){
				p.x = point.x;
			}else if (point.x < p.x){
				p.x = p.x- camSpeed;
			}else{
				p.x = p.x + camSpeed;
			}
			if(Math.abs(point.y - p.y) <= camSpeed){
				p.y = point.y;
			}else if (point.y < p.y){
				p.y = p.y- camSpeed;
			}else{
				p.y = p.y + camSpeed;
			}
		}else{
			p.x = lockedOn.getX() + lockedOn.getWidth()/2 - canvasW/2;
			p.y = lockedOn.getY() + lockedOn.getHeight()/2 - canvasH/2;
		}
	}
	
	public boolean inCameraView(EngineObject o){
		return Math.abs(o.getX() - p.x) <= (canvasW + o.getWidth()) && Math.abs(o.getY() - p.y) <= (canvasH + o.getHeight());
		//extra draw on the sides by the objects width so if it does stick in it will draw some of it
		//Buffer a little bit
	}
	
	//returns position of the object relative to the camera 
	public Point getObjPos(EngineObject o){
		return new Point(o.getX()-getX(), o.getY()-getY());
	}
	
	public void drawObject(Graphics2D g2,EngineObject o){
		o.draw(g2, new Point(o.getX() - p.x, o.getY() - p.y));
		//give point where the object needs to be drawn then call the objects draw function
	}
	
	public Point getP(){
		return p;
	}
	
	public void setP(Point p){
		this.p = p;
	}
	
	public int getX(){
		return p.x;
	}
	
	public int getY(){
		return p.y;
	}
	
	public EngineObject getLockedOn() {
		return lockedOn;
	}

	public void setX(int x){
		p.x = x;
	}
	
	public void setY(int y){ 
		p.y = y;
	}

	public void setLockedOn(EngineObject lockedOn) {
		this.lockedOn = lockedOn;
	}
	
	
}
