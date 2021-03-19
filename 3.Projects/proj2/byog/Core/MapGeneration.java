package byog.Core;

import byog.TileEngine.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Stack;

/** Generate random rooms.
 * @author
 */

public class MapGeneration {

    //testing purpose
//    public static void main(String[] args) {
//
//        MapGeneration mg = new MapGeneration(854711);
//        TERenderer ter = new TERenderer();
//        ter.initialize(85, 48);
//        ter.renderFrame(mg.generate(85, 48).getWorld());
//
//    }

    // a switch indicate if it should generate a room or a hallway.
    // when s = 0, generate a vertical hallway
    // when s = 1, generate a horizontal hallway
    // when s = 2, generate a room
    private int s = 0;

    //max random length of a room or a hallway, and it has to be an odd number.
    private int maxRandomLength = 9;

    //max random width of a room or a hallway, and it has to be an   odd number.
    private int maxRandomWidth = 7;

    //Random generator
    private Random r;

    //true if there is a door, otherwise false.
    private boolean doorCre = false;

    //a list of rooms
    private List<MapBuilder> rooms = new ArrayList<>();

    // Create a constructor with seed input.
    public MapGeneration(long seed) {
        r = new Random(seed);
    }


    // Generate a new world by the given seed, and return a GameData object which store world and player data.
    public GameData generate(int X, int Y) {
        TETile[][] world = new TETile[X][Y];
        for (int x = 0; x < X; x += 1) {
            for (int y = 0; y < Y; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        int initialX = r.nextInt(X - (maxRandomWidth + 1)) + ((maxRandomWidth - 1) / 2 + 1);
        int initialY = r.nextInt(Y - 3  - (maxRandomLength + 1)) + ((maxRandomLength - 1) / 2 + 1);
        Position initialPos = new Position(initialX, initialY);
        MapBuilder current = new MapBuilder(randomLength(), randomWidth(), initialPos);
        current.BuildToWorld(world);
        Player player = new Player(world, initialPos);
        Stack<MapBuilder> stack = new Stack<>();
        stack.push(current);
        while (!stack.isEmpty()) {
            while (!current.isExpandable()) {
                s = r.nextInt(3); // the value of s indicate to build a room or hallway current
                int d = randomDirect(current.neighbors()); // expand at current room/hallway's random direction
                int l = randomLength();
                int w = randomWidth();
                if (current.isExpandableAtDirection(world, l, w, d, X, Y - 3)) {
                    current = current.expand(world, l, w, d);
                    stack.push(current);
                    if (l > 1 && w > 1) {
                        rooms.add(current);
                    }
                } else {
                    current.neighbors()[d] = true;
                }
            }
            MapBuilder temp = stack.peek();
            if (!doorCre && temp.isRoom()) {
                makeDoor(world, temp);
                doorCre = true;
            }
            stack.pop();
            if (!stack.isEmpty()) {
                current = stack.peek();
            }
        }
        return new GameData(world, player);
    }

    //Replace a wall to a door in a room
    private void makeDoor(TETile[][] world, MapBuilder room) {
        for (int i = 0; i < 4; i++) {
            room.neighbors()[i] = false;
        }
        while (!room.isExpandable()) {
            int d = randomDirect(room.neighbors());
            int x = room.getEdgePos(d).getX();
            int y = room.getEdgePos(d).getY();
            if (world[x][y].equals(Tileset.WALL)) {
                world[x][y] = Tileset.LOCKED_DOOR;
                break;
            } else {
                room.neighbors()[d] = true;
            }
        }
    }


    //Generate a random length (odd number only).
    private int randomLength() {
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
    private int randomWidth() {
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
    private int randomDirect(boolean[] directions) {
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
