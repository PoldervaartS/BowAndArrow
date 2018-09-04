import java.awt.Graphics2D;
import java.awt.Point;

public class Arrow extends EngineObject{
	
	static final int width = 80, height = width/2;
	final String iLoc = "src/Arrow.png";

	
	public Arrow(Point p) {
		super(p, width, height);
		setTheta(0);
		setImage(iLoc);
	}
	
	public Arrow(Point p, double theta){
		super(p,width,height);
		setTheta(theta);
		setImage(iLoc);
	}
	
	public Arrow(Point p,double velX, double velY){
		super(p,width,height);
		setVelX(velX);
		setVelY(velY);
		setImage(iLoc);
	}
	
	@Override
	public void draw(Graphics2D g2, Point p){
		try{
			g2.rotate(Math.toRadians(getTheta()), p.x + getWidth()/2, p.y + getHeight()/2);
			g2.drawImage(getImage(), p.x, p.y, null);
			g2.rotate(-Math.toRadians(getTheta()), p.x + getWidth()/2, p.y + getHeight()/2);
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
	
	
	@Override
	public void setVelX(double x){
		super.setVelX(x);
		//needed so that the angle has 360 degree range
		if(getVelX() > 0){
			setTheta(Math.toDegrees(Math.atan(getVelY()/getVelX())));
		}else if(getVelX() == 0){
			if(getVelY() > 0){
				setTheta(90);
			}else if(getVelY() < 0){
				setTheta(-90);
			}else{
				setTheta(0);
			}
		}else{
			setTheta(Math.toDegrees(Math.atan(getVelY()/getVelX())) + 180);
		}
	}
	
	@Override
	public void setVelY(double y){
		super.setVelY(y);
		//needed so that the angle has 360 degree range
		if(getVelX() > 0){
			setTheta(Math.toDegrees(Math.atan(getVelY()/getVelX())));
		}else if(getVelX() == 0){
			if(getVelY() > 0){
				setTheta(90);
			}else if(getVelY() < 0){
				setTheta(-90);
			}else{
				setTheta(0);
			}
		}else{
			setTheta(Math.toDegrees(Math.atan(getVelY()/getVelX())) + 180);
		}
	}
	
}
