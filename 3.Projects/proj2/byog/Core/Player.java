package byog.Core;
import byog.TileEngine.*;

import java.io.*;

/** Player class: store and update position of player.*/
public class Player implements Serializable {
    //current position of player
    private static Position position;

    //Getter method of position.
    public static Position getPosition() {
        return position;
    }

    //Constructor: takes the initial position of player
    public Player(TETile[][] world, Position pos) {
        this.position = pos;
        int x = position.getX();
        int y = position.getY();
        world[x][y] = Tileset.PLAYER;
    }

    // Constructor without passing argument.
    public Player() {
    }

    //Move to next tile if next tile is not a wall, otherwise, do nothing.
    public static void move(TETile[][] world, String move, Game game) {
        int x = position.getX();
        int y = position.getY();
        if (move.equals("w")) {
            x = position.getX();
            y = position.getY() + 1;
        }
        if (move.equals("d")) {
            x = position.getX() + 1;
            y = position.getY();
        }
        if (move.equals("s")) {
            x = position.getX();
            y = position.getY() - 1;
        }
        if (move.equals("a")) {
            x = position.getX() - 1;
            y = position.getY();
        }
        if (world[x][y].equals(Tileset.LOCKED_DOOR)) {
            game.gameOver = true;
            return;
        }
        if (!world[x][y].equals(Tileset.WALL)) {
            world[position.getX()][position.getY()] = Tileset.FLOOR;
            world[x][y] = Tileset.PLAYER;
            position.update(x, y);
        }
    }
}
