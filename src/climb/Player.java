
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;

public class Player extends Object {
    private Level lvl;						// Current level instance
    private Rectangle box, ledgeBox;				// Collision box & "ledge" box
    private int velX, velY;					// Velocity
    private boolean facingRight;				// Boolean when facing right
    private boolean grounded, climbing, walledL, walledR;	// Booleans when touching a boundary
    private Boundary climbableBound;				// Boundary instance that can be climbed
    
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
	grounded = false; climbing = false;
	walledL = false; walledR = false;
    }
    
    /* METHODS */
    
    /**
     * Determines if player is hitting the wall or the floor, by checking if the line between its current and previous position intersects with the wall
     * @param x - X coordinate of an important point of the player (depends on the situation)
     * @param y - Y coordinate of an important point of the player (depends on the situation)
     * @param wall - Rectangle of the wall in question
     * @return if player is hitting the wall (true) or the floor (false)
     */
    private boolean wallOrFloor(int x, int y, Rectangle wall) {
	Line2D line = new Line2D.Float(x, y, x - velX, y - velY);
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
	    velX = -10;
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
	grounded = false;
	
	// Boundary collision detection
	for (Boundary b : lvl.getBounds()) {
	    // Temporary booleans for walledL, walledR, and grounded (resets for each boundary)
	    boolean l = false, r = false, g = false;
	    
	    if (!(walledL || walledR)) {	// Only enter if walledL & walledR aren't true
		if (box.intersects(b.getWallR())) {		// If player touches the right wall
		    l = true;
		    climbableBound = b;
		} else if (box.intersects(b.getWallL())) {	// If player touches the left wall
		    r = true;
		    climbableBound = b;
		}
	    }
	    if (box.intersects(b.getFloor())) {	    // If player touches the floor
		g = true;
	    } else if (box.intersects(b.getCeiling()) && !grounded && !climbing) {	// If player touches the ceiling
		// TODO: Determine what happens here and when bumping into bottom corners of boundaries
		velY = 0;
//		setY(b.y + b.height);
	    }
	    
	    // Special cases when player touches a corner of a boundary
	    if (g && l) {	    // Top-right corner
		if (wallOrFloor(getX(), getY() + getHeight(), b.getWallR())) {
		    g = false;
		} else {
		    l = false;
		}
	    } else if (g && r) {    // Top-left corner
		if (wallOrFloor(getX() + getWidth(), getY() + getHeight(), b.getWallL())) {
		    g = false;
		} else {
		    r = false;		    
		}
	    }
	    // TODO: Special cases with bottom corners
	    
	    // Corrects position when first touching wall/floor/ceiling (the player would paritally clip through without this)
	    if (g && !grounded) {	    // Ground
		setY((int) (b.getFloor().getY() - getHeight()) + 1);
		velY = 0;
	    }
	    // TODO: Determine what happens when first hitting a ceiling
	    if (l && !walledL) {	    // Left wall
		setX((int) (b.getWallR().getX() + b.getWallR().getWidth()));
		velX = 0;
	    } else if (r && !walledR) {	    // Right wall
		setX((int) b.getWallL().getX() - getWidth());
		velX = 0;
	    }
	    
	    // Update booleans to true when possible
	    if (!grounded) {
		grounded = g;
	    }
	    if (!walledL) {
		walledL = l;
	    }
	    if (!walledR) {
		walledR = r;
	    }
	    
	    // If all necessary booleans equal to true, 'break' the loop
	    if (grounded && (walledL || walledR)) {
		break;
	    }
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
	
	// Movement optiions
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
	} else if (!climbing) {	    // When not climbing (in the air)
	    // Slide slower when pushing against the wall (and update facingRight, as well)
	    if ((walledL && left) || (walledR && right)) {
		velY = (velY > 3 ? velY - 2 : velY + 1);
		facingRight = right;
	    } else if (velY < 8) {	// Fall to a certain speed when freefalling
		velY++;
	    }
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
