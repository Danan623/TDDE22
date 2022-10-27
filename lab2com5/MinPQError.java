package lab2com5;

/**
 * Simple exception class to handle errors in the priority queue.
 */
public class MinPQError extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public MinPQError(String err) {
	super(err);
    }
}
