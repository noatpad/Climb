
package climb;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class PauseMenu {
    private Game game;						// Game instance
    private boolean paused;					// Boolean to determine paused state
    private int exitState, state, selected;			// State to which to exit, current state, current selection
    /* State values:
    0 -> Main pause menu
    1 -> Options menu
    2 -> Controller config menu
    */
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
     * @param exitState State that menu can be in to exit pause menu altogether
     * @param state Current state
     */
    public PauseMenu(Game game, int exitState, int state) {
	this.game = game;
	this.exitState = exitState;
	this.state = state;
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
		case 1:	    // Restart level (NOT YET IMPLEMENTED)
		    return;
		case 2:	    // Options
		    state = 1;
		    return;
		case 3:	    // Return to Title
		    game.setGameState(1);
		default: break;
	    }
	}
	
	selectTimer++;
    }
    
    public void render(Graphics g) {
	g.setColor(bgColor);
	g.fillRect(0, 0, game.getWidth(), game.getHeight());
	g.setFont(Assets.font.deriveFont(25f));
	for (int i = 0; i < 4; i++) {
	    if (i == 1) {
		g.setColor(new Color(0.5f, 0.5f, 0.5f));
	    } else {
		g.setColor(Color.white);
	    }
	    g.drawString(pauseOptions[i], pauseOptionsPosX[i], pauseOptionsPosY[i] + (i == selected ? posOffset : 0));
	}
	}
    }
}
