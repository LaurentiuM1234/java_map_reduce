

public interface Mapper<K, E, T, A> {
    /**
     * Method used to transform a text.
     *
     * @param key is the value used to find the result of the map operation
     *            in the resulting collector.
     * @param text is the information to be mapped.
     * @param output is a container which collects the result of the operation.
     */
    void map(K key, Text<A> text, Collector<E, T> output);
}
