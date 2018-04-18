
package climb;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class StaminaBar {
    private Player player;			    // Player instance
    private int posX, posY, width, height;	    // Stamina bar dimensions
    private int stamina, max;			    // Max and current amount of stamina
    double percent;				    // Percentage of stamina left
    private int tickCounter, displayTimer;	    // Tick counter, display timer

    /**
     * StaminaBar Constructor
     * @param player - Current player instance
     */
    public StaminaBar(Player player) {
	this.player = player;
	posX = player.getX() - 4;
	posY = player.getY() - 6;
	width = player.getWidth() + 8;
	height = 3;
	
	max = 33;
	stamina = max;
	percent = 1;
	
	tickCounter = 0;
	displayTimer = 0;
    }

    /* SETTERS AND GETTERS */
    
    /**
     * tickCounter Setter
     * @param tickCounter to modify 
     */
    public void setTickCounter(int tickCounter) {
	this.tickCounter = tickCounter;
    }

    /**
     * tickCounter Getter
     * @return tickCounter
     */
    public int getTickCounter() {
	return tickCounter;
    }
    
    /* METHODS */
    
    /**
     * Determine if you have stamina left
     * @return stamina is over 0
     */
    public boolean gotStamina() {
	return stamina > 0;
    }
    
    /**
     * Decrease stamina by a certain amount
     * @param usage - Stamina used up in action
     */
    public void useStamina(int usage) {
	stamina -= usage;
    }
    
    public void tick() {
	if (player.isClimbing()) {	    // If player is climbing, slowly deplete stamina
	    displayTimer = 0;
	    tickCounter++;
	    if (tickCounter >= 20) {	    // Happens every 20 frames (0.4 seconds)
		tickCounter = 0;
		stamina -= 2;
	    }
	} else {
	    if (player.isGrounded()) {	    // Replenish stamina when on the ground
		tickCounter = 0;
		if (stamina < max) {
		    stamina++;
		}
	    }
	    
	    // Make stamina bar transparent when stamina is full
	    if (stamina == max && displayTimer < 10) {
		displayTimer++;
	    }
	}
	percent = (double) stamina / max;
	
	// Update position
	posX = player.getX() - 4;
	posY = player.getY() - 6;
    }
    
    public void render(Graphics g) {
	// Graphics2D will allow drawing in transparency
	Graphics2D g2d = (Graphics2D) g;
	g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1 - 0.1f * displayTimer));
	
	g2d.setColor(new Color(0.5f, 0.5f, 0.5f));
	g2d.fillRect(posX, posY, width, height);
	g2d.setColor(Color.green);
	g2d.fillRect(posX, posY, (int) (width * percent), height);
    }
}
