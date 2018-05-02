
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
    2 -> Credits
    3 -> Entering level
    */
    private String mainOptions[];		// Strings of available options
    private int mainPosX[], mainPosY[];		// Positions of options
    private int posOffset;			// Position offset for selected option
    private boolean offsetUp;			// Boolean for incrementing or decrementing posOffset
    private int selectTimer;			// Tick counter for selected option
    private int levelToGo;			// Level selected and ready to go
    
    private String credits[];			    // Credits
    private int creditsPosX[], creditsPosY[];	    // Credits positions
    
    private Player player;
    private Boundary boundary;
    private Animation tent, campfire;

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
	mainOptions = new String[] {"Climb", "Credits"};
	mainPosX = new int[2]; mainPosY = new int[2];
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
	
	player = new Player(800, 564, null, null);
	boundary = new Boundary(0, 612, 1000, 168, 0, 0, 0, 0);
	tent = new Animation(Assets.tent, 150, true);
	campfire = new Animation(Assets.campfire, 90, true);
	
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
	if (selected < 1 && game.getKeyMan().typed(KeyEvent.VK_DOWN)) {
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
		case 0:	    // Level select
		    state = 1;
		    return;
		case 1:	    // Credits
		    state = 2;
		    break;
		default: break;
	    }
	    selectTimer = 0;
	    posOffset = 0;
	    selected = 0;
	}
    }
    
    private void levelSelect() {
	if (selectTimer >= 2) {
	    selectTimer = 0;

	    // Invert offsetUp to continue selected option animation
	    if (Math.abs(posOffset) == 3) {
		offsetUp = !offsetUp;
	    }
	    posOffset += (offsetUp ? 1 : -1);
	}

	// Change option on command
	if (selected < 1 && game.getKeyMan().typed(KeyEvent.VK_RIGHT)) {
	    selected++;
	    selectTimer = 0;
	}
	if (selected > 0 && game.getKeyMan().typed(KeyEvent.VK_LEFT)) {
	    selected--;
	    selectTimer = 0;
	}
	
	selectTimer++;
	
	// Level selection
	if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
	    switch (selected) {
		case 0:	    // Level select
		    levelToGo = 1;
		    state = 3;
		    return;
		case 1:	    // Credits
		    levelToGo = 2;
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
		player.manualControl(3);
		break;
	    case 1:	// Level select
		levelSelect();
		player.manualControl(3);
		break;
	    case 2:	// Credits
		if (game.getKeyMan().typed(KeyEvent.VK_ENTER)) {
		    state = 0;
		}
		player.manualControl(3);
		break;
	    case 3:
		player.manualControl(1);
		if (player.getX() + player.getWidth() < -30) {
		    Assets.bg_music.stop();
		    game.prepareLevel(levelToGo);
		    player.setX(800);
		    state = 0;
		}
	    default: break;
	}
	campfire.tick();
	tent.tick();
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
		g.setFont(Assets.font.deriveFont(90f));
		for (int i = 0; i < 2; i++) {
		    g.drawString(Integer.toString(i + 1), (i + 1) * game.getWidth() / 3, 200 + (i == selected ? posOffset : 0));
		}
		break;
	    case 2:	// Credits
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
	
	g.drawImage(tent.getCurrentFrame(), 600, boundary.y - 66, 126, 66, null);
	g.drawImage(campfire.getCurrentFrame(), 750, boundary.y - 64, 40, 64, null);
	player.render(g);
	
//	g.setColor(Color.red);
//	g.drawRect(boundary.x, boundary.y, boundary.width, boundary.height);
//	g.drawRect(player.x, player.y, player.width, player.height);
    }
}
