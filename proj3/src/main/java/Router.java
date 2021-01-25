

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class provides a shortestPath method for finding routes between two points
 * on the map. Start by using Dijkstra's, and if your code isn't fast enough for your
 * satisfaction (or the autograder), upgrade your implementation by switching it to A*.
 * Your code will probably not be fast enough to pass the autograder unless you use A*.
 * The difference between A* and Dijkstra's is only a couple of lines of code, and boils
 * down to the priority you use to order your vertices.
 */
public class Router {

    private static Map<Long, Double> distanceToSource;
    private static Set<Long> visitedNodes;
    private static Map<Long, Double> heuristicDistanceToTarget;
    private static Map<Long, Long> childToParent;

    private static class NodeComparator implements Comparator<Long> {
        @Override
        public int compare(Long var1, Long var2) {
            double d1 = distanceToSource.get(var1) + heuristicDistanceToTarget.get(var1);
            double d2 = distanceToSource.get(var2) + heuristicDistanceToTarget.get(var2);
            if (d1 < d2) {
                return -1;
            } else if (d1 > d2) {
                return 1;
            } else {
                return 0;
            }
        }
    }


    /**
     * Return a List of longs representing the shortest path from the node
     * closest to a start location and the node closest to the destination
     * location.
     *
     * @param g       The graph to use.
     * @param stlon   The longitude of the start location.
     * @param stlat   The latitude of the start location.
     * @param destlon The longitude of the destination location.
     * @param destlat The latitude of the destination location.
     * @return A list of node id's in the order visited on the shortest path.
     * Original approach: similar to below except add duplicate node with difference priority instead of
     * removing the node after change priority and then re-insert the node with updated priority.
     * https://github.com/btke/CS61B-Spring-2018/blob/master/Projects/BearMaps/src/main/java/Router.java
     */
    public static List<Long> shortestPath(GraphDB g, double stlon, double stlat,
                                          double destlon, double destlat) {

        Long source = getSource(g, stlon, stlat);
        Long target = getTarget(g, destlon, destlat);
        childToParent = new HashMap<>();
        distanceToSource = new HashMap<>();
        visitedNodes = new HashSet<>();
        heuristicDistanceToTarget = new HashMap<>();
        for (Long vertex : g.vertices()) {
            if (vertex.equals(source)) {
                distanceToSource.put(vertex, 0.0);
            } else {
                distanceToSource.put(vertex, Double.MAX_VALUE);
            }
            heuristicDistanceToTarget.put(vertex, g.distance(vertex, target));
        }
        Queue<Long> pq = new PriorityQueue<>(new NodeComparator());
        pq.add(source);
        childToParent.put(source, source);
        while (!pq.isEmpty()) {
            Long current = pq.remove();
            if(current.equals(target)) {
                break;
            }
            if (!visitedNodes.contains(current)) {
                visitedNodes.add(current);
                for (Long adj : g.adjacent(current)) {
                    if (visitedNodes.contains(adj)) {
                        continue;
                    }
                    double currentDistance = distanceToSource.get(current) + g.distance(current, adj);
                    double bestDistance = distanceToSource.get(adj);
                    if (currentDistance < bestDistance) {
                        childToParent.put(adj, current);
                        distanceToSource.put(adj, currentDistance);
                        pq.add(adj);
                    }
                }
            }
        }
        return path(source, target);
    }



    //get source node
    private static Long getSource(GraphDB g, double lon, double lat) {
        return g.closest(lon, lat);
    }

    //get target node
    private static Long getTarget(GraphDB g, double lon, double lat) {
        return g.closest(lon, lat);
    }


    //given source and target, return a shortest path from source to target.
    private static List<Long> path(Long source, Long target) {
        if (target.equals(source)) {
            return new ArrayList<Long>(Arrays.asList(source));
        } else {
            List<Long> lst = path(source, childToParent.get(target));
            lst.add(target);
            return lst;
        }
    }


