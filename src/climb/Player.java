
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;

public class Player extends Object {
    private Level lvl;							// Current level instance
    private Rectangle box, ledgeBox;					// Collision box & "ledge" box
    private int velX, velY;						// Velocity
    private boolean facingRight;					// Boolean when facing right
    private boolean grounded, ceiling, walledL, walledR, climbing;	// Booleans when touching a boundary
    private Boundary climbableBound;					// Boundary instance that can be climbed
    
    /**
     * Player constructor
     * @param x - X coordinate of its position
     * @param y - Y coordinate of its position
     * @param width - Width of player
     * @param height - Height of player
     * @param lvl - Level instance
     */
    public Player(int x, int y, int width, int height, Level lvl) {
	super(x, y, width, height);
	this.lvl = lvl;
	
	box = new Rectangle(x, y, width, height);
	ledgeBox = new Rectangle(x + width / 2, y + height / 5, 1, 3 * height / 5);
	
	velX = 0; velY = 0;
	facingRight = true;
	grounded = false; ceiling = false;
	walledL = false; walledR = false;
	climbing = false;
    }
    
    /* METHODS */
    
    /**
     * Determines if player is hitting the corner of a boundary, by checking if the player's trajectory intersects with the wall
     * @param top - True if hitting top corners, false if hitting bottom ones
     * @param x - X coordinate of an important point of the player (depends on the situation)
     * @param y - Y coordinate of an important point of the player (depends on the situation)
     * @param wall - Rectangle of the wall in question
     * @return if player is hitting the wall (true) or the floor/ceiling (false)
     */
    private boolean cornerCollision(boolean top, int x, int y, Rectangle wall) {
	// If the point is below the wall, automatically assume it's hitting the wall
	if ((top && y > wall.y + wall.height) || (!top && y + getHeight() < wall.y)) {
	    return true;
	}
	Line2D line = new Line2D.Float(x - velX, y - velY, x + velX, y + velY);
	return line.intersects(wall);
    }
    
    /**
     * Determines if the player is able to climb a certain wall
     * @return ability of player being able to climb
     */
    private boolean canClimb() {
	if (climbableBound == null) {
	    return false;
	}
	// Only be able to climb if ledgebox is within the wall's dimensions
	return ledgeBox.y <= climbableBound.getWallL().y + climbableBound.getWallL().height &&
		ledgeBox.y + ledgeBox.height >= climbableBound.getWallL().y;
    }
    
    /**
     * Event when player climbs to the top of a wall
     */
    public void ledgeClimb() {
	// TODO: Develop this more instead of a 1-frame position change
	if (getX() < climbableBound.x) {
	    setX(climbableBound.x + 1);
	} else {
	    setX(climbableBound.x + climbableBound.width - getWidth() - 1);
	}
	setY(climbableBound.y - getHeight());
	velY = 0;
	
	walledL = false;
	walledR = false;
	climbing = false;
    }
    
    /**
     * Event when player jumps off a wall
     */
    public void wallJump() {
	if (facingRight) {
	    velX = -6;
	    walledR = false;
	    facingRight = false;
	} else {
	    velX = 10;
	    walledL = false;
	    facingRight = true;
	}
	velY = -12;
	climbing = false;
	grounded = false;
    }
    
    /**
     * Updates collision boxes
     */
    public void updateBoxes() {
	box.x = getX();
	box.y = getY();
	if (walledL) {
	    ledgeBox.x = getX() - 1;
	} else if (walledR) {
	    ledgeBox.x = getX() + getWidth();
	} else {
	    ledgeBox.x = getX() + getWidth() / 2;
	}
	ledgeBox.y = getY() + getHeight() / 5;
    }
    
