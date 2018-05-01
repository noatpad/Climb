
package climb;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Display {
    private JFrame jFrame;	    // jFrame object
    private Canvas canvas;	    // Canvas object
    
    private String title;	    // Title of window
    private int height, width;	    // Window dimensions
    
    /**
     * Display Constructor
     * @param t Title of the window
     * @param h Height of window
     * @param w Width of window
     */
    public Display(String t, int h, int w) {
	title = t;
	height = h;
	width = w;
	createDisplay();
    }

    /**
     * <b>jFrame</b> Getter
     * @return jFrame
     */
    public JFrame getjFrame() {
	return jFrame;
    }
    
    /**
     * Creates app window and adds canvas to the window
     */
    public void createDisplay() {
	jFrame = new JFrame(title);	// Creates app window
	jFrame.setSize(width, height);	// Sets window's dimensions
	
	// Define window properties
	jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	jFrame.setResizable(false);
	jFrame.setLocationRelativeTo(null);
	jFrame.setVisible(true);
	
	// Define canvas and its properties
	canvas = new Canvas();
	canvas.setPreferredSize(new Dimension(width, height));
	canvas.setMaximumSize(new Dimension(width, height));
	canvas.setFocusable(false);
	
	// Add canvas to window and pack it into the right dimensions
	jFrame.add(canvas);
	jFrame.pack();
    }
    
    /**
     * Canvas getter
     * @return the canvas
     */
    public Canvas getCanvas() {
	return canvas;
    }
}
