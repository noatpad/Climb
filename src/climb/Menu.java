
package climb;

import java.awt.Color;
import java.awt.Font;
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
    
    private PauseMenu options;			// Options instance
    
    private String credits[];			    // Credits
    private int creditsPosX[], creditsPosY[];	    // Credits positions

    /**
     * Game Constructor
     * @param game Game instance
     */
    public Menu(Game game) {
	this.game = game;
	state = 0;
	selected = 0;
	offsetUp = true;
	posOffset = 0;
	selectTimer = 0;
	
	// TODO: Make a Textbox class to allow handling these texts easier
	
	// Positions for main menu elements
	mainOptions = new String[] {"Climb", "Options", "Credits"};
	mainPosX = new int[3]; mainPosY = new int[3];
	FontMetrics fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(40f));    // FontMetrics is used to measure string lengths
	// Position menu options to a certain area, aligned to the right
	for (int i = 0; i < mainOptions.length; i++) {
	    mainPosX[i] = game.getWidth() - fm.stringWidth(mainOptions[i]) - 70;
	    mainPosY[i] = (int) (1.4 * fm.getAscent() + (i == 0 ? 30 : mainPosY[i - 1]));
	}
	
	// Positions for credits elements
	credits = new String[] {
	    "Daniel Hernandez",
	    "Leader, Programming, Art",
	    "Federico Alcereca",
	    "Level Design, Organization",
	    "Pedro Ontiveros",
	    "Level Design, Testing",
	    "Special thanks to:",
	    "The Celeste team",
	    "Lena Raine"
	};
	creditsPosX = new int[9]; creditsPosY = new int[9];
	for (int i = 0; i < 6; i++) {
	    if (i % 2 == 0) {
		fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(25f));
	    } else {
		fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(15f));
	    }
	    creditsPosX[i] = game.getWidth() - fm.stringWidth(credits[i]) - 70;
	    creditsPosY[i] = (int) (1.4 * fm.getAscent() + (i == 0 ? 30 : creditsPosY[i - 1]));
	}
	fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(25f));
	creditsPosX[6] = game.getWidth() - fm.stringWidth(credits[6]) - 70;
	creditsPosY[6] = (int) (1.4 * fm.getAscent() + creditsPosY[5] + 50);
	fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(20f));
	for (int i = 7; i < 9; i++) {
	    creditsPosX[i] = game.getWidth() - fm.stringWidth(credits[i]) - 70;
	    creditsPosY[i] = (int) (1.4 * fm.getAscent() + creditsPosY[i - 1]);
	}
	
	Assets.bg_music.play();
    }
    
    /* METHODS */
    
    /**
     * Main menu
     */
    private void mainMenu() {
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
	
	// Menu Selection
	if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
	    switch (selected) {
		case 0:	    // Supposed to be level select
		    Assets.bg_music.stop();
		    game.prepareLevel(1);
		    return;
		case 1:	    // Options
		    options = new PauseMenu(game, 1, 1);
		    state = 2;
		    break;
		case 2:	    // Credits
		    state = 3;
		    break;
		default: break;
	    }
	    selected = 0;
	}
    }
    
    public void tick() {
	switch (state) {
	    case 0:	// Main menu state
		mainMenu();
		break;
	    case 1:	// Level select
		break;
	    case 2:	// Options
		options.tick();
		if (!options.isPaused()) {
		    state = 0;
		}
		break;
	    case 3:	// Credits
		if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
		    state = 0;
		}
		break;
	    default: break;
	}
    }
    
    public void render(Graphics g) {
	g.drawImage(Assets.title_bg, 0, 0, game.getWidth(), game.getHeight(), null);
	g.setColor(Color.white);
	
	switch (state) {
	    case 0:	// Main menu
		g.setFont(Assets.font.deriveFont(40f));
		for (int i = 0; i < mainOptions.length; i++) {
		    g.drawString(mainOptions[i], mainPosX[i], mainPosY[i] + (i == selected ? posOffset : 0));
		}
		break;
	    case 1:	// Level Select
		break;
	    case 2:	// Options
		options.render(g);
		break;
	    case 3:	// Credits
		for (int i = 0; i < 6; i++) {
		    if (i % 2 == 0) {
			g.setFont(Assets.font.deriveFont(25f));
		    } else {
			g.setFont(Assets.font.deriveFont(15f));
		    }
		    g.drawString(credits[i], creditsPosX[i], creditsPosY[i]);
		}
		g.setFont(Assets.font.deriveFont(25f));
		g.drawString(credits[6], creditsPosX[6], creditsPosY[6]);
		g.setFont(Assets.font.deriveFont(20f));
		for (int i = 7; i < 9; i++) {
		    g.drawString(credits[i], creditsPosX[i], creditsPosY[i]);
		}
		g.setFont(Assets.font.deriveFont(Font.ITALIC, 15f));
		g.drawString("Press ENTER to go back", 30, game.getHeight() - 30);
		break;
	    default: break;
	}
    }
}
