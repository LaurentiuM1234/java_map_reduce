

/**
 * Class which provides utility methods.
 */
public class Util {
    /**
     * Compute the element found at given index in the
     * Fibonacci sequence.
     *
     * @param index is the index of element in Fibonacci sequence.
     *
     * @return number from Fibonacci sequence.
     */
    public static int fib(int index) {
        if (index == 0)
            return 0;
        if (index == 1)
            return 1;

        return fib(index - 1) + fib(index - 2);
    }
}