    /**
     * Create the list of directions corresponding to a route on the graph.
     *
     * @param g     The graph to use.
     * @param nodes The route to translate into directions. Each element
     *              corresponds to a node from the graph in the route.
     * @return A list of NavigatiionDirection objects corresponding to the input
     * route.
     */
    public static List<NavigationDirection> routeDirections(GraphDB g, List<Long> nodes) {
        List<NavigationDirection> results = new ArrayList<>();
        if (nodes.size() <= 1) {
            return results;
        }
        String previousWay = g.getStreetName(nodes.get(0), nodes.get(1));
        double distance = g.distance(nodes.get(0), nodes.get(1));
        int direction = 0;
        if (nodes.size() == 2) {
            NavigationDirection currentND = new NavigationDirection();
            currentND.distance = distance;
            currentND.direction = direction;
            currentND.way = previousWay;
            results.add(currentND);
        }
        for (int i = 2; i < nodes.size(); i++) {
            String currentWay = g.getStreetName(nodes.get(i - 1), nodes.get(i));
            //if we encounter a turn
            if (!currentWay.equals(previousWay)) {
                NavigationDirection currentND = new NavigationDirection();
                currentND.distance = distance;
                currentND.way = previousWay;
                currentND.direction = direction;
                results.add(currentND);
                double preBearing = g.bearing(nodes.get(i - 2), nodes.get(i - 1));
                double curBearing = g.bearing(nodes.get(i - 1), nodes.get(i));
                System.out.println(preBearing);
                direction = currentND.getDirection(preBearing, curBearing);
                distance = 0;
            }
            previousWay = currentWay;
            distance += g.distance(nodes.get(i - 1), nodes.get(i));
            if (i == nodes.size() - 1) {
                NavigationDirection currentND = new NavigationDirection();
                currentND.direction = direction;
                currentND.distance = distance;
                currentND.way = previousWay;
                results.add(currentND);
            }
        }
        return results;
    }


    /**
     * Class to represent a navigation direction, which consists of 3 attributes:
     * a direction to go, a way, and the distance to travel for.
     */
    public static class NavigationDirection {

        /**
         * Integer constants representing directions.
         */
        public static final int START = 0;
        public static final int STRAIGHT = 1;
        public static final int SLIGHT_LEFT = 2;
        public static final int SLIGHT_RIGHT = 3;
        public static final int RIGHT = 4;
        public static final int LEFT = 5;
        public static final int SHARP_LEFT = 6;
        public static final int SHARP_RIGHT = 7;

        /**
         * Number of directions supported.
         */
        public static final int NUM_DIRECTIONS = 8;

        /**
         * A mapping of integer values to directions.
         */
        public static final String[] DIRECTIONS = new String[NUM_DIRECTIONS];

        /**
         * Default name for an unknown way.
         */
        public static final String UNKNOWN_ROAD = "unknown road";

        /** Static initializer. */
        static {
            DIRECTIONS[START] = "Start";
            DIRECTIONS[STRAIGHT] = "Go straight";
            DIRECTIONS[SLIGHT_LEFT] = "Slight left";
            DIRECTIONS[SLIGHT_RIGHT] = "Slight right";
            DIRECTIONS[LEFT] = "Turn left";
            DIRECTIONS[RIGHT] = "Turn right";
            DIRECTIONS[SHARP_LEFT] = "Sharp left";
            DIRECTIONS[SHARP_RIGHT] = "Sharp right";
        }

        /**
         * The direction a given NavigationDirection represents.
         */
        int direction;
        /**
         * The name of the way I represent.
         */
        String way;
        /**
         * The distance along this way I represent.
         */
        double distance;

        /**
         * Create a default, anonymous NavigationDirection.
         */
        public NavigationDirection() {
            this.direction = STRAIGHT;
            this.way = UNKNOWN_ROAD;
            this.distance = 0.0;
        }

