
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
}
