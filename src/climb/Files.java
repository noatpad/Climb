
package climb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

public class Files {
    /**
     * Load level elements from a specified file
     * @param lvl Level instance to modify
     * @param lvlNum Level number (for selecting correct file)
     * @param areaNum Area number. Default to 0 to place player at start of level, but can be used as a save-point of sorts
     */
    public static void loadLevel(Level lvl, int lvlNum, int areaNum) {
	BufferedReader br;
	FileReader fr;
	
	URL url = Files.class.getResource("/levels/" + Integer.toString(lvlNum) + ".lvl");
	try {
	    // Setup file reading
	    fr = new FileReader(new File(url.getPath()));
	    br = new BufferedReader(fr);
	    
	    int x, y;
	    String line;
	    String[] elements;
	    
	    // Read number of areas/rooms within the level
	    line = br.readLine();
	    x = Integer.parseInt(line);
	    for (int i = 0; i < x; i++) {	// Repeat for each area
		// Read number of boundaries in room
		lvl.getLevelBounds().add(new ArrayList<>());
		line = br.readLine();
		y = Integer.parseInt(line);
		
		// Read spawn point position in room
		line = br.readLine();
		elements = line.split(",");
		lvl.getSpawnX().add(Integer.parseInt(elements[0]));
		lvl.getSpawnY().add(Integer.parseInt(elements[1]));
		
		for (int j = 0; j < y; j++) {	    // Repeat for each boundary
		    // Read boundary dimensions
		    line = br.readLine();
		    elements = line.split(",");
		    lvl.getLevelBounds().get(i).add(new Boundary(
			    Integer.parseInt(elements[0]), 
			    Integer.parseInt(elements[1]), 
			    Integer.parseInt(elements[2]), 
			    Integer.parseInt(elements[3])));
		}
	    }
	} catch (IOException ioe) {
	    System.err.println("Error: Unable to find level file?! " + ioe.toString());
	}
    }
}
