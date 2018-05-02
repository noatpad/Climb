
package climb;

import java.awt.Graphics;
import java.util.ArrayList;

public class Area {
    private int roomID;				// Room ID number
    private int posX, posY, spawnX, spawnY;	// Room position (by its top-left corner) / Spawn point position
    private ArrayList<Boundary> bounds;		// List of Boundaries in room
    private ArrayList<LoadZone> loadZones;	// List of LoadZones in room

    /**
     * Area Constructor
     * @param roomID Room identifier
     */
    public Area(int roomID) {
	this.roomID = roomID;
	bounds = new ArrayList<>();
	loadZones = new ArrayList<>();
    }
    
    /* GETTERS AND SETTERS */
    
    /**
     * posX Setter
     * @param posX to modify  
     */
    public void setPosX(int posX) {
	this.posX = posX;
    }

    /**
     * posY Setter
     * @param posY to modify 
     */
    public void setPosY(int posY) {
	this.posY = posY;
    }

    /**
     * spawnX Setter
     * @param spawnX to modify 
     */
    public void setSpawnX(int spawnX) {
	this.spawnX = spawnX;
    }

    /**
     * spawnY Setter
     * @param spawnY to modify 
     */
    public void setSpawnY(int spawnY) {
	this.spawnY = spawnY;
    }

    /**
     * posX Getter
     * @return posX
     */
    public int getPosX() {
	return posX;
    }

    /**
     * posY Getter
     * @return posY
     */
    public int getPosY() {
	return posY;
    }

    /**
     * spawnX Getter
     * @return spawnX
     */
    public int getSpawnX() {
	return spawnX;
    }

    /**
     * spawnY Getter
     * @return spawnY
     */
    public int getSpawnY() {
	return spawnY;
    }

    /**
     * bounds Getter
     * @return bounds
     */
    public ArrayList<Boundary> getBounds() {
	return bounds;
    }

    /**
     * loadZones Getter
     * @return loadZones
     */
    public ArrayList<LoadZone> getLoadZones() {
	return loadZones;
    }
    
    public void render(Graphics g) {
	for (Boundary b : bounds) {
	    b.render(g);
	}
	
	for (LoadZone l : loadZones) {
	    l.render(g);
	}
    }
}
