import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DecimalFormat;
import java.awt.Polygon;
import java.awt.geom.Rectangle2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import javax.imageio.ImageIO;

public class EngineObject {
	
	private Point p;
	private int width,height;
	private double velX,velY, theta;
	Color c;
	Polygon poly;
	//The polygon that will be rotated?
	private Polygon initialPoly;
	boolean inAir,canCollide;
	private long landTime;
	private BufferedImage bImage;
	private Image image;
	
	
	public EngineObject(Point p, int width, int height){
		this(p,width,height,0,0);
	}
	
	public EngineObject(Point p, int width, int height, double velX, double velY){
		setP(p);
		setWidth(width);
		setHeight(height);
		setVelX(velX);
		setVelY(velY);
		setColor(randColor());
		setTheta(0);
		setLandTime(0);
		setInAir(false);
		setCanCollide(true);
		setPoly();
		initialPoly = poly;
	}
	
	public void draw(Graphics2D g2, Point p){
		//just for reference
		g2.setColor(c);
		g2.fillRect(p.x, p.y, width, height);
	}
	
	public void draw(Graphics2D g2, int x, int y){
		//just for reference part 2
		g2.setColor(c);
		g2.fillRect(x, y, width, height);
	}
	
	//ALL classes with image drawing will have a final string for image location
	public void setImage(String s){
		try {
			bImage = ImageIO.read(new File(s));
			image = bImage.getScaledInstance(width, height, 1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public Color randColor(){
		return new Color((int)(Math.random()*255),(int)(Math.random()*255),(int)(Math.random()*255));
	}
	
	public Point getP() {
		return p;
	}
	
	public int getX(){
		return p.x;
	}
	
	public int getY(){
		return p.y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public Color getColor(){
		return c;
	}
	
	public double getVelX() {
		return velX;
	}

	public double getVelY() {
		return velY;
	}

	public double getTheta() {
		return theta;
	}

	public long getLandTime() {
		return landTime;
	}

	public boolean inAir(){
		return inAir;
	}
	
	public boolean canCollide(){
		return canCollide;
	}

	public Image getImage(){
		return image;
	}
	
	public Polygon getPoly() {
		return poly;
	}

	public void setP(Point p) {
		if(this.p != null && poly != null){
			initialPoly.translate(p.x-this.p.x, p.y-this.p.y);
			poly = initialPoly;
		}
		this.p = p;
	}
	
	public void setX(int x){
		setP(new Point(x,getY()));
	}
	
	public void setY(int y){
		setP(new Point(getX(),y));
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setColor(Color c){
		this.c = c;
	}

	public void setVelX(double velX) {
		this.velX = velX;
	}

	public void setVelY(double velY) {
		this.velY = velY;
	}
	
	public void setInAir(boolean inAir){
		this.inAir = inAir;
	}

	//ROUNDING THE DECIMAL TO 3 PLACES
	public void setTheta(double theta) {
		if(initialPoly!=null){
			Point centroid = getPolyCentroid();
			Point2D[] initialP = getPolyPoints();
			Point2D[] finalP = new Point2D[initialP.length];
			AffineTransform.getRotateInstance(Math.toRadians(theta), centroid.x, centroid.y).transform(initialP,0,finalP,0,initialP.length);
			Polygon newPoly = new Polygon();
			for(int i = 0; i < finalP.length; i++){
				newPoly.addPoint((int)finalP[i].getX(),(int)finalP[i].getY());
			}
			setPoly(newPoly);
		}
		DecimalFormat df = new DecimalFormat("#.###");
		this.theta = Double.parseDouble(df.format(theta));
	}

	public void setLandTime(long landTime) {
		this.landTime = landTime;
	}
	
	public void setCanCollide(boolean c){
		canCollide = c;
	}
	
	//the default just makes a regular square
	public void setPoly(){
		poly = new Polygon();
		poly.addPoint(getX(), getY());
		poly.addPoint(getX(), getY()+height);
		poly.addPoint(getX()+ width, getY()+height);
		poly.addPoint(getX()+ width, getY());
	}

	public void setPoly(Polygon poly) {
		this.poly = poly;
	}

	public int compareXTo(EngineObject arg0) {
		return getX() - arg0.getX();
	}
	
	public int compareYTo(EngineObject o){
		return getY() - o.getY();
	}

	public Point getPolyCentroid(){
		int pCnt = initialPoly.npoints;
		int[] xPoints = initialPoly.xpoints;
		int[] yPoints = initialPoly.ypoints;
		int xVal = 0;
		int yVal = 0;
		for(int i = 0; i < pCnt; i++){
			xVal += xPoints[i];
			yVal += yPoints[i];
		}
		return new Point(xVal/pCnt, yVal/pCnt);
	}
	
	public Point2D[] getPolyPoints(){
		int pCnt = initialPoly.npoints;
		Point2D[] points = new Point2D[pCnt];
		int[] xPoints = initialPoly.xpoints;
		int[] yPoints = initialPoly.ypoints;
		for(int i = 0; i < pCnt; i++){
			points[i] = new Point(xPoints[i],yPoints[i]);
		}
		return points;
	}
	
	public Rectangle2D getBounds(){
		return poly.getBounds2D();
	}
	
	public double netVel(){
		return Math.sqrt(Math.pow(getVelX(), 2) + Math.pow(getVelY(), 2));
	}

}
