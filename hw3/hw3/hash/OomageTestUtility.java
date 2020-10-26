package hw3.hash;


import java.util.List;

public class OomageTestUtility {
    public static boolean haveNiceHashCodeSpread(List<Oomage> oomages, int M) {
        int size = oomages.size();
        double min = size / 50;
        double max = size / 2.5;
        int[] buckets = new int[M];
        for (Oomage o: oomages) {
            int hc = (o.hashCode() & 0x7FFFFFFF) % M;
            buckets[hc]++;
        }
        // can't put if statement in previous for-loop
        // because we want to check the number of Oomage objects
        // in each bucket is within (N / 50, N / 2.5)
        for (int i = 0; i < M; i++) {
            if (buckets[i] <= min || buckets[i] >= max) {
                return false;
            }
        }
        return true;
    }
}
