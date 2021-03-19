import java.util.ArrayList;
import java.util.List;

/** It stores max speed. */
public class Way {

    // Store nodes in the this particular way.
    private List<Long> nodes = new ArrayList<>();

    // Max speed.
    private int maxSpeed;

    //the name of way
    private String wayName = "";

    public String getWayName() {
        return wayName;
    }

    public List<Long> getNodes() {
        return nodes;
    }

    /**add a node to the way. */
    public void addNode(Long id) {
        nodes.add(id);
    }

    /** set max speed. */
    public void setMaxSpeed(int maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /** set way name. */
    public void setWayName(String wayName) {
        this.wayName = wayName;
    }

}
