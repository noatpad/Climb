
package climb;

import java.awt.Graphics;

public abstract class Object {
    protected int x, y, width, height;

    public Object(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }

    public void setX(int x) {
	this.x = x;
    }

    public void setY(int y) {
	this.y = y;
    }

    public void setWidth(int width) {
	this.width = width;
    }

    public void setHeight(int height) {
	this.height = height;
    }

    public int getX() {
	return x;
    }

    public int getY() {
	return y;
    }

    public int getWidth() {
	return width;
    }

    public int getHeight() {
	return height;
    }
    
    public abstract void tick();
    
    public abstract void render(Graphics g);
}
