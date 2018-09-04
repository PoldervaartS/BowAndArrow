import java.awt.Point;

public class LastShot {

	Point mouse1,mouse2;
	private double theta;
	
	
	public LastShot() {
		mouse1 = null;
		mouse2 = null; 
		theta = 0;
	}
	
	public LastShot(Point m1, Point m2, double t) {
		mouse1 = m1;
		mouse2 = m2; 
		theta = t;
	}
	
	public Point getM1(){
		return mouse1;
	}
	
	public Point getM2(){
		return mouse2;
	}
	
	public double getTheta() {
		return theta;
	}

	public void setM1(Point p){
		mouse1 = p;
	}
	
	public void setM2(Point p){
		mouse2 = p;
	}

	public void setTheta(double theta) {
		this.theta = theta;
	}

}
