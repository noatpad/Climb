
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.geom.Line2D;

public class LoadZone {
    private int x1, y1, x2, y2;
    private Line2D loadLine;
    private int toArea;

    public LoadZone(int x1, int y1, int x2, int y2, int toArea) {
	this.x1 = x1;
	this.y1 = y1;
	this.x2 = x2;
	this.y2 = y2;
	loadLine = new Line2D.Float(x1, y1, x2, y2);
	this.toArea = toArea;
    }
    
    public void render(Graphics g) {
	g.setColor(Color.green);
	g.drawLine(x1, y1, x2, y2);
    }
}
