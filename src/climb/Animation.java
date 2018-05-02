
package climb;

import java.awt.image.BufferedImage;

public class Animation {
    private int speed;			    // Speed of which the animation loops
    private int index, start, end;	    // Index of array of frames
    private boolean right;		    // Determine to use animation for facing right (2nd half of animation)
    private boolean loop;		    // Boolean to determine looping
    private long lastTime;		    // last registered time
    private long timer;			    // time passed
    private BufferedImage[] frames;	    // BufferedImage array of frames

    /**
     * <b>Animation</b> constructor
     * @param frames Array of sprites
     * @param speed Speed the animation will run as
     * @param loop If the animation can be looped
     */
    public Animation(BufferedImage[] frames, int speed, boolean loop) {
	this.frames = frames;
	this.speed = speed;
	this.loop = loop;
	right = true;
	index = 0;
	start = 0;
	end = frames.length;
	timer = 0;
	lastTime = System.currentTimeMillis();
    }
    
    /**
     * <b>Animation</b> constructor with specified index, start frame, & end frame
     * @param frames Array of sprites
     * @param speed Speed the animation will run as
     * @param index Starting frame index
     * @param start Starting frame of loop
     * @param end Last frame of loop
     */
    public Animation(BufferedImage[] frames, int speed, int index, int start, int end) {
	this.frames = frames;
	this.speed = speed;
	this.index = index;
	this.start = start;
	this.end = end;
	loop = true;
	right = true;
	timer = 0;
	lastTime = System.currentTimeMillis();
    }
    
    /* SETTERS AND GETTERS */

    /**
     * <code>speed</code> Setter
     * @param speed to modify
     */
    public void setSpeed(int speed) {
	this.speed = speed;
    }

    /**
     * <code>speed</code> Getter
     * @return speed
     */
    public int getSpeed() {
	return speed;
    }

    /**
     * Gets current frame of the animation
     * @return frame in frames[index]
     */
    public BufferedImage getCurrentFrame() {
	return frames[index];
    }
    
    /* METHODS */
    
    /**
     * Resets animation back to its start
     */
    public void reset() {
	// Depending on the player's direction, the range of frames is decided (frames facing right are the first half of the frames, while the other has the frames facing left
	if (right) {
	    start = 0;
	    end = frames.length / 2;
	} else {
	    start = frames.length / 2 + 1;
	    end = frames.length;
	}
	
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
	    if (index >= end) {    // if index goes out of bounds, start back at 0
		index = start;
	    }
	}
    }
    
    /**
     * Tick update with player direction involved
     * @param facingRight Player's current direction
     */
    public void tick(boolean facingRight) {
	timer += System.currentTimeMillis() - lastTime;	    // accumulate timer
	lastTime = System.currentTimeMillis();		    // updates lastTime
	
	if (right != facingRight) {
	    right = facingRight;
	    reset();
	}
	
	if (timer > speed) {
	    timer = 0;
	    index++;
	    if (index >= end) {
		if (loop) {
		    index = start;
		} else {
		    index--;
		}
	    }
	}
    }
}
