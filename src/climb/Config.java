
package climb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;

public class Config {
    /* THE FOLLOWING IS JUST A TEMPLATE FOR FUTURE DEVELOPMENT */
    
    public static int music, sound;			    // Music and sound volumes
    public static boolean staminaDisplay, cameraShake;	    // Booleans to allow displaying stamina and shaking camera
    public static int buttonMap[];			    // Controller button mapping
    
    public static void loadConfig() {
	BufferedReader br;
	FileReader fr;
	
	URL url = Files.class.getResource("config.cfg");
	try {
	    fr = new FileReader(new File(url.getPath()));
	    br = new BufferedReader(fr);
	    
	    String line = br.readLine();
	    music = Integer.parseInt(line);
	    line = br.readLine();
	    sound = Integer.parseInt(line);
	    line = br.readLine();
	    staminaDisplay = Boolean.parseBoolean(line);
	    line = br.readLine();
	    cameraShake = Boolean.parseBoolean(line);
	} catch (IOException ioe) {
	    System.err.println("ERROR: Unable to find configuration file! Creating one for you... " + ioe.toString());
	    try {
		PrintWriter pw = new PrintWriter("config.cfg");
		pw.println(10);
		pw.println(10);
		pw.println("true");
		pw.println("true");
		pw.close();
		music = 10;
		sound = 10;
		staminaDisplay = true;
		cameraShake = true;
	    } catch (IOException ioe2) {
		System.err.println("ERROR: Unable to create config file! " + ioe.toString());
	    }
	}
    }
}
