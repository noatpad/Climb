
package climb;

import java.awt.image.BufferedImage;

public class Spritesheet {

    private BufferedImage sheet;	// Spritesheet

    /**
     * <b>SpriteSheet</b> constructor
     * @param sheet Spreadsheet in question
     */
    public Spritesheet(BufferedImage sheet) {
	this.sheet = sheet;
    }

    /**
     * Crop a sprite from sprite sheet
     * @param x X position
     * @param y Y position
     * @param width Width of the sprite
     * @param height Height of the sprite
     * @return Sprite from spritesheet
     */
    public BufferedImage crop(int x, int y, int width, int height) {
	return sheet.getSubimage(x, y, width, height);
    }
}
