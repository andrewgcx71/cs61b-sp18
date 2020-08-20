package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

public class Position {
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



}
