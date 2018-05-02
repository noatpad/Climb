
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Goal extends Object {
    private Rectangle bounds;		// Collision detection
    private int tentPosX, tentPosY;	// Position of tent
    private int firePosX, firePosY;	// Position of campfire
    private Animation tent, fire;	// Animations
    
    /**
     * Goal constructor
     * @param x X coordinate of goal collision
     * @param y Y coordinate of goal collision
     * @param width Width of goal collision area
     * @param height Height of goal collision area
     * @param tentPosX X coordinate of tent
     * @param firePosX X coordinate of campfire
     */
    public Goal(int x, int y, int width, int height, int tentPosX, int firePosX) {
	super(x, y, width, height);
	
	bounds = new Rectangle(x, y, width, height);
	
	this.tentPosX = tentPosX;
	tentPosY = y + height - 66;
	this.firePosX = firePosX;
	firePosY = y + height - 64;
	
	tent = new Animation(Assets.tent, 150, true);
	fire = new Animation(Assets.campfire, 90, true);
    }
    
    /* GETTERS */

    /**
     * bounds Getter
     * @return bounds
     */
    public Rectangle getBounds() {
	return bounds;
    }

    /**
     * firePosX Getter
     * @return firePosX
     */
    public int getFirePosX() {
	return firePosX;
    }
    
    /* METHODS */

    @Override
    public void tick() {
	tent.tick();
	fire.tick();
    }

    @Override
    public void render(Graphics g) {
//	g.setColor(Color.red);
//	g.drawRect(x, y, width, height);
	g.drawImage(tent.getCurrentFrame(), tentPosX, tentPosY, 126, 66, null);
	g.drawImage(fire.getCurrentFrame(), firePosX, firePosY, 40, 64, null);
    }

}
