/**A class for off-by-N comparators.*/
public class OffByN implements CharacterComparator {

    /**the difference that two characters off by.*/
    private int N;

    /** Constructor that takes an integer.*/
    public OffByN(int n) {
        this.N = n;
    }

    /**return True if the difference of two characters is exactly by N, otherwise return false.*/
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == this.N) {
            return true;
        }
        return false;
    }

}
