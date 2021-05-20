import org.junit.Test;

import java.util.*;

public class TestKdTree {

    @Test
    public void TestInsert() {

        int size = 4;

        Point a = new Point(2, 3);
        Point b = new Point(4, 2);
        Point c = new Point(4, 5);
        Point d = new Point(3, 3);
        Point e = new Point(1, 5);
        Point f = new Point(4, 4);

        KdTree kdTree = new KdTree();
        kdTree.insert(a);
        kdTree.insert(b);
        kdTree.insert(c);
        kdTree.insert(d);
        kdTree.insert(e);
        kdTree.insert(f);


        List<Point> actual = kdTree.levelOrderTraversal();
        List<Point> expected = Arrays.asList(a, e, b, c, d, f);

        for (int i = 0; i < size; i++) {
            if (!actual.get(i).equals(expected.get(i))) {
                System.out.println("It doesn't match.");
                System.out.println("-----------------------------------------------");
                System.out.println("Actual                            Expected");
                System.out.println("-----------------------------------------------");
                for (int j = 0; j < size; j++) {
                    double x1 = actual.get(j).getX();
                    double y1 = actual.get(j).getY();
                    double x2 = expected.get(j).getX();
                    double y2 = expected.get(j).getY();
                    System.out.println("x: " + x1 + ", " + "y: " + y1 + "         |           " + "x: " + x2 + ", " + "y: " + y2);
                }
                return;
            }
        }
        System.out.println("test passed");
    }

    @Test
    public void TestNearest() {
        Point a = new Point(2, 3);
        Point b = new Point(4, 2);
        Point c = new Point(4, 5);
        Point d = new Point(3, 3);
        Point e = new Point(1, 5);
        Point f = new Point(4, 4);

        KdTree kdTree = new KdTree();
        kdTree.insert(a);
        kdTree.insert(b);
        kdTree.insert(c);
        kdTree.insert(d);
        kdTree.insert(e);
        kdTree.insert(f);
        Point actual = kdTree.nearest(5, 3.1);
        Point expected = f;
        if (actual.equals(expected)) {
            System.out.println("Test Passed");
        } else {
            System.out.println("Actual and Expected are not the same");
            System.out.println("-----------------------------------------------");
            System.out.println("Actual                            Expected");
            System.out.println("x: " + actual.getX() + ", " + "y: " + actual.getY() + "         |           " + "x: " + expected.getX() + ", " + "y: " + expected.getY());
        }
    }
}
