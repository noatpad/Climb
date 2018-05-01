
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

public class LoadZone {
    private int x1, y1, x2, y2;	    // LoadZone dimensions
    private int direction;	    // Direction it transports to
    private Line2D loadLine;	    // Collision detection
    private int toArea;		    // Area to transport to upon entering LoadZone

    /**
     * LoadZone Constructor
     * @param x1 X coordinate of starting point
     * @param y1 Y coordinate of starting point
     * @param x2 X coordinate of ending point
     * @param y2 Y coordinate of ending point
     * @param direction Player direction necessary to travel "towards" LoadZone
     * @param toArea Area to transport to
     */
    public LoadZone(int x1, int y1, int x2, int y2, int direction, int toArea) {
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	this.direction = direction;
	loadLine = new Line2D.Float(x1, y1, x2, y2);
	this.toArea = toArea;
    }
    
    /* GETTERS */

    /**
     * direction Getter
     * @return direction
     */
    public int getDirection() {
	return direction;
    }

    /**
     * loadLine Getter
     * @return loadLine
     */
    public Line2D getLoadLine() {
	return loadLine;
    }

    /**
     * toArea Getter
     * @return toArea
     */
    public int getToArea() {
	return toArea;
    }
    
    public void render(Graphics g) {
	g.setColor(Color.green);
	g.drawLine(x1, y1, x2, y2);
    }
}
