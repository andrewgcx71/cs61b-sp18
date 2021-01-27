import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.util.*;

/**
 * Graph for storing all of the intersection (vertex) and road (edge) information.
 * Uses your GraphBuildingHandler to convert the XML files into a graph. Your
 * code must include the vertices, adjacent, distance, closest, lat, and lon
 * methods. You'll also need to include instance variables and methods for
 * modifying the graph (e.g. addNode and addEdge).
 *
 * @author Alan Yao, Josh Hug
 */
//From spec, You should ignore all one-way tags and pretend all roads are two-way.
public class GraphDB {
    /**
     * Your instance variables for storing the graph. You should consider
     * creating helper classes, e.g. Node, Edge, etc.
     */

    private static List<Map<String, Object>> locations = new ArrayList<>();

    public static List<Map<String, Object>> getLocations() {
        return locations;
    }

    //Trie data structure that store the location name
    private static Trie trie = new Trie();


    public Trie getTrie() {
        return trie;
    }

    //vertices
    private Set<Long> vertices = new HashSet<>();

    //neighbors
    private Map<Long, Set<Long>> edges = new HashMap<>();

    //coordinate
    private Map<Long, Coordinate> coordinates = new HashMap<>();

    //get street name for a particular edge
    private Map<List<Long>, String> wayName = new HashMap<>();

    public Map<Long, Coordinate> getCoordinates() {
        return coordinates;
    }

    public Map<List<Long>, String> getWayName() {
        return wayName;
    }

