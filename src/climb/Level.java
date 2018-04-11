
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Level {
    private Game game;
    private ArrayList<ArrayList<Boundary>> levelBounds;
    private ArrayList<Boundary> areaBounds;
    private ArrayList<Integer> spawnX, spawnY;
    private Player player;
    
    /**
     * Level Constructor
     * @param game - Game instance
     */
    Level(Game game, int lvlNum, int areaNum) {
	this.game = game;
	levelBounds = new ArrayList<>();
	areaBounds = new ArrayList<>();
	spawnX = new ArrayList<>(); spawnY = new ArrayList<>();
	player = new Player(0, 0, 30, 50, this);

	Files.loadLevel(this, lvlNum, areaNum);
	loadArea(areaNum);
    }
    
    /* GETTERS */
    
    /**
     * keyMan Getter
     * @return keyMan
     */
    public KeyManager getKeyMan() {
	return game.getKeyMan();
    }

    /**
     * levelBounds Getter
     * @return levelBounds
     */
    public ArrayList<ArrayList<Boundary>> getLevelBounds() {
	return levelBounds;
    }

    /**
     * bounds Getter
     * @return bounds
     */
    public ArrayList<Boundary> getBounds() {
	return areaBounds;
    }

    /**
     * spawnX Getter
     * @return spawnX
     */
    public ArrayList<Integer> getSpawnX() {
	return spawnX;
    }

    /**
     * spawnY Getter
     * @return spawnY
     */
    public ArrayList<Integer> getSpawnY() {
	return spawnY;
    }

    /**
     * player Getter
     * @return player
     */
    public Player getPlayer() {
	return player;
    }
    
    /* METHODS */
    
    /**
     * Loads the specified area for the first time
     * @param areaNum Area number of the level
     */
    public void loadArea(int areaNum) {
	areaBounds = levelBounds.get(areaNum);
	player.setX(spawnX.get(areaNum));
	player.setY(spawnY.get(areaNum));
    }
    
    public void tick() {
	player.tick();
    }
    
    public void render(Graphics g) {
	g.setColor(Color.black);
	g.fillRect(0, 0, game.getWidth(), game.getHeight());
	
	for (Boundary b : areaBounds) {
	    b.render(g);
	}
	
	player.render(g);
    }
}