        /**
         * Checks that a value is between the given ranges.
         */
        private static boolean numInRange(double value, double from, double to) {
            return value >= from && value <= to;
        }

        /**
         * Calculates what direction we are going based on the two bearings, which
         * are the angles from true north. We compare the angles to see whether
         * we are making a left turn or right turn. Then we can just use the absolute value of the
         * difference to give us the degree of turn (straight, sharp, left, or right).
         *
         * @param prevBearing A double in [0, 360.0]
         * @param currBearing A double in [0, 360.0]
         * @return the Navigation Direction type
         */
        private static int getDirection(double prevBearing, double currBearing) {
            double absDiff = Math.abs(currBearing - prevBearing);
            if (numInRange(absDiff, 0.0, 15.0)) {
                return NavigationDirection.STRAIGHT;

            }
            if ((currBearing > prevBearing && absDiff < 180.0)
                    || (currBearing < prevBearing && absDiff > 180.0)) {
                // we're going right
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    // bearmaps.proj2d.example of high abs diff is prev = 355 and curr = 2
                    return NavigationDirection.SLIGHT_RIGHT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.RIGHT;
                } else {
                    return NavigationDirection.SHARP_RIGHT;
                }
            } else {
                // we're going left
                if (numInRange(absDiff, 15.0, 30.0) || absDiff > 330.0) {
                    return NavigationDirection.SLIGHT_LEFT;
                } else if (numInRange(absDiff, 30.0, 100.0) || absDiff > 260.0) {
                    return NavigationDirection.LEFT;
                } else {
                    return NavigationDirection.SHARP_LEFT;
                }
            }
        }

        public String toString() {
            return String.format("%s on %s and continue for %.3f miles.",
                    DIRECTIONS[direction], way, distance);
        }

        /**
         * Takes the string representation of a navigation direction and converts it into
         * a Navigation Direction object.
         *
         * @param dirAsString The string representation of the NavigationDirection.
         * @return A NavigationDirection object representing the input string.
         */
        public static NavigationDirection fromString(String dirAsString) {
            String regex = "([a-zA-Z\\s]+) on ([\\w\\s]*) and continue for ([0-9\\.]+) miles\\.";
            Pattern p = Pattern.compile(regex);
            Matcher m = p.matcher(dirAsString);
            NavigationDirection nd = new NavigationDirection();
            if (m.matches()) {
                String direction = m.group(1);
                if (direction.equals("Start")) {
                    nd.direction = NavigationDirection.START;
                } else if (direction.equals("Go straight")) {
                    nd.direction = NavigationDirection.STRAIGHT;
                } else if (direction.equals("Slight left")) {
                    nd.direction = NavigationDirection.SLIGHT_LEFT;
                } else if (direction.equals("Slight right")) {
                    nd.direction = NavigationDirection.SLIGHT_RIGHT;
                } else if (direction.equals("Turn right")) {
                    nd.direction = NavigationDirection.RIGHT;
                } else if (direction.equals("Turn left")) {
                    nd.direction = NavigationDirection.LEFT;
                } else if (direction.equals("Sharp left")) {
                    nd.direction = NavigationDirection.SHARP_LEFT;
                } else if (direction.equals("Sharp right")) {
                    nd.direction = NavigationDirection.SHARP_RIGHT;
                } else {
                    return null;
                }

                nd.way = m.group(2);
                try {
                    nd.distance = Double.parseDouble(m.group(3));
                } catch (NumberFormatException e) {
                    return null;
                }
                return nd;
            } else {
                // not a valid nd
                return null;
            }
        }

        @Override
        public boolean equals(Object o) {
            if (o instanceof NavigationDirection) {
                return direction == ((NavigationDirection) o).direction
                        && way.equals(((NavigationDirection) o).way)
                        && distance == ((NavigationDirection) o).distance;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(direction, way, distance);
        }
    }
}
