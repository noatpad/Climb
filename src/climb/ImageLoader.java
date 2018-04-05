
package climb;

import java.awt.image.BufferedImage;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Danny
 */
public class ImageLoader {

    /**
     * Load image from file path
     *
     * @param path is the path to image file
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
     *
     * @param path is the path to the image file
     * @param x is the x coordinate of the image
     * @param y is the y coordinate of the image
     * @param width is the width of the image
     * @param height is the height of the image
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
