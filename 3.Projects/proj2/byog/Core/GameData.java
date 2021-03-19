package byog.Core;

import byog.TileEngine.TETile;
import java.io.*;

/**store the TETile[][] object and player object.
 * @Authur
 */
public class GameData implements Serializable {

    //the actual world

    private static TETile[][] world;

    //Getter method for world.

    public static TETile[][] getWorld() {
        return world;
    }

    //The player object

    private static Player player;

    //getter method for player object

    public static Player getPlayer() {
        return player;
    }

    //Constructor with parameter.

    public GameData(TETile[][] w, Player p) {
        world = w;
        player = p;
    }

    //Constructor without parameter.

    public GameData() {
        world = null;
        player = null;
    }



}
