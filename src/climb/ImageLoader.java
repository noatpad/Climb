
package climb;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
    /**
     * Load image from file path
     * @param path Path to image file
     * @return <b>BufferedImage</b> object
     */
    public static BufferedImage loadImage(String path) {
	BufferedImage bi = null;
	try {
	    bi = ImageIO.read(ImageLoader.class.getResource(path));
	} catch (IOException ioe) {
	    System.out.println("Error loading image " + path + ioe.toString());
	    System.exit(1);
	}
	return bi;
    }

    /**
     * Load and crop a single image from file path
     * @param path Path to the image file
     * @param x X coordinate of the image
     * @param y Y coordinate of the image
     * @param width Width of the image
     * @param height Height of the image
     * @return <b>BufferedImage</b> object
     */
    public static BufferedImage loadImage(String path, int x, int y, int width, int height) {
	BufferedImage bi = null;
	try {
	    bi = ImageIO.read(ImageLoader.class.getResource(path));
	} catch (IOException ioe) {
	    System.out.println("Error loading image " + path + ioe.toString());
	    System.exit(1);
	}
	return bi.getSubimage(x, y, width, height);
    }
}
