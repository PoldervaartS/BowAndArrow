import java.awt.BorderLayout;
import javax.swing.*;
 
public class Client extends JFrame {
	
	final int WIDTH=1200, HEIGHT=800;
	
    Client() {
         
        // frame description
        super("Bow And Arrow WIP");
        // our Canvas
        Game canvas = new Game(WIDTH,HEIGHT);
        add(canvas, BorderLayout.CENTER);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // set it's size and make it visible
        setSize(WIDTH, HEIGHT);
        setVisible(true);
        // befor being able to do that, the Canvas as to be visible
        canvas.createBufferStrategy(2);
    }
    // just to start the application
    public static void main(String[] args) {
        // instance of our stuff
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new Client();
            }
        });
    }
}