
package climb;

import java.awt.Graphics;

public class DeathParticle extends Object {
    private int velX, velY;	// Velocities of the particle
    private Animation anim;	// Sprite animation
    
    /**
     * DeathParticle constructor
     * @param x X coordinate of particle
     * @param y Y coordinate of particle
     * @param velX Velocity in x axis
     * @param velY Velocity in y axis
     */
    public DeathParticle(int x, int y, int velX, int velY) {
	super(x, y, 32, 32);
	this.velX = velX;
	this.velY = velY;
	anim = new Animation(Assets.deathParticle, 50, 0, 3, 10);
    }

    @Override
    public void tick() {
	// Update positions
	setX(getX() + velX);
	setY(getY() + velY);
	
	// Lower velocities overtime
	if (velX < 0) {
	    velX++;
	} else if (velX > 0) {
	    velX--;
	}
	if (velY < 0) {
	    velY++;
	} else if (velY > 0) {
	    velY--;
	}
	anim.tick();
    }

    @Override
    public void render(Graphics g) {
	g.drawImage(anim.getCurrentFrame(), getX(), getY(), null);
    }

}
