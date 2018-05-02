
package climb;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Files {
    /**
     * Load level elements from a specified file
     * @param lvl Level instance to modify
     * @param lvlNum Level number (for selecting correct file)
     * @param areaNum Area number. Default to 0 to place player at start of level, but can be used as a save-point of sorts
     */
    public static void loadLevel(Level lvl, int lvlNum, int areaNum) {
	BufferedReader br;
	// InputStream gets corresponding level file
	InputStream is = Files.class.getResourceAsStream("/levels/" + Integer.toString(lvlNum) + ".lvl");
	
	try {
	    // Setup file reading
	    br = new BufferedReader(new InputStreamReader(is));

	    int x, y;
	    String line;
	    String[] elements;
	    
	    // Read number of areas/rooms within the level
	    line = br.readLine();
	    x = Integer.parseInt(line);
	    for (int i = 0; i < x; i++) {	// Repeat for each area
		line = br.readLine();	    // Skip empty line
		
		lvl.getAreas().add(new Area(i));
		
		// Read room position [x, y]
		line = br.readLine();
		elements = line.split(",");
		lvl.getAreas().get(i).setPosX(Integer.parseInt(elements[0]));
		lvl.getAreas().get(i).setPosY(Integer.parseInt(elements[1]));
		
		// Read spawn point of room [x, y]
		line = br.readLine();
		elements = line.split(",");
		lvl.getAreas().get(i).setSpawnX(Integer.parseInt(elements[0]));
		lvl.getAreas().get(i).setSpawnY(Integer.parseInt(elements[1]));
		
		// Read number of boundaries in room
		line = br.readLine();
		y = Integer.parseInt(line);
		for (int j = 0; j < y; j++) {	    // Repeat for each boundary
		    // Read boundary dimensions [x, y, width, height] & boundary flags [ground, wallL, wallR, ceiling]
		    line = br.readLine();
		    elements = line.split(",");
		    lvl.getAreas().get(i).getBounds().add(new Boundary(
			    Integer.parseInt(elements[0]) + lvl.getAreas().get(i).getPosX(), 
			    Integer.parseInt(elements[1]) + lvl.getAreas().get(i).getPosY(), 
			    Integer.parseInt(elements[2]), 
			    Integer.parseInt(elements[3]),
			    Integer.parseInt(elements[4]), 
			    Integer.parseInt(elements[5]),
			    Integer.parseInt(elements[6]), 
			    Integer.parseInt(elements[7])));
		}
		
		// Read number of load zones in room
		line = br.readLine();
		y = Integer.parseInt(line);
		
		for (int j = 0; j < y; j++) {	    // Repeat for each load zone
		    // Read load zone direction, dimensions, and room to transport to [direction, startPos, endPos, toArea]
		    int x1 = 0, y1 = 0, x2 = 0, y2 = 0;
		    line = br.readLine();
		    elements = line.split(",");
		    switch(elements[0]) {
			case "0":	// top part of the screen
			    x1 = Integer.parseInt(elements[1]);
			    x2 = Integer.parseInt(elements[2]);
			    y1 = y2 = 0;
			    break;
			case "1":	// right part of the screen
			    x1 = x2 = lvl.getGame().getWidth();
			    y1 = Integer.parseInt(elements[1]);
			    y2 = Integer.parseInt(elements[2]);
			    break;
			case "2":	// bottom part of the screen
			    x1 = Integer.parseInt(elements[1]);
			    x2 = Integer.parseInt(elements[2]);
			    y1 = y2 = lvl.getGame().getHeight();
			    break;
			case "3":	// left part of the screen
			    x1 = x2 = 0;
			    y1 = Integer.parseInt(elements[1]);
			    y2 = Integer.parseInt(elements[2]);
			    break;
			default: 
			    System.err.println("ERROR: Couldn't read load zone direction! " + elements[0]);
			    break;
		    }
		    lvl.getAreas().get(i).getLoadZones().add(new LoadZone(
			    x1 + lvl.getAreas().get(i).getPosX(),
			    y1 + lvl.getAreas().get(i).getPosY(),
			    x2 + lvl.getAreas().get(i).getPosX(),
			    y2 + lvl.getAreas().get(i).getPosY(),
			    Integer.parseInt(elements[0]),
			    Integer.parseInt(elements[3])));
		}
		
		// Read number of spike areas
		line = br.readLine();
		y = Integer.parseInt(line);
		
		for (int j = 0; j < y; j++) {
		    line = br.readLine();
		    elements = line.split(",");
		    
		    int x1 = 0, y1 = 0, width = 0, height = 0;
		    Boundary b = lvl.getAreas().get(i).getBounds().get(Integer.parseInt(elements[0]));
		    switch (elements[1]) {
			case "0":	    // Top part of boundary
			    x1 = b.getX();
			    y1 = b.getY() - 18;
			    width = b.getWidth();
			    height = 18;
			    break;
			case "1":	    // Right part of boundary
			    x1 = b.getX() + b.getWidth();
			    y1 = b.getY();
			    width = 18;
			    height = b.getHeight();
			    break;
			case "2":	    // Bottom part of boundary
			    x1 = b.getX();
			    y1 = b.getY() + b.getHeight();
			    width = b.getWidth();
			    height = 18;
			    break;
			case "3":	    // Left part of boundary
			    x1 = b.getX() - 18;
			    y1 = b.getY();
			    width = 18;
			    height = b.getHeight();
			default:
			    System.err.println("ERROR: Couldn't read spike specifications! " + elements[0]);
			    break;
		    }
		    lvl.getAreas().get(i).getSpikes().add(new Spikes(x1, y1, width, height, Integer.parseInt(elements[1])));
		}
	    }
	    
	    line = br.readLine();	// Skip empty line
	    line = br.readLine();
	    elements = line.split(",");
	    
	    // Goal area of level
	    Area a = lvl.getAreas().get(Integer.parseInt(elements[0]));
	    lvl.setGoal(new Goal(
		    Integer.parseInt(elements[1]) + a.getPosX(),
		    Integer.parseInt(elements[2]) + a.getPosY(),
		    Integer.parseInt(elements[3]),
		    Integer.parseInt(elements[4]),
		    Integer.parseInt(elements[5]) + a.getPosX(),
		    Integer.parseInt(elements[6]) + a.getPosX()));
	} catch (IOException ioe) {
	    System.err.println("ERROR: Unable to find level file?! " + ioe.toString());
	}
    }
}
