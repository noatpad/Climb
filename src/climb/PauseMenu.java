
package climb;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class PauseMenu {
    private Game game;						// Game instance
    private boolean paused;					// Boolean to determine paused state
    private int selected;			// State to which to exit, current state, current selection
    private Color bgColor;					// Background color
    private FontMetrics fm;					// FontMetrics instance
    
    // Strings and positions of pause menu options
    private String pauseOptions[];
    private int pauseOptionsPosX[], pauseOptionsPosY[];
    
    private int posOffset;			// Position offset for selected option
    private boolean offsetUp;			// Boolean for incrementing or decrementing posOffset
    private int selectTimer;			// Tick counter for selected option

    /**
     * PauseMenu Constructor
     * @param game Game instance
     */
    public PauseMenu(Game game) {
	this.game = game;
	paused = true;
	selected = 0;
	bgColor = new Color(0, 0, 0, 0.4f);
	offsetUp = true;
	posOffset = 0;
	selectTimer = 0;
	
	// Pause menu elements
	pauseOptions = new String[] {"Resume", "Restart level", "Return to Title"};
	fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(25f));
	pauseOptionsPosX = new int[3]; pauseOptionsPosY = new int[3];
	for (int i = 0; i < 3; i++) {
	    pauseOptionsPosX[i] = game.getWidth() / 2 - fm.stringWidth(pauseOptions[i]) / 2;
	    pauseOptionsPosY[i] = (int) (1.4 * fm.getAscent()) + (i == 0 ? 200 : pauseOptionsPosY[i - 1]);
	}
    }
    
    /* GETTERS */

    /**
     * paused Getter
     * @return paused
     */
    public boolean isPaused() {
	return paused;
    }
    
    /* METHODS */
    
    public void tick() {
	if (selectTimer >= 2) {
	    selectTimer = 0;

	    // Invert offsetUp to continue selected option animation
	    if (Math.abs(posOffset) == 3) {
		offsetUp = !offsetUp;
	    }
	    posOffset += (offsetUp ? 1 : -1);
	}
	
	// Change option on command
	if (selected < 3 && game.getKeyMan().typed(KeyEvent.VK_DOWN)) {
	    selected++;
	    selectTimer = 0;
	}
	if (selected > 0 && game.getKeyMan().typed(KeyEvent.VK_UP)) {
	    selected--;
	    selectTimer = 0;
	}
	
	// Menu Selection
	if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
	    switch (selected) {
		case 0:	    // Resume
		    paused = false;
		    return;
		case 1:	    // Restart level
		    game.getLvl().getPlayer().death();
		    paused = false;
		    return;
		case 2:	    // Return to Title
		    Assets.level_music.stop();
		    Assets.bg_music.play();
		    game.setGameState(1);
		    return;
		default: break;
	    }
	}
	
	selectTimer++;
    }
    
    public void render(Graphics g) {
	g.setColor(bgColor);
	g.fillRect(Camera.x, Camera.y, game.getWidth(), game.getHeight());
	g.setFont(Assets.font.deriveFont(25f));
	g.setColor(Color.white);
	for (int i = 0; i < 3; i++) {
	    g.drawString(pauseOptions[i], Camera.x + pauseOptionsPosX[i], Camera.y + pauseOptionsPosY[i] + (i == selected ? posOffset : 0));
	}
    }
}
