
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Level {
    private Game game;
    private ArrayList<Boundary> bounds;
    private Player player;
    
    /**
     * Level Constructor
     * @param game - Game instance
     */
    Level(Game game) {
	this.game = game;
	bounds = new ArrayList<>();
	// The following bounds.add() commands are for debugging purposes
	bounds.add(new Boundary(0, 0, 50, game.getHeight()));
	bounds.add(new Boundary(50, game.getHeight() - 100, game.getWidth(), 100));
	bounds.add(new Boundary(game.getWidth() - 500, game.getHeight() - 300, 250, 200));
	bounds.add(new Boundary(game.getWidth() - 250, 0, 250, game.getHeight() - 100));
	bounds.add(new Boundary(100, game.getHeight() - 400, game.getWidth() / 3, 50));
	bounds.add(new Boundary(100, game.getHeight() - 250, game.getWidth() / 6, 50));
	
	player = new Player(game.getWidth() / 2, game.getHeight() / 2, 30, 50, this);
    }
    
    /* GETTERS */

    /**
     * bounds Getter
     * @return bounds
     */
    public ArrayList<Boundary> getBounds() {
	return bounds;
    }
    
    /**
     * keyMan Getter
     * @return keyMan
     */
    public KeyManager getKeyMan() {
	return game.getKeyMan();
    }
    
    /* METHODS */
    
    public void tick() {
	player.tick();
    }
    
    public void render(Graphics g) {
	g.setColor(Color.black);
	g.fillRect(0, 0, game.getWidth(), game.getHeight());
	
	for (Boundary b : bounds) {
	    b.render(g);
	}
	
	player.render(g);
    }
}