    @Override
    public void tick() {
	boolean up = lvl.getKeyMan().pressed(KeyEvent.VK_UP),
		down = lvl.getKeyMan().pressed(KeyEvent.VK_DOWN),
		left = lvl.getKeyMan().pressed(KeyEvent.VK_LEFT),
		right = lvl.getKeyMan().pressed(KeyEvent.VK_RIGHT),
		keyC = lvl.getKeyMan().pressed(KeyEvent.VK_C);
	grounded = false; ceiling = false;
	
	// Determine collision box position in immediate next tick (for collision detection purposes)
	box.x = getX() + velX;
	box.y = getY() + velY;
	
	// Boundary collision detection
	for (Boundary b : lvl.getBounds()) {
	    // Temporary booleans for walledL, walledR, grounded, & hitting the ceiling (resets for each boundary)
	    boolean l = false, r = false, g = false, c = false;
	    
	    if (!(walledL || walledR)) {	// Only enter if walledL & walledR aren't true
		if (box.intersects(b.getWallR())) {		// If player touches the right wall
		    l = true;
		    climbableBound = b;
		}
		if (box.intersects(b.getWallL())) {	// If player touches the left wall
		    r = true;
		    climbableBound = b;
		}
	    }
	    if (box.intersects(b.getFloor())) {	    // If player touches the floor
		g = true;
	    }
	    if (box.intersects(b.getCeiling()) && !grounded && !climbing) {	// If player touches the ceiling
		c = true;
	    }
	    
	    // Special cases when player touches a corner of a boundary
	    if (g && l) {	    // Top-right corner
		if (cornerCollision(true, getX(), getY() + getHeight(), b.getWallR())) {
		    g = false;
		} else {
		    l = false;
		}
	    } else if (g && r) {    // Top-left corner
		if (cornerCollision(true, getX() + getWidth(), getY() + getHeight(), b.getWallL())) {
		    g = false;
		} else {
		    r = false;		    
		}
	    }
	    if (c && l) {
		if (cornerCollision(false, getX(), getY(), b.getWallR())) {
		    c = false;
		} else {
		    l = false;
		}
	    } else if (c && r) {
		if (cornerCollision(false, getX() + getWidth(), getY(), b.getWallL())) {
		    c = false;
		} else {
		    r = false;
		}
	    }
	    
	    // Corrects position when first touching wall/floor/ceiling (the player would paritally clip through without this)
	    if (g && !grounded) {	    // Ground
		setY((int) (b.getFloor().getY() - getHeight()) + 1);
		velY = 0;
		grounded = true;
	    } else if (c && !ceiling) {
		setY((int) (b.getCeiling().getY()));
		velY = 0;
		ceiling = true;
	    }
	    if (l && !walledL) {	    // Left wall
		setX((int) (b.getWallR().getX() + b.getWallR().getWidth()));
		velX = 0;
		walledL = true;
	    } else if (r && !walledR) {	    // Right wall
		setX((int) b.getWallL().getX() - getWidth());
		velX = 0;
		walledR = true;
	    }
	    
	    // If all necessary booleans equal to true, 'break' the loop
	    if ((grounded || ceiling) && (walledL || walledR)) {
		break;
	    }
	}
	
	// When sliding down the wall, slow down descent (and update 'facingRight')
	if (!climbing && ((walledL && left) || walledR && right)) {
	    velY = (velY > 3 ? velY - 2 : velY + 1);
	    facingRight = right;
	}
	
	// Determine when climbing
	if ((walledL || walledR) && canClimb()) {
	    if (keyC) {	    // Holding the key will allow climbing
		climbing = true;
	    } else if (climbing && !keyC) {
		climbing = false;
	    }
	} else {
	    walledL = false;
	    walledR = false;
	}
	
	// Movement options
	if (climbing) {	    // Vertical movement (climbing)
	    if ((up && down) || (!up && !down)) {	// No input/holding both up and down
		velY = 0;
	    } else if (up) {				// Holding up
		velY = -2;
		if (ledgeBox.y + ledgeBox.height + velY < climbableBound.getWallL().y) {
		    ledgeClimb();
		}
	    } else if (down && !grounded) {		// Holding down (and not at the ground)
		velY = 2;
		if (ledgeBox.y + velY > climbableBound.getWallL().y + climbableBound.getWallL().height) {
		    velY = 0;
		}
	    }
	} else {	    // Horizontal movement
	    if ((!left && !right) || (left && right)) {	    // No input/holding both left and right
		velX /= 2;
	    } else if (left && !walledL) {		    // Holding left (and no wall is in the way)
		walledR = false;
		velX -= (velX > -6 ? 2 : 0);
	    } else if (right && !walledR) {		    // Holding right (and no wall is in the way)
		walledL = false;
		velX += (velX < 6 ? 2 : 0);
	    }
	    
	    // velX regulation if going too fast
	    if (velX > 6) {
		velX--;
	    } else if (velX < -6) {
		velX++;
	    }
	}
	
	// Jump options
	if (lvl.getKeyMan().typed(KeyEvent.VK_SPACE)) {
	    if (grounded) {
		grounded = false;
		velY = -15;
	    } else if (walledL || walledR) {
		wallJump();
	    }
	}
	
	// Extra stuff
	if (grounded) {		// When on the ground
	    // Update facingRight depending on 'velX'
	    if (velX > 0) {
		facingRight = true;
	    } else if (velX < 0) {
		facingRight = false;
	    }
	} else if (!climbing && velY < 8) {
	    velY++;
	}

	// Update position with velocity
	setX(getX() + velX);
	setY(getY() + velY);
	
	// Collision position update
	updateBoxes();
    }

    @Override
    public void render(Graphics g) {
	g.setColor(Color.white);
	g.drawRect(box.x, box.y, box.width, box.height);
	g.setColor(Color.magenta);
	g.drawRect(ledgeBox.x, ledgeBox.y, ledgeBox.width, ledgeBox.height);
    }
}
