
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Level {
    private Game game;
    private ArrayList<Area> areas;
    private Area currentArea;
    private Player player;
    
    /**
     * Level Constructor
     * @param game - Game instance
     */
    Level(Game game, int lvlNum, int areaNum) {
	this.game = game;
	areas = new ArrayList<>();

	Files.loadLevel(this, lvlNum, areaNum);
	loadArea(areaNum);
	System.out.println(areas.size());
	player = new Player(currentArea.getSpawnX(), currentArea.getSpawnY(), 30, 50, this, currentArea);
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
     * areas Getter
     * @return areas
     */
    public ArrayList<Area> getAreas() {
	return areas;
    }

    public Area getCurrentArea() {
	return currentArea;
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
	currentArea = areas.get(areaNum);
    }
    
    public void tick() {
	player.tick();
    }
    
    public void render(Graphics g) {
	g.setColor(Color.black);
	g.fillRect(0, 0, game.getWidth(), game.getHeight());
	
	currentArea.render(g);
	
	player.render(g);
    }
}
