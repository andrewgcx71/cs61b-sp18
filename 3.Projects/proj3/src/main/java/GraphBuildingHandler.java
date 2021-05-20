import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import java.util.*;

/**
 *  Parses OSM XML files using an XML SAX parser. Used to construct the graph of roads for
 *  pathfinding, under some constraints.
 *  See OSM documentation on
 *  <a href="http://wiki.openstreetmap.org/wiki/Key:highway">the highway tag</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Way">the way XML element</a>,
 *  <a href="http://wiki.openstreetmap.org/wiki/Node">the node XML element</a>,
 *  and the java
 *  <a href="https://docs.oracle.com/javase/tutorial/jaxp/sax/parsing.html">SAX parser tutorial</a>.
 *
 *  You may find the CSCourseGraphDB and CSCourseGraphDBHandler examples useful.
 *
 *  The idea here is that some external library is going to walk through the XML
 *  file, and your override method tells Java what to do every time it gets to the next
 *  element in the file. This is a very common but strange-when-you-first-see it pattern.
 *  It is similar to the Visitor pattern we discussed for graphs.
 *
 *  @author Alan Yao, Maurice Lee
 */
public class GraphBuildingHandler extends DefaultHandler {

    //only the HighWay-type way has following feature consider as valid way in our graph
    private static final Set<String> ALLOWED_HIGHWAY_TYPES = new HashSet<>(Arrays.asList
            ("motorway", "trunk", "primary", "secondary", "tertiary", "unclassified",
                    "residential", "living_street", "motorway_link", "trunk_link", "primary_link",
                    "secondary_link", "tertiary_link"));
    private String activeState = "";
    private final GraphDB g;
    private Way way;
    // to flag whether this is a highway or not.
    private boolean isHighway = false;
    //previous ID/node
    private Long currentNode;

    /**
     * Create a new GraphBuildingHandler.
     * @param g The graph to populate with the XML data.
     */
    public GraphBuildingHandler(GraphDB g) {
        this.g = g;
    }

