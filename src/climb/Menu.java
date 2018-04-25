
package climb;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Menu {
    private Game game;				// Game instance
    private int state, selected;		// Current state within menu / Selected index option in menu
    /* Menu state values:
    0 -> Main menu
    1 -> Level select
    2 -> Options
    3 -> Credits
    4 -> Entering level
    */
    private String mainOptions[];		// Strings of available options
    private int mainPosX[], mainPosY[];		// Positions of options
    private int posOffset;			// Position offset for selected option
    private boolean offsetUp;			// Boolean for incrementing or decrementing posOffset
    private int selectTimer;			// Tick counter for selected option

    /**
     * Game Constructor
     * @param game - Game instance
     */
    public Menu(Game game) {
	this.game = game;
	state = 0;
	selected = 0;
	mainOptions = new String[] {"Climb", "Options", "Credits"};
	offsetUp = true;
	posOffset = 0;
	selectTimer = 0;
	
	mainPosX = new int[3]; mainPosY = new int[3];
	FontMetrics fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(40f));    // FontMetrics is used to measure string lengths
	// Position menu options to a certain area, aligned to the right
	for (int i = 0; i < mainOptions.length; i++) {
	    mainPosX[i] = game.getWidth() - fm.stringWidth(mainOptions[i]) - 70;
	    mainPosY[i] = (int) (1.4 * fm.getAscent() + (i == 0 ? 30 : mainPosY[i - 1]));
	}
    }
    
    /* METHODS */
    
    private void mainMenu() {
	// Menu Selection
	// TODO: Work on other menu options
	if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
	    switch (selected) {
		case 0:
		    game.prepareLevel();
		    return;
		default: break;
	    }
	}

	if (selectTimer >= 2) {
	    selectTimer = 0;

	    // Invert offsetUp to continue selected option animation
	    if (Math.abs(posOffset) == 3) {
		offsetUp = !offsetUp;
	    }
	    posOffset += (offsetUp ? 1 : -1);
	}

	// Change option on command
	if (selected < 2 && game.getKeyMan().typed(KeyEvent.VK_DOWN)) {
	    selected++;
	    selectTimer = 0;
	}
	if (selected > 0 && game.getKeyMan().typed(KeyEvent.VK_UP)) {
	    selected--;
	    selectTimer = 0;
	}

	selectTimer++;
    }
    
    public void tick() {
	switch (state) {
	    case 0:	// Main menu state
		mainMenu();
		break;
	    default: break;
	}
    }
    
    public void render(Graphics g) {
	g.drawImage(Assets.title_bg, 0, 0, game.getWidth(), game.getHeight(), null);
	
	g.setFont(Assets.font.deriveFont(40f));
	g.setColor(Color.WHITE);
	for (int i = 0; i < mainOptions.length; i++) {
	    g.drawString(mainOptions[i], mainPosX[i], mainPosY[i] + (i == selected ? posOffset : 0));
	}
    }
}
