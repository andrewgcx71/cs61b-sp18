/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 *
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     *
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        // TODO: Implement LSD Sort
        int digits = 0;
        for (int i = 0; i < asciis.length; i++) {
          digits = Math.max(digits, asciis[i].length());
        }
        String[] results = asciis;
        for (int i = 1; i <= digits; i++) {
          results = sortHelperLSD(results, i);
        }
        return results;
    }

    /**
     * LSD helper method that performs a non-destructive counting sort the array of
     * Strings based off characters at a specific index.
     * @param asciis Input array of Strings
     * @param index The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index) {
        // Optional LSD helper method for required LSD radix sort
        String[] sorted = new String[asciis.length];
        // this is a extended ASCII
        int[] counts = new int[256];
        for (int i = 0; i < asciis.length; i++) {
          int size = asciis[i].length();
          if (size >= index) {
            char c = asciis[i].charAt(size - index);
            counts[c]++;
          } else {
            counts[0]++;
          }
        }
        int[] starts = new int[256];
        int pos = 0;
        for (int i = 0; i < counts.length; i++) {
          starts[i] = pos;
          pos += counts[i];
        }
        for (int i = 0; i < asciis.length; i++) {
          int size = asciis[i].length();
          if (size >= index) {
            char c = asciis[i].charAt(size - index);
            sorted[starts[c]] = asciis[i];
            starts[c]++;
          } else {
            sorted[starts[0]] = asciis[i];
            starts[0]++;
          }
        }
        return sorted;
    }

    public static void main(String[] args) {
        RadixSort rs = new RadixSort();
        String[] strings = {"3", "5" , "2", "1", "5"};
        String[] results = rs.sort(strings);
        for (String str: results) {
            System.out.println(str);
        }
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start int for where to start sorting in this method (includes String at start)
     * @param end int for where to end sorting in this method (does not include String at end)
     * @param index the index of the character the method is currently sorting on
     *
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
