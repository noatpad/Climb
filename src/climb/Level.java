
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

public class Level {
    private Game game;
    private ArrayList<Area> areas;
    private Area currentArea;
    private Player player;
    private boolean transition;
    
    /**
     * Level Constructor
     * @param game - Game instance
     */
    Level(Game game, int lvlNum, int areaNum) {
	this.game = game;
	areas = new ArrayList<>();
	transition = false;

	Files.loadLevel(this, lvlNum, areaNum);
	currentArea = areas.get(areaNum);
	Camera.levelInit(currentArea);
	player = new Player(currentArea.getSpawnX(), currentArea.getSpawnY(), 30, 50, this, currentArea);
//	posX = currentArea.getPosX(); posY = currentArea.getPosY();
    }
    
    /* SETTERS AND GETTERS */

    public void setTransition(boolean transition) {
	this.transition = transition;
    }

    public Game getGame() {
	return game;
    }
    
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
    
    public void startTransition(LoadZone lz) {
	currentArea = areas.get(lz.getToArea());
	transition = true;
     }
    
    private void transition() {
	boolean h = (Camera.x == currentArea.getPosX()), v = (Camera.y == currentArea.getPosY());
	if (!h) {
	    if (Math.abs(Camera.x - currentArea.getPosX()) >= 4) {
		Camera.x += (currentArea.getPosX() - Camera.x) / 4;
		if (Camera.x < currentArea.getPosX()) {
		    player.setX(player.getX() + (currentArea.getPosX() - player.getX()) / 4);
		} else {
		    player.setX(player.getX() + (currentArea.getPosX() + game.getWidth() - player.getX() - player.getWidth()) / 4);
		}
	    } else {
		Camera.x += currentArea.getPosX() > Camera.x ? 1 : -1;
		player.setX(player.getX() + (currentArea.getPosX() > Camera.x ? 1 : -1));
	    }
	}
	if (!v) {
	    if (Math.abs(Camera.y - currentArea.getPosY()) >= 4) {
		Camera.y += (currentArea.getPosY() - Camera.y) / 4;
		if (Camera.y < currentArea.getPosY()) {
		    player.setY(player.getY() + (currentArea.getPosY() - player.getY()) / 4);
		} else {
		    player.setY(player.getY() + (currentArea.getPosY() + game.getHeight() - player.getY() - player.getHeight()) / 4);
		}
	    } else {
		Camera.y += currentArea.getPosY() > Camera.y ? 1 : -1;
		player.setY(player.getY() + (currentArea.getPosY() > Camera.y ? 1 : -1));
	    }
	}
	
	player.updateBoxes();
	if (h && v) {
	    player.setArea(currentArea);
	    transition = false;
	}
    }
    
    public void tick() {
	if (transition) {
	    transition();
	} else {
	    player.tick();
	}
    }
    
    public void render(Graphics g) {
	g.translate(-Camera.x, -Camera.y);
	g.setColor(Color.black);
	g.fillRect(0, 0, game.getWidth() * 2, game.getHeight() * 2);
	
	if (!transition) {
	    currentArea.render(g);
	}
	
	player.render(g);
    }
}
