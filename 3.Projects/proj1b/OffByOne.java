/**A class for off-by-1 comparators.*/
public class OffByOne implements CharacterComparator {

    /**return True if the difference of two characters is exactly by 1, otherwise return false*/
    @Override
    public boolean equalChars(char x, char y) {
        if (Math.abs(x - y) == 1) {
            return true;
        }
        return false;
    }
}
