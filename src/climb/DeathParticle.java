
package climb;

import java.awt.Graphics;

public class DeathParticle extends Object {
    private int velX, velY;
    private Animation anim;
    
    public DeathParticle(int x, int y, int velX, int velY) {
	super(x, y, 32, 32);
	this.velX = velX;
	this.velY = velY;
	anim = new Animation(Assets.deathParticle, 50, 0, 3, 10);
    }

    @Override
    public void tick() {
	setX(getX() + velX);
	setY(getY() + velY);
	
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
