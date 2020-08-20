package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/** A room : the length and width of a room is bigger or equal to 3.
 * @author */
public class Room implements MapElement {

    /**
     * length of room.
     */
    private int l;

    /**
     * width of room.
     */
    private int w;

    /**
     * Position of Room, p is locate at the center of room.
     */
    private Position p;

    /**
     * check if there is a hallway on its left, right, bottom, or top.
     * 0:left, 1:bottom, 2:right, 3:top
     */
    private boolean[] explored = new boolean[4];

    // takes length and width of the room
    public Room(int L, int W, Position P) {
        l = L;
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
