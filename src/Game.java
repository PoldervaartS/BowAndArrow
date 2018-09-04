
import java.awt.*;
import java.util.*;
import javax.swing.Timer;
import java.awt.event.*;
import java.awt.image.BufferStrategy;

public class Game extends Canvas implements MouseListener, KeyListener {

	int width, height, playerTurn;
	Point mouse1, mouse2, cameraGoal;
	boolean mousePressed = false, repaintInProgress = false;
	ArrayList<EngineObject> objects;
	EngineObject ground;
	final long turnTime = 10000, waitTime = 2000; // 10 seconds to shoot, wait 2
													// seconds after landing
	final int FLOORHT = 140;
	final double GRAVITY = .1, POWERDIVISOR = 10;
	Player p1, p2;
	Camera camera;
	Color backGround;

	public Game(int width, int height) {
		this.width = width;
		this.height = height;
		setIgnoreRepaint(true);
		addMouseListener(this);
		addKeyListener(this);
		requestFocus();
		Chrono chrono = new Chrono(this);
		new Timer(10, chrono).start();
		setUpGame();
	}

	public void setUpGame() {
		camera = new Camera(0, 0, width, height);
		cameraGoal = new Point(0, 0);
		objects = new ArrayList<>();
		
		//will be different for every game depending on what is happening 
		backGround = Color.WHITE;
		// Adding the ground
		ground = new EngineObject(new Point(-100000, height - FLOORHT), 1000000, 1000000);
		ground.setColor(Color.green);
	
		// Spawning players
		spawnPlayers();
		objects.add(p1);
		objects.add(p2);
		playerTurn = 0;
	
		// Spawn rocks every thousand units between the players, rand amount of
		// sides from 1-8
		int startX = p1.getX();
		for (int i = 0; i < (Math.abs(p1.getX() - p2.getX())); i += 1000) {
			Rock r = new Rock(new Point(startX + i, height - FLOORHT - Rock.R), Rock.R * 2, Rock.R * 2,
					(int) (Math.random() * 10) + 1);
			objects.add(r);
		}
		
		objects.add(new Wall(new Point(6000, 0), 40, height));
		objects.add(new Arrow(new Point(p1.getX() + 40, p1.getY() + 40)));
	}

	public void spawnPlayers() {
		p1 = new Player(new Point(150, 200));
		p2 = new Player(new Point((int) (Math.random() * 10000 + 5000), 200));
	}

	public void myRepaint() {

		update();
		if (getBufferStrategy() == null) {
			createBufferStrategy(2);
		}

		if (repaintInProgress)
			return;

		repaintInProgress = true;
		BufferStrategy strategy = getBufferStrategy();
		Graphics g = strategy.getDrawGraphics();
		Graphics2D g2 = (Graphics2D) g;
		
		if(p1.getHealth() <= 0){
			win(g2, 2);
			if (g2 != null) {
				g2.dispose();
				g.dispose();
			}
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
			repaintInProgress = false;
		}else if(p2.getHealth() <= 0){
			win(g2, 1);
			if (g2 != null) {
				g2.dispose();
				g.dispose();
			}
			strategy.show();
			Toolkit.getDefaultToolkit().sync();
			repaintInProgress = false;
		}
		g2.setColor(backGround);
		g2.fillRect(0, 0, width, height);

		g2.setColor(p1.getColor());
		g2.drawString("Player one health " + p1.getHealth(), 20, 20);
		g2.setColor(p2.getColor());
		g2.drawString("Player two health " + p2.getHealth(), 200, 20);

		if (camera.getLockedOn() != null) {
			camera.moveCamera(null);
		}
		if (cameraGoal.x != camera.p.x || cameraGoal.y != camera.p.y) {
			camera.moveCamera(cameraGoal);
		}

		// Drawing the objects in view
		if (camera.inCameraView(ground)) {
			camera.drawObject(g2, ground);
		}
		for (int i = 0; i < objects.size(); i++) {
			if (camera.inCameraView(objects.get(i))) {
				camera.drawObject(g2, objects.get(i));
			}

		}
		if(Math.random() > .5){
			objects.add(new Arrow(new Point((int)(Math.random()*width),20),2,1));
			objects.get(objects.size()-1).setInAir(true);
			objects.get(objects.size()-1).setCanCollide(false);
		}
		System.out.println(objects.size());

		// Drawing the mouse drag line
		if (mousePressed) {
			Point temp = getMousePosition();
			if (temp != null) {
				mouse2 = temp;
				g2.setColor(Color.BLACK);
				g2.drawLine(mouse1.x, mouse1.y, mouse2.x, mouse2.y);
				g2.drawString(
						"Power: " + String.format("%.4f",
								Math.sqrt((mouse1.x - mouse2.x) / POWERDIVISOR * (mouse1.x - mouse2.x) / POWERDIVISOR
										+ (mouse1.y - mouse2.y) / POWERDIVISOR * (mouse1.y - mouse2.y) / POWERDIVISOR)),
						mouse1.x, mouse1.y);
			} else {
				g2.setColor(Color.BLACK);
				g2.drawLine(mouse1.x, mouse1.y, mouse2.x, mouse2.y);
			}
		}
		if (g2 != null) {
			g2.dispose();
			g.dispose();
		}
		strategy.show();
		Toolkit.getDefaultToolkit().sync();
		repaintInProgress = false;
	}

