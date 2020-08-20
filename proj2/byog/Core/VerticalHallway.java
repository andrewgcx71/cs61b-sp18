package byog.Core;

/** A vertical hallway, where variable w is a constant and is always equal 1.
 * @author */
public class VerticalHallway implements MapElement {
    /**
     * length of hallway.
     */
    private int l;

    /**
     * width of hallway which is a constant.
     */
    private int w = 1;


    /**
     * check if there is a hallway on its left, right, bottom, or top.
     * 0:left, 1:bottom, 2:right, 3:top
     */
    private boolean[] explored = new boolean[4];

    /**
     * Position of Room, p is locate at the center of room.
     */
    private Position p;

    // takes length and width of the room
    public VerticalHallway(int L, Position P) {
        l = L;
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
