package byog.Core;

import byog.TileEngine.TETile;
import byog.TileEngine.Tileset;

import java.io.Serializable;

public interface MapElement {

    // getter method for length
    int getLength();

    // getter method for width
    int getWidth();

    // getter method for explored array
    boolean[] getExplored();

    // getter method for Position
    Position getPosition();

    //fill row with tiles, n is the numbers of tiles.
    default void fillRow(TETile[][] world, int n, Position p) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < n; i++) {
            world[x][y] = Tileset.WALL;
            x++;
        }
    }
    /** Call drawWall() and DrawFloor().
     * p is in the center of object.
     *   x x x x x
     *   x   p   x
     *   x x x x x
     */
    default void draw(TETile[][] world) {
        int y = getEdgePosition(2).getY() - ((getLength() - 1) / 2 + 1);
        int x = getEdgePosition(2).getX();
        drawFloor(world, new Position(x, y));
        drawWall(world, new Position(x, y));
    }

    /** Draw the wall of the object.*/
    default void drawWall(TETile[][] world, Position p) {
        int l = getLength();
        int w = getWidth();
        fillRow(world, w + 2, p);
        fillRow(world, w + 2, new Position(p.getX(), p.getY() + l + 1));
        for (int i = 1; i <= l; i++) {
            world[p.getX()][p.getY() + i] = Tileset.WALL;
            world[p.getX() + w + 1][p.getY() + i] = Tileset.WALL;
        }
    }

    /** Draw the floor of the object. */
    default void drawFloor(TETile[][] world, Position p) {
        for (int y = p.getY() + 1; y <= p.getY() + getLength() + 1; y++) {
            for (int x = p.getX() + 1; x <= p.getX() + getWidth() + 1; x++){
                world[x][y] = Tileset.FLOOR;
            }
        }
    }

    /**Return true if all directions has been explored, otherwise false */
    default boolean isExplored() {
        boolean[] explored = getExplored();
        if (explored[0] && explored[1] && explored[2] && explored[3]) {
            return true;
        } else {
            return false;
        }
    }

    /** Return a position at the edge. Either left, bottom, right, or up */
    default Position getEdgePosition(int d) {
        if (d == 0) {
            return new Position(getPosition().getX() + ((getWidth() - 1) / 2 + 1), getPosition().getY());
        } else if (d == 1) {
            return new Position(getPosition().getX(), getPosition().getY() - ((getLength() - 1) / 2 + 1));
        } else if (d == 2) {
            return new Position(getPosition().getX() - ((getWidth() - 1) / 2 + 1), getPosition().getY());
        } else {
            return new Position(getPosition().getX(), getPosition().getY() + ((getLength() - 1) / 2 + 1));
        }
    }

    /** return true if is not out of boundaries, otherwise false. */
    default boolean isNotOutOfBoundaries(int l, int w, int d, int X, int Y) {
        Position EdgeP = getEdgePosition(d);
        if (d == 0) {
            if (getExplored()[0] || EdgeP.getX() + (w + 2) >= X || EdgeP.getY() - ((l - 1) / 2 + 1) < 0 || EdgeP.getY() + ((l - 1) / 2 + 1) >= Y) {
                return false;
            }
        } else if (d == 1) {
            if (getExplored()[1] || EdgeP.getY() - (l + 2) < 0 || EdgeP.getX() - ((w - 1) / 2 + 1) < 0 || EdgeP.getX() + ((w - 1) / 2 + 1) >= X) {
                return false;
            }
        } else if (d == 2) {
            if (getExplored()[2] || EdgeP.getX() - (w + 2) < 0 || EdgeP.getY() - ((l - 1) / 2 + 1) < 0 || EdgeP.getY() + ((l - 1) / 2 + 1) >= Y) {
                return false;
            }
        } else {
            if (getExplored()[3] || EdgeP.getY() + (l + 2) >= Y || EdgeP.getX() - ((w - 1) / 2 + 1) < 0 || EdgeP.getX() + ((w - 1) / 2 + 1) >= X) {
                return false;
            }
        } return true;
    }

    /** return true if is not overlap, otherwise false. */
    default boolean isNotOverlap(TETile[][] world, int l, int w, int d) {
        Position p = getEdgePosition(d);
        if (d == 0) {
            if (overlap(world, l, w, new Position(p.getX() + 1, p.getY() - ((l - 1) / 2 + 1)))) {
                return false;
            }
        } else if (d == 1) {
            if (overlap(world, l, w, new Position(p.getX() - ((w - 1) / 2 + 1), p.getY() - (l + 2)))) {
                return false;
            }
        } else if (d == 2) {
            if (overlap(world, l, w, new Position(p.getX() - (w + 2), p.getY() - ((l - 1) / 2 + 1)))) {
                return false;
            }
        } else {
            if (overlap(world, l, w, new Position(p.getX() - ((w - 1) / 2 + 1), p.getY() + 1))) {
                return false;
            }
        }
        return true;
    }

    /** return true if overlap, otherwise false.*/
    default boolean overlap(TETile[][] world, int l, int w, Position p) {
        if (!checkRow(world, w + 2, p) || !checkRow(world, w + 2, new Position(p.getX(), p.getY() + l + 1))) {
            return true;
        }
        for (int i = 1; i <= l; i++) {
            if(!world[p.getX()][p.getY() + i].equals(Tileset.NOTHING) || !world[p.getX() + w + 1][p.getY() + i].equals(Tileset.NOTHING)) {
                return true;
            }
        }
        return false;
    }

    //check each tile in that row, return false we found a tile is not "NOTHING", otherwise true.*/
    default boolean checkRow(TETile[][] world, int n, Position p) {
        int x = p.getX();
        int y = p.getY();
        for (int i = 0; i < n; i++) {
            if (!world[x][y].equals(Tileset.NOTHING)) {
                return false;
            }
            x++;
        } return true;
    }

    /** draw a new object at current object's specific direction, and return the object that was added */
    default MapElement add(TETile[][] world, int l, int w, int d) {
        MapElement next;
        int x = getEdgePosition(d).getX();
        int y = getEdgePosition(d).getY();
        if (d == 0) {
            x = x + ((w - 1) / 2 + 2);
        } else if (d == 1) {
            y = y - ((l - 1) / 2 + 2);
        } else if (d == 2) {
            x = x - ((w - 1) / 2 + 2);
        } else {
            y = y + ((l - 1) / 2 + 2);
        }
        next = getShape(l, w, new Position(x, y));
        next.draw(world);
        connectWithFloor(world, next, d);
        getExplored()[d] = true;
        next.getExplored()[adjDirection(d)] = true;
        return next;
    }

    /** Return either HorizontalHallway, VerticalHallway or Room base on l and w.*/
    default MapElement getShape(int l, int w, Position p) {
        if (l == 1) {
            return new HorizontalHallway(w, p);
        } else if (w == 1) {
            return new VerticalHallway(l, p);
        } else {
            return new Room(l, w, p);
        }
    }

    /** Connect two objects with floors at specific direction, d is the direction this.object */
    default void connectWithFloor(TETile[][] world, MapElement e, int d) {
        int x = getEdgePosition(d).getX();
        int y = getEdgePosition(d).getY();
        world[x][y] = Tileset.FLOOR;
        x = e.getEdgePosition(adjDirection(d)).getX();
        y = e.getEdgePosition(adjDirection(d)).getY();
        world[x][y] = Tileset.FLOOR;
    }
    /**Return the adjacent direction.*/
    default int adjDirection(int d) {
        if (d == 0) {
            return 2;
        } else if(d == 1) {
            return 3;
        } else if (d == 2) {
            return 0;
        } else {
            return 1;
        }
    }
}