    /**
     * Called at the beginning of an element. Typically, you will want to handle each element in
     * here, and you may want to track the parent element.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available. This tells us which element we're looking at.
     * @param attributes The attributes attached to the element. If there are no attributes, it
     *                   shall be an empty Attributes object.
     * @throws SAXException Any SAX exception, possibly wrapping another exception.
     * @see Attributes
     */
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {
        /* Some example code on how you might begin to parse XML files. */

        if (qName.equals("node")) {
            /* We encountered a new <node...> tag. */
            activeState = "node";
//            System.out.println("Node id: " + attributes.getValue("id"));
//            System.out.println("Node lon: " + attributes.getValue("lon"));
//            System.out.println("Node lat: " + attributes.getValue("lat"));

            /* TODO Use the above information to save a "node" to a GraphDB object. */
            Long node = Long.parseLong(attributes.getValue("id"));
            Double lat = Double.parseDouble(attributes.getValue("lat"));
            Double lon = Double.parseDouble(attributes.getValue("lon"));
            g.addNode(node, lat, lon);
            currentNode = node;

        } else if (qName.equals("way")) {
            /* We encountered a new <way...> tag. */
            activeState = "way";
            way = new Way();
            //System.out.println("Beginning a way...");
        } else if (activeState.equals("way") && qName.equals("nd")) {
            /* While looking at a way, we found a <nd...> tag. */
            //System.out.println("Id of a node in this way: " + attributes.getValue("ref"));

            /* TODO Use the above id to make "possible" connections between the nodes in this way */
            /* Hint1: It would be useful to remember what was the last node in this way. */
            /* Hint2: Not all ways are valid. So, directly connecting the nodes here would be
            cumbersome since you might have to remove the connections if you later see a tag that
            makes this way invalid. Instead, think of keeping a list of possible connections and
            remember whether this way is valid or not. */
            Long node = Long.parseLong(attributes.getValue("ref"));
            way.addNode(node);

        } else if (activeState.equals("way") && qName.equals("tag")) {
            /* While looking at a way, we found a <tag...> tag. */
             String k = attributes.getValue("k");
            String v = attributes.getValue("v");
            if (k.equals("maxspeed")) {
                //System.out.println("Max Speed: " + v);
                /* TODO set the max speed of the "current way" here. */
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < v.length(); i++) {
                    char c = v.charAt(i);
                    if(c >= 48 && c <=57) {
                        sb.append(c);
                    }
                }
                way.setMaxSpeed(Integer.parseInt(sb.toString()));
            } if (k.equals("highway") && ALLOWED_HIGHWAY_TYPES.contains(v)) {
                //System.out.println("Highway type: " + v);
                /* TODO Figure out whether this way and its connections are valid. */
                /* Hint: Setting a "flag" is good enough! */
                isHighway = true;
            } if (k.equals("name")) {
                way.setWayName(v);
            }
            //System.out.println("Tag with k=" + k + ", v=" + v + ".");
        } else if (activeState.equals("node") && qName.equals("tag") && attributes.getValue("k")
                .equals("name")) {
            /* While looking at a node, we found a <tag...> with k="name". */
            /* TODO Create a location. */
            String cleanName = g.cleanString(attributes.getValue("v"));
            String actualName = attributes.getValue("v");
            g.getTrie().insert(cleanName, actualName);
            Map<String, Object> location = new HashMap<>();
            location.put("id", currentNode);
            location.put("name", actualName);
            location.put("lat", g.getCoordinates().get(currentNode).getLat());
            location.put("lon", g.getCoordinates().get(currentNode).getLon());
            boolean contains = false;
            if (g.getLocations().containsKey(cleanName)) {
                for (Map<String, Object> loc: g.getLocations().get(cleanName)) {
                    if (loc.get("id").equals(currentNode)) {
                        contains = true;
                    }
                 }
                if (contains == false) {
                    g.getLocations().get(cleanName).add(location);
                }
            } else {
                g.getLocations().put(cleanName, new ArrayList<>(Arrays.asList(location)));
            }
            /* Hint: Since we found this <tag...> INSIDE a node, we should remember which
            node  this tag belongs to. Remember XML is parsed top-to-bottom, so it's the
            last node that you looked at (check the first if-case). */
            //System.out.println("Node's name: " + attributes.getValue("v"));
        }
    }

//         * "lat" : Number, The latitude of the node. <br>
//     * "lon" : Number, The longitude of the node. <br>
//     * "name" : String, The actual name of the node. <br>
//     * "id" : Number, The id of the node. <br>

    /**
     * Receive notification of the end of an element. You may want to take specific terminating
     * actions here, like finalizing vertices or edges found.
     * @param uri The Namespace URI, or the empty string if the element has no Namespace URI or
     *            if Namespace processing is not being performed.
     * @param localName The local name (without prefix), or the empty string if Namespace
     *                  processing is not being performed.
     * @param qName The qualified name (with prefix), or the empty string if qualified names are
     *              not available.
     * @throws SAXException  Any SAX exception, possibly wrapping another exception.
     */
    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equals("way") && isHighway) {
            /* We are done looking at a way. (We finished looking at the nodes, speeds, etc...)*/
            //Once confirm it's indeed a highway, connect all the edges together because we weren't sure early.
            //System.out.println("Finishing a way...");
            List<Long> list = way.getNodes();
            int pre = 0;
            for(int i = 1; i < list.size(); i++) {
                long current = list.get(pre);
                long previous = list.get(i);
                g.addEdge(previous, current);
                pre = i;
                g.getWayName().put(new ArrayList<Long>(Arrays.asList(current, previous)), way.getWayName());
                g.getWayName().put(new ArrayList<Long>(Arrays.asList(previous, current)), way.getWayName());
            }
            isHighway = false;
        }
    }

}