	public void update() {
		
		//so you can't shoot yourself until one second after shooting the arrow
		if(!p1.canCollide){
			if(System.currentTimeMillis() - p1.getShotT() > 1000) p1.setCanCollide(true);
		}
		if(!p2.canCollide){
			if(System.currentTimeMillis() - p2.getShotT() > 1000) p2.setCanCollide(true);
		}
		for (int i = 0; i < objects.size(); i++) {
			EngineObject e = objects.get(i);
			if (e.inAir()) {
				if(e.canCollide()){
					for(int j = 0; j < objects.size(); j++){
						if(j != i && objects.get(j).canCollide() && collisionCheck(e,objects.get(j))){
							//specific to game only
							if(objects.get(j) instanceof Player){
								System.out.println("Player hit for " + (int)(e.netVel()*10));
								Player p = (Player) objects.get(j);
								p.setHealth(p.getHealth() - (int)e.netVel() * 10);
								//moves the player to new random position after someone has been hit
								p2.setX((int) (Math.random() * 10000 + 7000));
								p2.setLastShot(null);
								p1.setLastShot(null);
							}
							objects.get(i).setInAir(false);
							objects.get(i).setCanCollide(false);
							e.setLandTime(System.currentTimeMillis());
						}
					}
				}
				//even if collision for object is off can hit ground, idk how to feel about this
				if (e.getY() + Math.sin(Math.toRadians(e.getTheta())) * e.getHeight() >= ground.getY()) {
					e.setInAir(false);
					e.setLandTime(System.currentTimeMillis());
				} else {
					e.setVelY(e.getVelY() + GRAVITY);
					e.setP(new Point(e.getX() + (int) e.getVelX(), e.getY() + (int) e.getVelY()));
				}
			}
		}
		// If camera is locked on to something, what to do if it lands
		if (camera.getLockedOn() != null && !camera.getLockedOn().inAir) {
			if (camera.getLockedOn().getLandTime() != 0
					&& System.currentTimeMillis() - waitTime >= camera.getLockedOn().getLandTime()) {
				//changing turns and setting where the camera will move	
				camera.setLockedOn(null);
				if (playerTurn == 0) {
					playerTurn = 1;
					Point p = p2.getP();
					cameraGoal = new Point(p.x - width + 150, p.y - 200);
					objects.add(new Arrow(new Point(p.x - 80, p.y + 40), 180));
				} else {
					playerTurn = 0;
					Point p = p1.getP();
					cameraGoal = new Point(p.x - 150, p.y - 200);
					objects.add(new Arrow(new Point(p.x + 40, p.y + 40)));
				}
			}
		}
	}

	public Boolean collisionCheck(EngineObject e1, EngineObject e2){
		return(e1.getPoly().intersects(e2.getPoly().getBounds2D()));
	}

	// returns the true position of that point on the screen
	public Point gridPos(Point p) {
		Point cam = camera.getP();
		return new Point(cam.x + p.x, cam.y + p.y);
	}

	public void win(Graphics2D g2, int num){
		objects.clear();
		backGround = Color.BLACK;
		g2.setColor(backGround);
		g2.fillRect(0, 0, width, height);
		g2.setColor(Color.WHITE);
		g2.setFont(new Font("TimesRoman", Font.PLAIN, 64));
		g2.drawString("Player " + num + " Wins!", width/3, height/2);
		
		
	}

	public void launchArrow() {
		Arrow a = (Arrow) objects.get(objects.size() - 1);
		a.setInAir(true);
		a.setVelX((mouse1.x - mouse2.x) / POWERDIVISOR);
		a.setVelY((mouse1.y - mouse2.y) / POWERDIVISOR);
		if (playerTurn == 0) {
			p1.setCanCollide(false);
			p1.setLastShot(gridPos(mouse1), gridPos(mouse2), a.getTheta());
			p1.setShotT(System.currentTimeMillis());
		} else {
			p2.setCanCollide(false);
			p2.setLastShot(gridPos(mouse1), gridPos(mouse2), a.getTheta());
			p2.setShotT(System.currentTimeMillis());
		}
		camera.setLockedOn(a);
		mouse1 = null;
		mouse2 = null;
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mousePressed(MouseEvent e) {
		if ((camera.getLockedOn() != null && camera.getLockedOn().inAir))
			return;
		mouse1 = e.getPoint();
		mousePressed = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		if (camera.getLockedOn() != null && camera.getLockedOn().inAir)
			return;
		mousePressed = false;
		mouse2 = e.getPoint();
		launchArrow();
	}

	@Override
	public void keyPressed(KeyEvent arg0) {
		System.out.println("Key Pressed");
		if(arg0.getExtendedKeyCode() == arg0.VK_R){
			setUpGame();
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}
}

class Chrono implements ActionListener {

	Game g = null;

	public Chrono(Game g) {
		this.g = g;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (g != null) {
			g.myRepaint();
		}

	}

}
