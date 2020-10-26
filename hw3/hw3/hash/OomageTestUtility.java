package hw3.hash;


import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        /* TODO:
         * Write a utility function that returns true if the given oomages
         * have hashCodes that would distribute them fairly evenly across
         * M buckets. To do this, convert each oomage's hashcode in the
         * same way as in the visualizer, i.e. (& 0x7FFFFFFF) % M.
         * and ensure that no bucket has fewer than N / 50
         * Oomages and no bucket has more than N / 2.5 Oomages.
         */
        int size = oomages.size();
        double min = size / 50;
        double max = size / 2.5;
        int[] buckets = new int[M];
        for (Oomage o: oomages) {
            int hc = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[hc]++;
        }
        for (int i = 0; i < M; i++) { // can't put if statement in previous for-loop because we want to check the number of Oomage objects in each bucket is within (N / 50, N / 2.5)
            if (buckets[i] <= min || buckets[i] >= max) {
                return false;
            }
        }
        return true;
    }
}
