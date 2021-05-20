import java.util.*;

public class KdTree {

  private TreeNode root;
  private Point nearestPoint;
  private double shortestDistance;


  public class TreeNode implements Comparable<TreeNode> {

    TreeNode left;
    TreeNode right;
    Point point;
    int depth;

    public TreeNode(Point point) {
      this.point = point;
    }

    public TreeNode(Point point, int depth) {
      this.point = point;
      this.depth = depth;
    }

    @Override
    public int compareTo(TreeNode o) {
      if (depth % 2 == 0) {
        double x1 = point.getX();
        double x2 = o.point.getX();
        return compare(x1, x2);
      } else {
        double y1 = point.getY();
        double y2 = o.point.getY();
        return compare(y1, y2);
      }
    }

    private int compare(double a, double b) {
      if (a > b) {
        return 1;
      } else if (a < b) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  public void insert(Point point) {
    if (root == null) {
      root = new TreeNode(point, 0);
    } else {
      insert(root, point, 0);
    }
  }

  private void insert(TreeNode node, Point point, int depth) {
    //don't do anything if we insert a duplicate point.
    if (node.point.equals(point)) {
      return;
    } else {
      TreeNode insertNode = new TreeNode(point);
      //if inserted node is bigger than the current node, go to the left.
      if (node.compareTo(insertNode) > 0) {
        if (node.left == null) {
          node.left = new TreeNode(point, depth + 1);
        } else {
          insert(node.left, point, depth + 1);
        }
      }
      //otherwise go to the right.
      else {
        if (node.right == null) {
          node.right = new TreeNode(point, depth + 1);
        } else {
          insert(node.right, point, depth + 1);
        }
      }
    }
  }

  // return null if the tree is empty, if not, return nearest point has been inserted.
  public Point nearest(double x, double y) {
    if (root == null) {
      return null;
    }
    nearestPoint = root.point;
    Point point = new Point(x, y);
    shortestDistance = Point.distance(root.point, point);
    TreeNode target = new TreeNode(point);
    findNearestPoint(root, target);
    return nearestPoint;
  }

  private void findNearestPoint(TreeNode node, TreeNode target) {
    if (node == null) {
      return;
    } else {
      double currentDistance = Point.distance(node.point, target.point);
      if (currentDistance < shortestDistance) {
        shortestDistance = currentDistance;
        nearestPoint = node.point;
      }
      //assume node use x for comparison
      double x = node.point.getX();
      double y = target.point.getY();
      if (node.depth % 2 != 0) {
        x = target.point.getX();
        y = node.point.getY();
      }
      double possibleBadSideDistance = Point.distance(target.point, new Point(x, y));
      //assume the target is bigger than node
      TreeNode goodSide = node.right;
      TreeNode badSide = node.left;
      if (node.compareTo(target) > 0) {
        goodSide = node.left;
        badSide = node.right;
      }
      findNearestPoint(goodSide, target);
      if (possibleBadSideDistance < shortestDistance) {
        findNearestPoint(badSide, target);
      }
    }
  }


  // for Junit test
  public List<Point> levelOrderTraversal() {
    Queue<TreeNode> q = new LinkedList<>();
    List<Point> res = new ArrayList<>();
    q.add(root);
    while (!q.isEmpty()) {
      TreeNode curr = q.remove();
      res.add(curr.point);
      if (curr.left != null) {
        q.add(curr.left);
      }
      if (curr.right != null) {
        q.add(curr.right);
      }
    }
    return res;
  }

}
