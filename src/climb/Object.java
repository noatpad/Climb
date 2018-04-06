
package climb;

import java.awt.Graphics;

public abstract class Object {
    protected int x, y, width, height;

    /**
     * Object Constructor
     * @param x - X coordinate of object
     * @param y - Y coordinate of object
     * @param width - width of object
     * @param height - height of object
     */
    public Object(int x, int y, int width, int height) {
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
    }
    
    /* GETTERS AND SETTERS */

    /**
     * x Setter
     * @param x to modify
     */
    public void setX(int x) {
	this.x = x;
    }

    /**
     * y Setter
     * @param y to modify 
     */
    public void setY(int y) {
	this.y = y;
    }

    /**
     * width Setter
     * @param width to modify 
     */
    public void setWidth(int width) {
	this.width = width;
    }

    /**
     * height Setter
     * @param height to modify 
     */
    public void setHeight(int height) {
	this.height = height;
    }

    /**
     * x Getter
     * @return x
     */
    public int getX() {
	return x;
    }

    /**
     * y Getter
     * @return y
     */
    public int getY() {
	return y;
    }

    /**
     * width Getter
     * @return width
     */
    public int getWidth() {
	return width;
    }

    /**
     * height Getter
     * @return height
     */
    public int getHeight() {
	return height;
    }
    
    /* METHODS */
    
    public abstract void tick();
    
    public abstract void render(Graphics g);
}
