
package climb;

import java.awt.image.BufferedImage;

public class Animation {
    private int speed;			    // Speed of which the animation loops
    private int index, start, end;	    // Index of array of frames
    private boolean loop;		    // Boolean to determine looping
    private long lastTime;		    // last registered time
    private long timer;			    // time passed
    private BufferedImage[] frames;	    // BufferedImage array of frames

    /**
     * <b>Animation</b> constructor with specified frames and speed
     *
     * @param frames is the array of sprites
     * @param speed is the speed the animation will run as
     */
    public Animation(BufferedImage[] frames, int speed, boolean loop) {
	this.frames = frames;
	this.speed = speed;
	this.loop = loop;
	index = 0;
	start = 0;
	end = frames.length;
	timer = 0;
	lastTime = System.currentTimeMillis();
    }
    
    /* SETTERS AND GETTERS */

    /**
     * <code>speed</code> Setter
     *
     * @param speed to modify
     */
    public void setSpeed(int speed) {
	this.speed = speed;
    }

    /**
     * <code>speed</code> Getter
     *
     * @return speed
     */
    public int getSpeed() {
	return speed;
    }

    /**
     * Gets current frame of the animation
     *
     * @return frame in frames[index]
     */
    public BufferedImage getCurrentFrame() {
	return frames[index];
    }
    
    /* METHODS */
    
    public void reset() {
	index = start;
	timer = 0;
	lastTime = 0;
    }

    /**
     * Tick update
     */
    public void tick() {
	timer += System.currentTimeMillis() - lastTime;	    // accumulate timer
	lastTime = System.currentTimeMillis();		    // updates lastTime
	if (loop && timer > speed) {	    // When timer surpasses speed, go to next frame index
	    timer = 0;
	    index++;
	    if (index >= frames.length) {    // if index goes out of bounds, start back at 0
		index = 0;
	    }
	}
    }
    
    public void tick(boolean facingRight) {
	timer += System.currentTimeMillis() - lastTime;	    // accumulate timer
	lastTime = System.currentTimeMillis();		    // updates lastTime
	
	if (facingRight) {
	    start = 0;
	    end = frames.length / 2;
	    if (index > end) {
		index = end;
	    }
	} else {
	    start = frames.length / 2 + 1;
	    end = frames.length; 
	    if (index < start) {
		index = start;
	    }
	}
	
	if (timer > speed) {	    // When timer surpasses speed, go to next frame index
	    timer = 0;
	    if (loop) {
		index++;
		if (index >= end) {    // if index goes out of bounds, start back at 0
		    index = start;
		}
	    }
	}
    }
}
