
package climb;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

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
		line = br.readLine();	    // Skip empty line
		
		lvl.getAreas().add(new Area(i));
		
		// Read room position
		line = br.readLine();
		elements = line.split(",");
		lvl.getAreas().get(i).setPosX(Integer.parseInt(elements[0]));
		lvl.getAreas().get(i).setPosY(Integer.parseInt(elements[1]));
		
		// Read spawn point of room
		line = br.readLine();
		elements = line.split(",");
		lvl.getAreas().get(i).setSpawnX(Integer.parseInt(elements[0]));
		lvl.getAreas().get(i).setSpawnY(Integer.parseInt(elements[1]));
		
		// Read number of boundaries in room
		line = br.readLine();
		y = Integer.parseInt(line);
		for (int j = 0; j < y; j++) {	    // Repeat for each boundary
		    // Read boundary dimensions
		    line = br.readLine();
		    elements = line.split(",");
		    lvl.getAreas().get(i).getBounds().add(new Boundary(
			    Integer.parseInt(elements[0]), 
			    Integer.parseInt(elements[1]), 
			    Integer.parseInt(elements[2]), 
			    Integer.parseInt(elements[3])));
		}
		
		// Read number of load zones in room
		line = br.readLine();
		y = Integer.parseInt(line);
		
		for (int j = 0; j < y; j++) {	    // Repeat for each load zone
		    // Read load zone dimensions and room to transport to
		    line = br.readLine();
		    elements = line.split(",");
		    lvl.getAreas().get(i).getLoadZones().add(new LoadZone(
			    Integer.parseInt(elements[0]),
			    Integer.parseInt(elements[1]),
			    Integer.parseInt(elements[2]),
			    Integer.parseInt(elements[3]),
			    Integer.parseInt(elements[4])));
		}
	    }
	} catch (IOException ioe) {
	    System.err.println("Error: Unable to find level file?! " + ioe.toString());
	}
    }
}
