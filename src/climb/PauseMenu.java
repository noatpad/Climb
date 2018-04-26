
package climb;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class PauseMenu {
    private Game game;
    private boolean paused;
    private int exitState, state, selected;
    private Color bgColor;
    private FontMetrics fm;
    
    private String pauseOptions[];
    private int pauseOptionsPosX[], pauseOptionsPosY[];
    
    private String options[], values[];
    private int optionsPosX, valuesPosX[], optionsPosY[];
    
    private int posOffset;			// Position offset for selected option
    private boolean offsetUp;			// Boolean for incrementing or decrementing posOffset
    private int selectTimer;			// Tick counter for selected option

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
	
	pauseOptions = new String[] {"Resume", "Restart level", "Options", "Return to Title"};
	fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(25f));
	pauseOptionsPosX = new int[4]; pauseOptionsPosY = new int[4];
	for (int i = 0; i < 4; i++) {
	    pauseOptionsPosX[i] = game.getWidth() / 2 - fm.stringWidth(pauseOptions[i]) / 2;
	    pauseOptionsPosY[i] = (int) (1.4 * fm.getAscent()) + (i == 0 ? 200 : pauseOptionsPosY[i - 1]);
	}
	
	options = new String[] {"Music", "Sounds", "Display Stamina", "Camera Shake", "Controller Config", "Back"};
	values = new String[] {"10", "10", "YES", "YES"};	// Debug values (currently not yet usable)
	fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(30f));
	optionsPosX = game.getWidth() / 6;
	valuesPosX = new int[4]; optionsPosY = new int[6];
	for (int i = 0; i < 4; i++) {
	    valuesPosX[i] = 5 * game.getWidth() / 6 - fm.stringWidth(values[i]);
	}
	for (int i = 0; i < 6; i++) {
	    optionsPosY[i] = (int) (1.6 * fm.getAscent() + (i == 0 ? 250 : optionsPosY[i - 1]));
	}
	
    }

    public boolean isPaused() {
	return paused;
    }
    
    private void pauseMenu() {
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
    
//    private void options() {
//	if (selected < 6 && game.getKeyMan().typed(KeyEvent.VK_DOWN)) {
//	    selected++;
//	}
//	if (selected > 0 && game.getKeyMan().typed(KeyEvent.VK_UP)) {
//	    selected--;
//	}
//    }
    
    public void tick() {
	switch (state) {
	    case 0:	// Pause menu
		pauseMenu();
		break;
	    case 1:	// Options menu
		if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
		    paused = false;
		}
//		options();	    // NOT YET READY
		break;
	    case 2:	// Controller configuration
		break;
	}
    }
    
    public void render(Graphics g) {
	g.setColor(bgColor);
	g.fillRect(0, 0, game.getWidth(), game.getHeight());
	
	switch (state) {
	    case 0:	// Pause menu
		g.setFont(Assets.font.deriveFont(25f));
		for (int i = 0; i < 4; i++) {
		    if (i == 1) {
			g.setColor(new Color(0.5f, 0.5f, 0.5f));
		    } else {
			g.setColor(Color.white);
		    }
		    g.drawString(pauseOptions[i], pauseOptionsPosX[i], pauseOptionsPosY[i] + (i == selected ? posOffset : 0));
		}
		break;
	    case 1:	// Options menu
		g.setColor(Color.white);
		g.setFont(Assets.font.deriveFont(50f));
		fm = g.getFontMetrics();
		g.drawString("OPTIONS", game.getWidth() / 2 - fm.stringWidth("OPTIONS") / 2, 150);
		g.setFont(Assets.font.deriveFont(Font.ITALIC, 18f));
		g.setColor(new Color(0.5f, 0.5f, 0.5f));
		fm = g.getFontMetrics();
		g.drawString("Sorry! This isn't ready to go just yet! Press ENTER to exit.", game.getWidth() / 2 - fm.stringWidth("Sorry! This isn't ready to go just yet! Press ENTER to exit.") / 2, 200);
		g.setFont(Assets.font.deriveFont(30f));
		for (int i = 0; i < 6; i++) {
		    g.drawString(options[i], optionsPosX, optionsPosY[i]);
		}
		for (int i = 0; i < 4; i++) {
		    g.drawString(values[i], valuesPosX[i], optionsPosY[i]);
		}
		break;
	    case 2:	// Controller configuration
		break;
	    default: break;
	}
    }
}
