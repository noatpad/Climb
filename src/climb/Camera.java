
package climb;

public class Camera {
    public static int x, y;
//    private static int minOffsetX, minOffsetY, maxOffsetX, maxOffsetY;
    
    /**
     * Initialize camera for the main menu
     */
    public static void menuInit() {
	x = 0;
	y = 0;
    }
    
    /**
     * Initialize camera for a level
     * @param area - Current area player is in
     */
    public static void levelInit(Area area) {
	x = area.getPosX();
	y = area.getPosY();
    }
}
