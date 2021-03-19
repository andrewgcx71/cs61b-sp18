package byog.lab5;
import byog.TileEngine.TERenderer;
import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {

    /**
     * Adds a hexagon based on it's start position.
     * The hexagon always starts at the longest two rows' second row's first tile, see below.
     *
     *       xx
     *      xxxx
     *    ->xxxx
     *       xx
     *
     * */

    public static void addHexagon(Position p, int sizeofHex, TETile[][] world) {
        int x = p.getX();
        int y = p.getY();
        Position p1 = new Position(x, y + 1);
        Position p2 = p;
        int n =sizeofHex + (sizeofHex - 1) * 2;
        fillRow(n, p1, world);
        fillRow(n, p2, world);
        for (int i = sizeofHex - 1; i > 0; i--) {
            n = n - 2;
            p1 = rowAbove(p1);
            fillRow(n, p1, world);
            p2 = rowBelow(p2);
            fillRow(n, p2, world);
        }
    }

    /** fill a particular row with tiles for that hexagon.
     * @param n : the number of tiles in that row
     * */

    public static void fillRow(int n, Position p, TETile[][] world) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < n; i++) {
            world[x][y] = Tileset.FLOWER;
            x++;
        }
    }

    /** return the start position of row above the row in the parameter */

    public static Position rowAbove(Position p) {
        return new Position(p.getX() + 1, p.getY() + 1);
    }

    /** return the start position of row below the row in the parameter */

    public static Position rowBelow(Position p) {
        return new Position(p.getX() + 1, p.getY() - 1);
    }




    public static void main(String[] args) {
        int width = 85;
        int height = 45;
        TERenderer ter = new TERenderer();
        ter.initialize(width, height);
        TETile[][] world = new TETile[width][height];
        Position p = new Position(3,8);
        for (int x = 0; x < width; x += 1) {
            for (int y = 0; y < height; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        HexWorld.addHexagon(p, 2, world);
        ter.renderFrame(world);

    }

//    @Test
//    public void testHexRowWidth() {
//        assertEquals(3, hexRowWidth(3, 5));
//        assertEquals(5, hexRowWidth(3, 4));
//        assertEquals(7, hexRowWidth(3, 3));
//        assertEquals(7, hexRowWidth(3, 2));
//        assertEquals(5, hexRowWidth(3, 1));
//        assertEquals(3, hexRowWidth(3, 0));
//        assertEquals(2, hexRowWidth(2, 0));
//        assertEquals(4, hexRowWidth(2, 1));
//        assertEquals(4, hexRowWidth(2, 2));
//        assertEquals(2, hexRowWidth(2, 3));
//    }
//
//    @Test
//    public void testHexRowOffset() {
//        assertEquals(0, hexRowOffset(3, 5));
//        assertEquals(-1, hexRowOffset(3, 4));
//        assertEquals(-2, hexRowOffset(3, 3));
//        assertEquals(-2, hexRowOffset(3, 2));
//        assertEquals(-1, hexRowOffset(3, 1));
//        assertEquals(0, hexRowOffset(3, 0));
//        assertEquals(0, hexRowOffset(2, 0));
//        assertEquals(-1, hexRowOffset(2, 1));
//        assertEquals(-1, hexRowOffset(2, 2));
//        assertEquals(0, hexRowOffset(2, 3));
//    }
}
