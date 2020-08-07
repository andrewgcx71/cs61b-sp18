package synthesizer;

//Make sure this class is public
/**Class Implement karplus strong algorithm.
 *@author
 * */
public class GuitarString {
    /** Constants. Do not change. In case you're curious, the keyword final means
     * the values cannot be changed at runtime. We'll discuss this and other topics
     * in lecture on Friday. */
    private static final int SR = 44100;      // Sampling Rate
    private static final double DECAY = .996; // energy decay factor

    /* Buffer for storing sound data. */
    private BoundedQueue<Double> buffer;

    /* Create a guitar string of the given frequency.
    * use Math.round, if 3.6, then it will be 4,
    * but if just do (int) 3.6, it will just equal to 3.*/
    public GuitarString(double frequency) {
        buffer = new ArrayRingBuffer<Double>((int) Math.round(SR / frequency));
        int j = buffer.capacity();
        for (int i = 0; i < j; i++) {
            buffer.enqueue(0.0);
        }
    }


    /* Pluck the guitar string by replacing the buffer with white noise. */
    public void pluck() {
        int counter = buffer.fillCount();
        for (int i = 0; i < counter; i++) {
            buffer.dequeue();
            buffer.enqueue(Math.random() - 0.5);
        }
    }

    /* Advance the simulation one time step by performing one iteration of
     * the Karplus-Strong algorithm. 
     */
    public void tic() {
        double x = buffer.dequeue();
        double y = sample();
        buffer.enqueue((x + y) / 2 * 0.996);
    }

    /* Return the double at the front of the buffer. */
    public double sample() {
        return buffer.peek();
    }
}
