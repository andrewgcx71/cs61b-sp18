package byog.Core;

import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;
import java.util.*;

import java.util.Random;

/** Generate random rooms.
 * @author
 */

public class MapGeneration {

    public static void main(String[] args) {

        MapGeneration.generate(85, 45);

    }

    // a switch indicate if it should generate a room or a hallway.
    // when s = 0, generate a vertical hallway
    // when s = 1, generate a horizontal hallway
    // when s = 2, generate a room
    private static int s = 0;

    //max random length of a room or a hallway, and it has to be odd number.
    private static int maxRandomLength = 11;

    //max random width of a room or a hallway, and it has to be odd number.
    private static int maxRandomWidth = 11;

    private static Random r = new Random();

    // Create a constructor with seed input.
    public MapGeneration(long seed) {
        r = new Random(seed);
    }


    public static TETile[][] generate(int X, int Y) {
        // initialize the tile rendering engine with a window of size X x Y
        TERenderer ter = new TERenderer();
        ter.initialize(X, Y);
        // initialize tiles
        TETile[][] world = new TETile[X][Y];
        for (int x = 0; x < X; x += 1) {
            for (int y = 0; y < Y; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        int a = r.nextInt( X - (maxRandomWidth + 1)) + ((maxRandomWidth - 1) / 2 + 1);
        int b = r.nextInt( Y - (maxRandomLength + 1)) + ((maxRandomLength - 1) / 2 + 1);
        Position initialP = new Position(a, b);
        MapElement next = new Room(randomLength(),maxRandomWidth, initialP);
        next.draw(world);
        Stack<MapElement> stack = new Stack<>();
        stack.push(next);
        while (!stack.isEmpty()) {
            while (!next.isExplored()) {
                s = r.nextInt(3);
                int d = randomDirect(next.getExplored());
                int l = randomLength();
                int w = randomWidth();
                if (next.isNotOutOfBoundaries(l, w, d, X, Y) && next.isNotOverlap(world, l, w, d)) {
                    next = next.add(world, l, w, d);
                    stack.push(next);
                } else {
                    next.getExplored()[d] = true;
                }
            }
            stack.pop();
            if (!stack.isEmpty()) {
                next = stack.peek();
            }
        }
        ter.renderFrame(world);  // draws the world to the screen
        return world;
    }


    //Generate a random odd length.
    private static int randomLength() {
        if (s == 0) {
            return 1;
        } else {
            int n = r.nextInt(maxRandomLength);
            while (n % 2 != 0) {
                n = r.nextInt(maxRandomLength);
            }
            return n + 1;
        }
    }
    //Generate a random odd width.
    private static int randomWidth() {
        if (s == 1) {
            return 1;
        } else {
            int n = r.nextInt(maxRandomWidth);
            while (n % 2 != 0) {
                n = r.nextInt(maxRandomWidth);
            }
            return n + 1;
        }
    }

    //Generate a random direction that hasn't been explored yet.
    private static int randomDirect(boolean[] directions) {
        List<Integer> lst = new ArrayList<>();
        for (int i = 0; i < directions.length; i++) {
            if (directions[i] == false) {
                lst.add(i);
            }
        }
        int n = r.nextInt(lst.size());
        return lst.get(n);
    }



}
