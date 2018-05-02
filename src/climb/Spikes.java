
package climb;

import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

public class Spikes extends Object {
    private Rectangle bounds;	    // Collision box
    private int side;	    // Side of boundary the spikes are on
    
    /**
     * Spikes constructor
     * @param x Starting x coordinate of spike area
     * @param y Starting y coordinate of spike area
     * @param width Width of spike area
     * @param height Height of spike area
     * @param side Side of boundary the spikes are on (0 = top, 1 = right, 2 = bottom, 3 = left)
     */
    public Spikes(int x, int y, int width, int height, int side) {
	super(x, y, width, height);
	this.side = side;
	bounds = new Rectangle(x, y, width, height);
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
     * side Getter
     * @return side
     */
    public int getSide() {
	return side;
    }

    @Override
    public void tick() {}

    @Override
    public void render(Graphics g) {
	if (side % 2 == 0) {	// Up / down sides of a boundary
	    int i = getX();
	    while (i + 18 < getX() + getWidth()) {
		g.drawImage(Assets.spikes[side], i, getY(), 18, 18, null);
		i += 18;
	    }
	    BufferedImage bi = Assets.spikes[side].getSubimage(0, 0, (getX() + getWidth() - i) / 2, 9);
	    g.drawImage(bi, i, getY(), bi.getWidth() * 2, 18, null);
	} else {			// Left / right sides
	    int i = getY();
	    while (i + 18 < getY() + getHeight()) {
		g.drawImage(Assets.spikes[side], getX(), i, 18, 18, null);
		i += 18;
	    }
	    BufferedImage bi = Assets.spikes[side].getSubimage(0, 0, 9, (getY() + getHeight() - i) / 2);
	    g.drawImage(bi, i, getY(), 18, bi.getHeight() * 2, null);
	}
    } 
}
