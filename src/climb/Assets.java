
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
    public static Font font;
    
    public static BufferedImage title_bg;
    
    public static BufferedImage level_bg;
    public static BufferedImage tile[];
    
    /**
     * Initialize Assets on first load
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
    }
    
    /**
     * Initialize Assets for the menu
     */
    public static void menuInit() {
	title_bg = ImageLoader.loadImage("/images/title_bg.png");
    }
    
    public static void levelInit() {
	level_bg = ImageLoader.loadImage("/images/lvl_bg.png");
	
	tile = new BufferedImage[5];
	tile[0] = ImageLoader.loadImage("/images/tile.png", 0, 0, 16, 16);
	tile[1] = ImageLoader.loadImage("/images/tile.png", 16, 0, 16, 6);
	tile[2] = ImageLoader.loadImage("/images/tile.png", 32, 0, 6, 16);
	tile[3] = ImageLoader.loadImage("/images/tile.png", 48, 0, 16, 6);
	tile[4] = ImageLoader.loadImage("/images/tile.png", 64, 0, 6, 16);
    }
}
