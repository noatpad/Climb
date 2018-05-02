
/* Credit where due:
Montserrat Font -> https://fonts.google.com/specimen/Montserrat
*/

package climb;

import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Assets {
    public static Font font;			    // Font used in-game
    
    public static BufferedImage title_bg;	    // Background image for title screen
    
    public static BufferedImage level_bg;	    // Background image for level screen
    public static BufferedImage tile[];		    // Tile images for textures
    public static BufferedImage spikes[];	    // Images for spikes
    
    public static BufferedImage stand[], run[], jumpFall[], boost[], climb[];	    // Animation frames for players
    public static BufferedImage deathParticle[];    // Player death particles
    
    /**
     * Initialize assets on first load
     */
    public static void firstInit() {
	// Load font "Montserrat"
	try {
	    InputStream is = Main.class.getResourceAsStream("/fonts/montserrat.ttf");
	    font = Font.createFont(Font.TRUETYPE_FONT, is).deriveFont(24f);
	} catch (IOException | FontFormatException ie) {
	    ie.printStackTrace();
	}
	
	menuInit();	// Call menuInit() since first load lands on the main menu
	playerInit();	// Call playerInit() too
    }
    
    /**
     * Initialize main menu assets
     */
    public static void menuInit() {
	title_bg = ImageLoader.loadImage("/images/title_bg.png");
    }
    
    /**
     * Initializes level assets
     */
    public static void levelInit() {
	level_bg = ImageLoader.loadImage("/images/lvl_bg.png");
	
	tile = new BufferedImage[5];
	tile[0] = ImageLoader.loadImage("/images/tile.png", 0, 0, 24, 24);
	tile[1] = ImageLoader.loadImage("/images/tile.png", 24, 0, 24, 10);
	tile[2] = ImageLoader.loadImage("/images/tile.png", 48, 0, 10, 24);
	tile[3] = ImageLoader.loadImage("/images/tile.png", 72, 0, 24, 10);
	tile[4] = ImageLoader.loadImage("/images/tile.png", 96, 0, 10, 24);
	
	spikes = new BufferedImage[4];
	for (int i = 0; i < 4; i++) {
	    spikes[i] = ImageLoader.loadImage("/images/spike.png", i * 9, 0, 9, 9);
	}
    }
    
    /**
     * Initializes player assets
     */
    public static void playerInit() {
	Spritesheet ss = new Spritesheet(ImageLoader.loadImage("/images/playersheet.png"));
	stand = new BufferedImage[8];
	for (int i = 0; i < 8; i++) {
	    stand[i] = ss.crop(i * 25, 0, 25, 40);
	}
	run = new BufferedImage[22];
	for (int i = 0; i < 22; i++) {
	    run[i] = ss.crop(i * 25, 40, 25, 40);
	}
	jumpFall = new BufferedImage[10];
	for (int i = 0; i < 10; i++) {
	    jumpFall[i] = ss.crop(i * 25, 80, 25, 40);
	}
	boost = new BufferedImage[2];
	for (int i = 0; i < 2; i++) {
	    boost[i] = ss.crop(i * 25, 120, 25, 40);
	}
	climb = new BufferedImage[8];
	for (int i = 0; i < 8; i++) {
	    climb[i] = ss.crop(i * 25, 160, 25, 40);
	}
	
	ss = new Spritesheet(ImageLoader.loadImage("/images/deathParticle.png"));
	deathParticle = new BufferedImage[11];
	for (int i = 0; i < 11; i++) {
	    deathParticle[i] = ss.crop(i * 32, 0, 32, 32);
	}
    }
}
