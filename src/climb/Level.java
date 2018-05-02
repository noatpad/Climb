
package climb;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Level {
    private Game game;				    // Game instance
    private ArrayList<Area> areas;		    // List of areas in level
    private Area currentArea, lastArea;		    // Current area that player was in (and previous area as well)
    private Player player;			    // Player object
    private PauseMenu pauseMenu;		    // PauseMenu object
    private Goal goal;				    // Goal area
    
    private boolean transition, paused, died, end;	    // Booleans for certain events
    private int deathTimer, endTimer;			    // Timers for certain events
    private int endMessageDisplayTimer;			    // Timer for "COMPLETE" at the end of the level
    
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
	end = false;
	deathTimer = 0;
	endTimer = 0;
	endMessageDisplayTimer = 0;

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
     * goal Setter
     * @param goal to modify 
     */
    public void setGoal(Goal goal) {
	this.goal = goal;
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

    /**
     * goal Getter
     * @return goal
     */
    public Goal getGoal() {
	return goal;
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
    
    /**
     * Cutscene for ending the level
     */
    private void endCutscene() {
	if (endTimer > 60) {
	    if (Math.abs(player.getX() - goal.getFirePosX()) > 50) {
		if (player.getX() > goal.getFirePosX()) {
		    player.manualControl(1);
		} else {
		    player.manualControl(2);
		}
	    } else {
		player.manualControl(3);
	    }
	} else {
	    player.manualControl(0);
	}
	
	if (endTimer == 180) {
	    Assets.ascending.stop();
	    Assets.complete.play();
	}
	
	if (endTimer > 180) {
	    if (endMessageDisplayTimer < 20) {
		endMessageDisplayTimer++;
	    }
	    
	    if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
		Assets.complete.stop();
		Assets.bg_music.play();
		game.setGameState(1);
	    }
	}
	
	endTimer++;
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
	    deathTimer++;
	    
	    if (deathTimer < 10) {
		player.tick();
	    } else if (deathTimer == 10) {
		player.respawn();
	    } else if (deathTimer == 25) {
		died = false;
	    }
	} else if (end) {
	    endCutscene();
	    goal.tick();
	} else {
	    player.tick();
	    
	    if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
		pauseMenu = new PauseMenu(game, 0, 0);
		paused = true;
	    }
	    
	    if (player.isEnd()) {
		end = true;
		Assets.level_music.stop();
		Assets.ascending.play();
	    }
	    
	    if (player.isDead()) {
		deathTimer++;
		if (deathTimer > 40) {
		    died = true;
		    deathTimer = 0;
		}
	    }
	    
	    goal.tick();
	}
    }
    
    public void render(Graphics g) {
	g.translate(-Camera.x, -Camera.y);
	g.setColor(Color.black);
	g.drawImage(Assets.level_bg, Camera.x, Camera.y, null);
	
	if (lastArea != null) {
	    lastArea.render(g);
	}
	
	goal.render(g);
	player.render(g);
	currentArea.render(g);
	
	if (died) {
	    g.setColor(Color.black);
	    g.fillRect(Camera.x, Camera.y - (int) (game.getHeight() * 1.5) + (game.getHeight() / 10 * deathTimer), game.getWidth(), (int) (game.getHeight() * 1.5));
	}
	
	if (paused) {
	    pauseMenu.render(g);
	}
	
	if (end) {
	    Graphics2D g2d = (Graphics2D) g;
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.05f * endMessageDisplayTimer));
	    g2d.setColor(Color.white);
	    g2d.setFont(Assets.font.deriveFont(90f));
	    g2d.drawString("COMPLETE", Camera.x + 50, Camera.y + 100 + endMessageDisplayTimer);
	    g.setFont(Assets.font.deriveFont(Font.ITALIC, 15f));
	    g2d.drawString("Press ENTER to continue...", Camera.x + 55, Camera.y + 130 + endMessageDisplayTimer);
	    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1));
	}
    }
}
