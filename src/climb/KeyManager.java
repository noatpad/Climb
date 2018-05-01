
package climb;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyManager implements KeyListener {
    private boolean keys[];		    // to store held-down key flags
    private boolean pressRegistered[];	    // to store when key press has been acknowledged

    /**
     * KeyManager constructor
     */
    public KeyManager() {
	keys = new boolean[256];
	pressRegistered = new boolean[256];
    }
    
    /**
     * Returns if key is currently pressed down
     * @param ke Keyboard number identifier
     * @return If specified key is held down
     */
    public boolean pressed(int ke) {
	return keys[ke];
    }
    
    /**
     * Returns if key has been pressed (registered input then output)
     * @param ke Keyboard number identifier
     * @return If specified key was pressed/typed
     */
    public boolean typed(int ke) {
	if (keys[ke] && !pressRegistered[ke]) {
	    pressRegistered[ke] = true;
	    return true;
	}
	return false;
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
	keys[e.getKeyCode()] = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
	keys[e.getKeyCode()] = false;
	pressRegistered[e.getKeyCode()] = false;
    }

    /**
     * Update key flags with each tick
     */
    public void tick() {}
}