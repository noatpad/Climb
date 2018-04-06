
package climb;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

public class Menu {
    private Game game;							// Game instance
    private int selected = 0;						// Selected index option in menu
    private String mainOptions[] = {"Climb", "Options", "Credits"};	// Strings of available options
    private int mainPosX[], mainPosY[];					// Positions of options
    private int posOffset = 0;						// Position offset for selected option
    private boolean offsetUp = true;					// Boolean for incrementing or decrementing posOffset

    /**
     * Game Constructor
     * @param game - Game instance
     */
    public Menu(Game game) {
	this.game = game;
	
	mainPosX = new int[3]; mainPosY = new int[3];
	FontMetrics fm = game.getGraphics().getFontMetrics(Assets.font.deriveFont(40f));    // FontMetrics is used to measure string lengths
	// Position menu options to a certain area, aligned to the right
	for (int i = 0; i < mainOptions.length; i++) {
	    mainPosX[i] = game.getWidth() - fm.stringWidth(mainOptions[i]) - 70;
	    mainPosY[i] = (int) (1.4 * fm.getAscent() + (i == 0 ? 30 : mainPosY[i - 1]));
	}
    }
    
    /* METHODS */
    
    public void tick() {
	// Prepare level when selecting "Climb"
	if (game.getKeyMan().typed(KeyEvent.VK_ENTER) && selected == 0) {
	    game.prepareLevel();
	    return;
	}
	// TODO: Work on other menu options
	
	// Invert offsetUp to continue selected option animation
	if (Math.abs(posOffset) == 4) {
	    offsetUp = !offsetUp;
	}
	posOffset += (offsetUp ? 1 : -1);
	
	// Change option on command
	if (selected < 2 && game.getKeyMan().typed(KeyEvent.VK_DOWN)) {
	    selected++;
	}
	if (selected > 0 && game.getKeyMan().typed(KeyEvent.VK_UP)) {
	    selected--;
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
