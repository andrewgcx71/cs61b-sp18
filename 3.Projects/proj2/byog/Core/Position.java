package byog.Core;

import java.io.Serializable;
//Represent a direction of a room/hallway (center position, the length and width are odd numbers only) or a player.
public class Position implements Serializable {
    // x-axis
    private int x;

    //y-axis
    private int y;

    /**Constructor, take the position*/
    public Position (int X, int Y) {
        x = X;
        y = Y;
    }
    /** Getter method for instance variable x. */
    public int getX() {
        return x;
    }

    /** Getter method for instance variable x. */
    public int getY() { return y; }

    /** Update x and y */
    public void update(int X, int Y) {
        x = X;
        y = Y;
    }
}
