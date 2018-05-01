
package climb;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class Player extends Object {
    private Level lvl;				// Current level instance
    private Area area;				// Current area instance
    private Rectangle box, ledgeBox;		// Collision box & "ledge" box
    private StaminaBar stamina;			// Stamina
    private int velX, velY;			// Velocity
    private boolean facingRight;		// Boolean when facing right
    private boolean grounded, ceiling, walledL, walledR, climbing, jumping, wJumping;	// Booleans when touching a boundary & jumping
    private Boundary climbableBound;		// Boundary instance that can be climbed
    private boolean canBoost;			// Boolean when able to boost
    private int boostTimer;			// Timer before boost runs out
    private int jumpTimer, wallJumpTimer;	// Timers for jumps and wall jumps
    
    private Animation stand, run, jumpFall, climb, currentAnim;	    // Player animations
    private BufferedImage boostImg;				    // Frame of player boosting
    
    /**
     * Player constructor
     * @param x X coordinate of its position
     * @param y Y coordinate of its position
     * @param lvl Level instance
     * @param area Area instance
     */
    public Player(int x, int y, Level lvl, Area area) {
	super(x, y, 25, 40);
	this.lvl = lvl;
	this.area = area;
	
	box = new Rectangle(x, y, width, height);
	ledgeBox = new Rectangle(x + width / 2, y + height / 5, 1, 3 * height / 5);
	stamina = new StaminaBar(this);
	
	velX = 0; velY = 0;
	facingRight = true;
	grounded = false; ceiling = false;
	walledL = false; walledR = false;
	climbing = false; jumping = false; wJumping = false;
	
	canBoost = true;
	boostTimer = 0;
	jumpTimer = 0;
	wallJumpTimer = 0;
	
	stand = new Animation(Assets.stand, 300, true);
	run = new Animation(Assets.run, 20, true);
	jumpFall = new Animation(Assets.jumpFall, 40, false);
	climb = new Animation(Assets.climb, 30, true);
	currentAnim = stand;
	boostImg = Assets.boost[0];
    }
    
    /* GETTERS AND SETTERS */

    /**
     * area Setter
     * @param area to modify 
     */
    public void setArea(Area area) {
	this.area = area;
    }

    /**
     * canBoost Setter
     * @param canBoost to modify 
     */
    public void setCanBoost(boolean canBoost) {
	this.canBoost = canBoost;
    }

    /**
     * grounded Getter
     * @return grounded
     */
    public boolean isGrounded() {
	return grounded;
    }
    
    /**
     * climbing Getter
     * @return climbing
     */
    public boolean isClimbing() {
	return climbing;
    }
    
    /* METHODS */
    
    /**
     * Determines if player is hitting the corner of a boundary, by checking if the player's trajectory intersects with the wall
     * @param top True if hitting top corners, false if hitting bottom ones
     * @param x X coordinate of an important point of the player (depends on the situation)
     * @param y Y coordinate of an important point of the player (depends on the situation)
     * @param wall Rectangle of the wall in question
     * @return If player is hitting the wall (true) or the floor/ceiling (false)
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
     * @return Ability of player being able to climb
     */
    private boolean canClimb() {
	if (climbableBound == null) {
	    return false;
	}
	// Only be able to climb if ledgebox is within the wall's dimensions & have stamina
	return stamina.gotStamina() &&
		ledgeBox.y <= climbableBound.getWallL().y + climbableBound.getWallL().height &&
		ledgeBox.y + ledgeBox.height >= climbableBound.getWallL().y;
    }
    
    /**
     * Event when player climbs to the top of a wall
     */
    private void ledgeClimb() {
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
    private void wallJump(boolean up, boolean left, boolean right) {
	int jumpDir = 0;
	
	// velX is determined depending on what wall you're jumping from
	if (facingRight) {
	    // If climbing, wall jump can be controlled further with arrow keys
	    if (climbing) {
		jumpDir = (left ? 1 : 0) + (up ? 2 : 0);
	    }
	    switch (jumpDir) {
		case 1:	    // left/right key will make more horizontal distance when jumping
		    velX = -8;
		    velY = -2;
		    stamina.useStamina(4);
		    wallJumpTimer = 8;
		    break;
		case 2:	    // up key will make almost vertical jumps
		    velX = -1;
		    velY = -7;
		    stamina.useStamina(10);
		    wallJumpTimer = 5;
		    break;
		default:    // wall jumps without holding to a wall or no direction inputted will result in a regular diagonal jump
		    velX = -6;
		    velY = -6;
		    if (climbing) {
			stamina.useStamina(4);
		    }
		    wallJumpTimer = 8;
		    break;
	    }
	    walledR = false;
	    facingRight = false;
	} else {
	    if (climbing) {
		jumpDir = (right ? 1 : 0) + (up ? 2 : 0);
	    }
	    switch (jumpDir) {
		case 1:
		    velX = 8;
		    velY = -2;
		    stamina.useStamina(4);
		    wallJumpTimer = 8;
		    break;
		case 2:
		    velX = 1;
		    velY = -7;
		    stamina.useStamina(10);
		    wallJumpTimer = 5;
		    break;
		default:
		    velX = 6;
		    velY = -6;
		    if (climbing) {
			stamina.useStamina(4);
		    }
		    wallJumpTimer = 8;
		    break;
	    }
	    walledL = false;
	    facingRight = true;
	}
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
    
    /**
     * Set player animation
     * @param anim Animation to set player to
     */
    private void setAnim(Animation anim) {
	if (anim == currentAnim) {
	    return;
	}
	anim.reset();
	currentAnim = anim;
	currentAnim.tick();
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
	
	// LoadZone collision detection
	for (LoadZone l : area.getLoadZones()) {
	    if (l.getLoadLine().intersects(box)) {
		// Only trigger LoadZone if moving towards it
		boolean good = false;
		switch (l.getDirection()) {
		    case 0: good = velY < 0; break;
		    case 1: good = velX > 0; break;
		    case 2: good = velY > 0; break;
		    case 3: good = velX < 0; break;
		    default: break;
		}
		if (good) {
		    area = null;
		    lvl.startTransition(l);
		    return;
		}
	    }
	}
	
	// Boundary collision detection
	for (Boundary b : area.getBounds()) {
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
		setY((int) (b.getCeiling().getY() + b.getCeiling().getHeight()));
		velY = 0;
		ceiling = true;
	    }
	    if (l && !walledL) {	    // Left wall
		setX((int) (b.getWallR().getX() + b.getWallR().getWidth()) - 1);
		velX = 0;
		walledL = true;
	    } else if (r && !walledR) {	    // Right wall
		setX((int) b.getWallL().getX() - getWidth() + 1);
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
	    velY = (velY > 2 ? velY - 2 : velY);
	    facingRight = right;
	}
	
	// Determine when climbing
	if ((walledL || walledR) && canClimb() && boostTimer == 0) {
	    if (keyC) {	    // Holding the key will allow climbing
		climbing = true;
	    } else if (climbing && !keyC) {
		climbing = false;
	    }
	} else {
	    walledL = false;
	    walledR = false;
	}
	
	// Movement
	if (climbing) {	    // Vertical movement (climbing)
	    if ((up && down) || (!up && !down)) {	// No input/holding both up and down
		velY = 0;
	    } else if (up) {				// Holding up
		velY = -2;
		if (ledgeBox.y + ledgeBox.height + velY < climbableBound.getWallL().y) {
		    ledgeClimb();
		}
		stamina.setTickCounter(stamina.getTickCounter() + 1);
	    } else if (down && !grounded) {		// Holding down (and not at the ground)
		velY = 3;
		if (ledgeBox.y + velY > climbableBound.getWallL().y + climbableBound.getWallL().height) {
		    velY = 0;
		}
		stamina.setTickCounter(stamina.getTickCounter() - 1);
	    }
	    
	    setAnim(climb);
	    if (velY != 0) {
		climb.tick(facingRight);
	    }
	    
	    // If you run out of stamina while climbing, go back into a falling state
	    if (!stamina.gotStamina()) {
		climbing = false;
	    }
	} else if (boostTimer == 0 && wallJumpTimer == 0) {	    // Horizontal movement
	    if ((!left && !right) || (left && right)) {	    // No input/holding both left and right
		velX /= 1.4 - (velY != 0 ? .3 : 0);
	    } else if (left && !walledL) {		    // Holding left (and no wall is in the way)
		walledR = false;
		velX -= (velX > -4 ? 2 : 0);
	    } else if (right && !walledR) {		    // Holding right (and no wall is in the way)
		walledL = false;
		velX += (velX < 4 ? 2 : 0);
	    }
	    
	    // velX regulation if going too fast
	    if (velX > 4) {
		velX--;
	    } else if (velX < -4) {
		velX++;
	    }
	    
	    if (grounded) {
		if (velX == 0) {
		    setAnim(stand);
		    stand.tick(facingRight);
		} else {
		    setAnim(run);
		    run.tick(facingRight);
		}
	    }
	}
	
	// Extra stuff
	if (grounded && boostTimer == 0) {		// When on the ground
	    // Update facingRight depending on 'velX'
	    if (velX > 0) {
		facingRight = true;
	    } else if (velX < 0) {
		facingRight = false;
	    }
	    // Allow boosting again
	    canBoost = true;
	} else if (boostTimer == 0 && !climbing && velY < 8) {
	    velY++;
	    setAnim(jumpFall);
	    jumpFall.tick(facingRight);
	}
	
	// Jump
	if (lvl.getKeyMan().typed(KeyEvent.VK_SPACE) && boostTimer == 0) {
	    // First spacebar press will put the player in a 'jumping' state
	    if (grounded) {
		velY = -6;
		grounded = false;
		jumping = true;
	    } else if (walledL || walledR) {
		wallJump(up, left, right);
		wJumping = true;
	    }
	}
	
	// Depending on how long you hold the jump button, gravity remains unaffected to you (velY--)
	if (jumping) {
	    if (lvl.getKeyMan().pressed(KeyEvent.VK_SPACE) && jumpTimer < 9) {
		velY--;
		jumpTimer++;
		setAnim(jumpFall);
	    } else {
		jumping = false;
		jumpTimer = 0;
	    }
	}
	if (wJumping) {
	    if (lvl.getKeyMan().pressed(KeyEvent.VK_SPACE) && jumpTimer < 6) {
		velY--;
		jumpTimer++;
	    } else {
		wJumping = false;
		wallJumpTimer = 0;
		jumpTimer = 0;
	    }
	}
	
	// Wall jump timer
	if (wallJumpTimer > 0) {
	    wallJumpTimer--;
	}
	
	// Boost
	if (boostTimer > 0) {	    // Timer is used to disable gravity and movement
	    boostTimer--;
	    if (boostTimer == 0) {	// Slow down at the last frame of boosting
		velX /= 2;
		velY /= 2;
	    }
	}
	if (lvl.getKeyMan().typed(KeyEvent.VK_X) && canBoost) {
	    boostTimer = 9;
	    canBoost = false;
	    walledL = false; walledR = false; climbing = false;
	    velX = 0; velY = 0;
	    
	    /* Direction integer. The number represents a direction of the numpad-direction notation
		1   2	3
		4   5	6
		7   8	9
	    */
	    int dir = 5 + (left ? -1 : 0) + (right ? 1 : 0) + (up ? -3 : 0) + (down ? 3 : 0);
	    
	    switch (dir) {
		case 1:	    // up-left
		    velX = -7;
		    velY = -7;
		    facingRight = false;
		    break;
		case 2:	    // up
		    velY = -10;
		    break;
		case 3:	    // up-right
		    velX = 7;
		    velY = -7;
		    facingRight = true;
		    break;
		case 4:	    // left
		    velX = -11;
		    facingRight = false;
		    break;
		case 6:	    // right
		    velX = 11;
		    facingRight = true;
		    break;
		case 7:	    // down-left
		    velX = -7;
		    velY = 7;
		    facingRight = false;
		    break;
		case 8:	    // down
		    velY = 10;
		    break;
		case 9:	    // down-right
		    velX = 7;
		    velY = 7;
		    facingRight = true;
		    break;
		default:    // No direction inputted (boost forward based on 'facingRight')
		    velX = facingRight ? 11 : -11;
	    }
	    
	    boostImg = Assets.boost[(facingRight ? 0 : 1)];
	}

	// Update position with velocity
	setX(getX() + velX);
	setY(getY() + velY);
	
	// Collision position update
	updateBoxes();
	
	// Stamina bar tick()
	stamina.tick();
    }

    @Override
    public void render(Graphics g) {
//	g.setColor(Color.white);
//	g.drawRect(box.x, box.y, box.width, box.height);
//	g.setColor(Color.magenta);
//	g.drawRect(ledgeBox.x, ledgeBox.y, ledgeBox.width, ledgeBox.height);

	if (boostTimer == 0) {
	    g.drawImage(currentAnim.getCurrentFrame(), getX(), getY(), null);
	} else {
	    g.drawImage(boostImg, getX(), getY(), null);
	}
	stamina.render(g);
    }
}
