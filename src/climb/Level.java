
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level {
    private Game game;				    // Game instance
    private ArrayList<Area> areas;		    // List of areas in level
    private Area currentArea, lastArea;		    // Current area that player was in (and previous area as well)
    private Player player;			    // Player object
    private PauseMenu pauseMenu;		    // PauseMenu object
    private boolean transition, paused, died;	    // Boolean to determine if transitioning, paused, or died
    
    /**
     * Level Constructor
     * @param game Game instance
     */
    Level(Game game, int lvlNum, int areaNum) {
	this.game = game;
	areas = new ArrayList<>();
	transition = false;
	paused = false;
	died = false;

	Files.loadLevel(this, lvlNum, areaNum);	    // Loads level from file
	currentArea = areas.get(areaNum);
	Camera.levelInit(currentArea);		    // Sets up camera
	player = new Player(currentArea.getSpawnX() + currentArea.getPosX(), currentArea.getSpawnY() + currentArea.getPosY(), this, currentArea);
    }
    
    /* SETTERS AND GETTERS */

    /**
     * transition Setter
     * @param transition to modify 
     */
    public void setTransition(boolean transition) {
	this.transition = transition;
    }

    /**
     * game Getter
     * @return game
     */
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

    /**
     * currentArea Getter
     * @return currentArea
     */
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
     * Used to start transition between rooms/areas
     * @param lz Triggered LoadZone by player. Used to update 'currentArea'
     */
    public void startTransition(LoadZone lz) {
	lastArea = currentArea;
	currentArea = areas.get(lz.getToArea());
	transition = true;
    }
    
    /**
     * Transitioning between rooms
     */
    private void transition() {
	boolean h = (Camera.x == currentArea.getPosX()), v = (Camera.y == currentArea.getPosY());
	if (!h) {	// Horizontal translation
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
	if (!v) {	// Vertical translation
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
	
	// When the camera is at the right spot, go back to regular gameplay (update Area instance in Player and allow boosting again)
	if (h && v) {
	    lastArea = null;
	    player.setArea(currentArea);
	    player.setCanBoost(true);
	    transition = false;
	}
    }
    
    public void death() {
	player = null;
	died = true;
	System.out.println("climb.Level.death()");
    }
    
    public void tick() {
	if (transition) {
	    transition();
	} else if (paused) {
	    pauseMenu.tick();
	    if (!pauseMenu.isPaused()) {
		paused = false;
	    }
	} else if (died) {
	    
	} else {
	    player.tick();
	    if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
		pauseMenu = new PauseMenu(game, 0, 0);
		paused = true;
	    }
	}
    }
    
    public void render(Graphics g) {
	g.translate(-Camera.x, -Camera.y);
	g.setColor(Color.black);
	g.drawImage(Assets.level_bg, Camera.x, Camera.y, null);
	
	if (lastArea != null) {
	    lastArea.render(g);
	}
	currentArea.render(g);
	if (!died) {
	    player.render(g);
	}
	
	if (paused) {
	    pauseMenu.render(g);
	}
    }
}
