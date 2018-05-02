
package climb;

// import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Boundary extends Object {
    private Rectangle bound, floor, wallL, wallR, ceiling;	// Collision detection rectangles

    /**
     * Boundary Constructor
     * @param x X coordinate of boundary
     * @param y Y coordinate of boundary
     * @param width Width of boundary
     * @param height Height of boundary
     */
    public Boundary(int x, int y, int width, int height) {
	super(x, y, width, height);
	bound = new Rectangle(x, y, width, height);
	floor = new Rectangle(x + 1, y, width - 2, 10);
	wallL = new Rectangle(x, y + 1, 10, height - 2);
	wallR = new Rectangle(x + width - 10, y + 1, 10, height - 2);
	ceiling = new Rectangle(x + 1, y + height - 10, width - 2, 10);
    }
    
    /* GETTERS */

    /**
     * floor Getter
     * @return floor
     */
    public Rectangle getFloor() {
	return floor;
    }

    /**
     * wallL Getter
     * @return wallL
     */
    public Rectangle getWallL() {
	return wallL;
    }

    /**
     * wallR Getter
     * @return wallR
     */
    public Rectangle getWallR() {
	return wallR;
    }

    /**
     * ceiling Getter
     * @return ceiling
     */
    public Rectangle getCeiling() {
	return ceiling;
    }

    @Override
    public void tick() {}

    @Override
    public void render(Graphics g) {
//	g.setColor(Color.white);
//	g.fillRect(getX(), getY(), getWidth(), getHeight());
//	g.setColor(Color.cyan);
//	g.drawRect(floor.x, floor.y, floor.width, floor.height);
//	g.drawRect(ceiling.x, ceiling.y, ceiling.width, ceiling.height);
//	g.setColor(Color.orange);
//	g.drawRect(wallL.x, wallL.y, wallL.width, wallL.height);
//	g.drawRect(wallR.x, wallR.y, wallR.width, wallR.height);

	g.setClip(bound);
	for (int y = getY(); y < getY() + getHeight(); y += Assets.tile[0].getHeight()) {
	    for (int x = getX(); x < getX() + getWidth(); x += Assets.tile[0].getWidth()) {
		g.drawImage(Assets.tile[0], x, y, null);
	    }
	}
	g.setClip(floor);
	for (int x = floor.x; x < floor.x + floor.width; x += Assets.tile[1].getWidth()) {
	    g.drawImage(Assets.tile[1], x, floor.y, null);
	}
	g.setClip(wallL);
	for (int y = wallL.y; y < wallL.y + wallL.height; y += Assets.tile[3].getHeight()) {
	    g.drawImage(Assets.tile[2], wallL.x, y, null);
	}
	g.setClip(ceiling);
	for (int x = ceiling.x; x < ceiling.x + ceiling.width; x += Assets.tile[1].getWidth()) {
	    g.drawImage(Assets.tile[3], x, ceiling.y, null);
	}
	g.setClip(wallR);
	for (int y = wallR.y; y < wallR.y + wallR.height; y += Assets.tile[3].getHeight()) {
	    g.drawImage(Assets.tile[4], wallR.x, y, null);
	}
	g.setClip(null);
    }
}
