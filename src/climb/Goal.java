
package climb;

import java.awt.Graphics;
import java.awt.Rectangle;

public class Goal extends Object {
    private Rectangle goal;
    private int tentPosX, tentPosY;
    private int firePosX, firePosY;
    private Animation tent, fire;
    
    public Goal(int x, int y, int width, int height, int tentPosX, int firePosX) {
	super(x, y, width, height);
	
	goal = new Rectangle(x, y, width, height);
	
	this.tentPosX = tentPosX;
	tentPosY = y + height - 66;
	this.firePosX = firePosX;
	firePosY = y + height - 64;
	
	tent = new Animation(Assets.tent, 150, true);
	fire = new Animation(Assets.campfire, 90, true);
    }

    @Override
    public void tick() {
	tent.tick();
	fire.tick();
    }

    @Override
    public void render(Graphics g) {
	g.drawImage(tent.getCurrentFrame(), tentPosX, tentPosY, 126, 66, null);
	g.drawImage(fire.getCurrentFrame(), firePosX, firePosY, 40, 64, null);
    }

}
