
package climb;

public class Camera {
    public static int x, y;
//    private static int minOffsetX, minOffsetY, maxOffsetX, maxOffsetY;
    
    public static void menuInit() {
	x = 0;
	y = 0;
    }
    
    public static void levelInit(Area area) {
	x = area.getPosX();
	y = area.getPosY();
    }
}
