
package climb;

// import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;

public class Game implements Runnable {
    private String title;		// Title of the game
    private int height, width;		// Height and width of window
    private Thread thread;		// Java thread
    private Display disp;		// Game display
    private BufferStrategy bs;		// Display buffers 
    private Graphics g;			// Graphics object for painting
    private KeyManager keyMan;		// Keyboard manager (KeyListener)
    private int gameState;		// Cuurent game state
    /* Game state values:
    0 -> haven't started/terminated
    1 -> title screen menu
    2 -> gameplay
    */
    
    private Menu menu;		// Main menu instance
    private Level lvl;		// Level instance
    
    /**
     * Game constructor
     * @param title Title of game
     * @param height Height of window
     * @param width Width of window
     */
    public Game(String title, int height, int width) {
	this.title = title;
	this.height = height;
	this.width = width;
	keyMan = new KeyManager();
	gameState = 0;
    }
    
    /* SETTERS AND GETTERS */

    /**
     * gameState Setter
     * @param gameState to modify 
     */
    public void setGameState(int gameState) {
	this.gameState = gameState;
    }

    /**
     * width Getter
     * @return width
     */
    public int getWidth() {
	return width;
    }

    /**
     * height Getter
     * @return height
     */
    public int getHeight() {
	return height;
    }

    /**
     * graphics Getter
     * @return graphics
     */
    public Graphics getGraphics() {
	return bs.getDrawGraphics();
    }

    /**
     * keyMan Getter
     * @return keyMan
     */
    public KeyManager getKeyMan() {
	return keyMan;
    }

    /* METHODS */
    
    @Override
    public void run() {
	init();
	
	int fps = 50, ticks = 0;
	double timeTick = 1000000000 / fps, delta = 0;
	long now, lastTime = System.nanoTime();
	
	while (gameState != 0) {
	    now = System.nanoTime();
	    delta += (now - lastTime) / timeTick;
	    lastTime = now;
	    
	    if (delta >= 1) {
		tick();
		render();
		delta--;
	    }
	}
    }
    
    /**
     * Initialize
     */
    private void init() {
	disp = new Display(title, height, width);
	Assets.firstInit();
	Camera.menuInit();
	
	disp.getCanvas().createBufferStrategy(3);
	bs = disp.getCanvas().getBufferStrategy();
	
	menu = new Menu(this);
	
	disp.getjFrame().addKeyListener(keyMan);
    }
    
    /**
     * Prepare new level for playing
     * @param level Level number to prepare
     */
    public void prepareLevel(int level) {
	Assets.levelInit();
	lvl = new Level(this, level, 0);
	gameState = 2;
    }
    
    /**
     * Set off a tick update
     */
    private void tick() {
	switch (gameState) {
	    case 1:
		menu.tick();
		break;
	    case 2:
		lvl.tick();
		break;
	    default: break;
	}
    }
    
    /**
     * Render and paint objects
     */
    private void render() {
	g = bs.getDrawGraphics();

	switch (gameState) {
	    case 1:
		menu.render(g);
		break;
	    case 2:
		lvl.render(g);
		break;
	    default:
		break;
	}

	bs.show();
	g.dispose();
    }
    
    /**
     * Starts thread
     */
    public synchronized void start() {
	if (gameState == 0) {
	    gameState = 1;
	    thread = new Thread(this);
	    thread.start();
	}
    }
    
    /**
     * Stops thread
     */
    public synchronized void stop() {
	gameState = 0;
	try {
	    thread.join();
	} catch (InterruptedException ie) {
	    ie.printStackTrace();
	}
    }
}
