import java.util.Random;

public class ArrayDequeTest {
    public static void main(String[] args) {
        Random random = new Random();
        ArrayDeque<Integer> nums = new ArrayDeque<>();
        nums.addLast(1);
        nums.addLast(2);
        nums.addLast(3);
        nums.addLast(4);
        nums.addLast(5);
        nums.addLast(6);



        nums.printDeque();

    }
 }
