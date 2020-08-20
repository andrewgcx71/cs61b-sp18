package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.util.Random;

/** A horizontal hallway : where variable l is a constant and is always equal 1.
 * @author */
public class HorizontalHallway implements MapElement {
    /**length of hallway. */
    private int w;

    /**width of hallway which is a constant. */
    private int l = 1;


    /** check if there is a hallway on its left, right, bottom, or top.
     *  0:left, 1:bottom, 2:right, 3:top
     */
    private boolean[] explored = new boolean[4];

    /** Position of Room, p is locate at the center of room.*/
    private Position p;

    // takes length and width of the room
    public HorizontalHallway(int W, Position P) {
        w = W;
        p = P;
    }

    @Override
    // getter method for explored array
    public Position getPosition() {
        return p;
    }
    @Override
    // getter method for explored array
    public boolean[] getExplored() {
        return explored;
    }

    @Override
    // getter method for length
    public int getLength() {
        return l;
    }

    @Override
    // getter method for width
    public int getWidth() {
        return w;
    }



}