    /**
     * Example constructor shows how to create and start an XML parser.
     * You do not need to modify this constructor, but you're welcome to do so.
     *
     * @param dbPath Path to the XML file to be parsed.
     */
    public GraphDB(String dbPath) {
        try {
            File inputFile = new File(dbPath);
            FileInputStream inputStream = new FileInputStream(inputFile);
            // GZIPInputStream stream = new GZIPInputStream(inputStream);

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            GraphBuildingHandler gbh = new GraphBuildingHandler(this);
            saxParser.parse(inputStream, gbh);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        List<String> results = trie.search("d");
        for (String str: results) {
            System.out.println(str);
        }
        System.out.println(cleanString("Dana & Durant (UCB Lot)"));
        trie.insert(cleanString("Dana & Durant (UCB Lot)"));
        clean();
    }

    /**
     * In linear time, collect all the names of OSM locations that prefix-match the query string.
     * @param prefix Prefix string to be searched for. Could be any case, with our without
     *               punctuation.
     * @return A <code>List</code> of the full names of locations whose cleaned name matches the
     * cleaned <code>prefix</code>.
     */

    public static List<String> getLocationsByPrefix(String prefix) {

        return trie.search(prefix);
    }

    /**
     * Collect all locations that match a cleaned <code>locationName</code>, and return
     * information about each node that matches.
     * @param locationName A full name of a location searched for.
     * @return A list of locations whose cleaned name matches the
     * cleaned <code>locationName</code>, and each location is a map of parameters for the Json
     * response as specified: <br>
     * "lat" : Number, The latitude of the node. <br>
     * "lon" : Number, The longitude of the node. <br>
     * "name" : String, The actual name of the node. <br>
     * "id" : Number, The id of the node. <br>
     */
    public static List<Map<String, Object>> getLocations(String locationName) {
        List<Map<String, Object>> results = new ArrayList<>();
        for(Map<String, Object> location: locations) {
            if(((String) location.get("name")).equals(locationName)) {
                results.add(location);
            }
        }
        return results;
    }


    //take a two nodes, and return the street name for these two nodes.
    public String getStreetName(Long node1, Long node2) {
        return wayName.get(new ArrayList<Long>(Arrays.asList(node1, node2)));
    }

    /**
     * Add node.
     */
    public void addNode(long id, double lat, double lon) {
        vertices.add(id);
        coordinates.put(id, new Coordinate(lat, lon));
    }


    /**
     * Add edge.
     */
    public void addEdge(long id1, long id2) {
        if (!edges.containsKey(id1)) {
            edges.put(id1, new HashSet<>());
        }
        if (!edges.containsKey(id2)) {
            edges.put(id2, new HashSet<>());
        }
        edges.get(id1).add(id2);
        edges.get(id2).add(id1);
    }

    /**
     * remove node.
     */
    public void removeNode(long id) {
        vertices.remove(id);
    }


    /**
     * Helper to process strings into their "cleaned" form, ignoring punctuation and capitalization.
     *
     * @param s Input string.
     * @return Cleaned string.
     */
    static String cleanString(String s) {

        return s.replaceAll("[^a-zA-Z ]", "").toLowerCase();
    }

    /**
     * Remove nodes with no connections from the graph.
     * While this does not guarantee that any two nodes in the remaining graph are connected,
     * we can reasonably assume this since typically roads are connected.
     */
    private void clean() {
        // TODO: Your code here.
        Set<Long> tempList = new HashSet<>(vertices);
        for (Long node : tempList) {
            if (!edges.containsKey(node)) {
                removeNode(node);
            }
        }
    }

    /**
     * Returns an iterable of all vertex IDs in the graph.
     *
     * @return An iterable of id's of all vertices in the graph.
     */
    Iterable<Long> vertices() {
        //YOUR CODE HERE, this currently returns only an empty list.
        return vertices;
    }

    /**
     * Returns ids of all vertices adjacent to v.
     *
     * @param v The id of the vertex we are looking adjacent to.
     * @return An iterable of the ids of the neighbors of v.
     */
    Iterable<Long> adjacent(long v) {
        return edges.get(v);
    }

    /**
     * Returns the shortest distance from point A to point B
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The great-circle distance between the two locations from the graph.
     */
    double distance(long v, long w) {
        return distance(lon(v), lat(v), lon(w), lat(w));
    }

    //assuming it's in miles right now.
    static double distance(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double dphi = Math.toRadians(latW - latV);
        double dlambda = Math.toRadians(lonW - lonV);

        double a = Math.sin(dphi / 2.0) * Math.sin(dphi / 2.0);
        a += Math.cos(phi1) * Math.cos(phi2) * Math.sin(dlambda / 2.0) * Math.sin(dlambda / 2.0);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return 3963 * c;
    }

    /**
     * Returns the initial bearing (angle) between vertices v and w in degrees.
     * The initial bearing is the angle that, if followed in a straight line
     * along a great-circle arc from the starting point, would take you to the
     * end point.
     * Assumes the lon/lat methods are implemented properly.
     * <a href="https://www.movable-type.co.uk/scripts/latlong.html">Source</a>.
     *
     * @param v The id of the first vertex.
     * @param w The id of the second vertex.
     * @return The initial bearing between the vertices.
     */
    double bearing(long v, long w) {
        return bearing(lon(v), lat(v), lon(w), lat(w));
    }

    static double bearing(double lonV, double latV, double lonW, double latW) {
        double phi1 = Math.toRadians(latV);
        double phi2 = Math.toRadians(latW);
        double lambda1 = Math.toRadians(lonV);
        double lambda2 = Math.toRadians(lonW);

        double y = Math.sin(lambda2 - lambda1) * Math.cos(phi2);
        double x = Math.cos(phi1) * Math.sin(phi2);
        x -= Math.sin(phi1) * Math.cos(phi2) * Math.cos(lambda2 - lambda1);
        return Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Returns the vertex closest to the given longitude and latitude.
     *
     * @param lon The target longitude.
     * @param lat The target latitude.
     * @return The id of the node in the graph closest to the target.
     */
    long closest(double lon, double lat) {
        //assuming the Set<Long> vertices is not empty, so we guarantee that there
        // will be a distance will be less then Double.MAX_VALUE, the value of id
        // is not important because the if statement will satisfy at least once in the for enhanced loop.
        Double closest = Double.MAX_VALUE;
        Long id = Long.MAX_VALUE;
        for (long node : vertices) {
            Coordinate c = coordinates.get(node);
            double distance = distance(lon, lat, c.getLon(), c.getLat());
            if (closest > distance) {
                closest = distance;
                id = node;
            }
        }
        return id;
    }

    /**
     * Gets the longitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The longitude of the vertex.
     */
    double lon(long v) {
        return coordinates.get(v).getLon();
    }

    /**
     * Gets the latitude of a vertex.
     *
     * @param v The id of the vertex.
     * @return The latitude of the vertex.
     */
    double lat(long v) {
        return coordinates.get(v).getLat();
    }

}
