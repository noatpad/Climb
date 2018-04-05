
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Boundary extends Object {
    private Rectangle floor, wallL, wallR, ceiling;

    public Boundary(int x, int y, int width, int height) {
	super(x, y, width, height);
	floor = new Rectangle(x + 1, y, width - 2, 1);
	wallL = new Rectangle(x, y + 1, 1, height - 2);
	wallR = new Rectangle(x + width - 1, y + 1, 1, height - 2);
	ceiling = new Rectangle(x + 1, y + height - 1, width - 2, 1);
    }

    public Rectangle getFloor() {
	return floor;
    }

    public Rectangle getWallL() {
	return wallL;
    }

    public Rectangle getWallR() {
	return wallR;
    }

    public Rectangle getCeiling() {
	return ceiling;
    }

    @Override
    public void tick() {}

    @Override
    public void render(Graphics g) {
	g.setColor(Color.cyan);
	g.drawRect(floor.x, floor.y, floor.width, floor.height);
	g.drawRect(ceiling.x, ceiling.y, ceiling.width, ceiling.height);
	g.setColor(Color.orange);
	g.drawRect(wallL.x, wallL.y, wallL.width, wallL.height);
	g.drawRect(wallR.x, wallR.y, wallR.width, wallR.height);
//	g.setColor(Color.white);
//	g.drawRect(getX(), getY(), getWidth(), getHeight());
    }
    
}
