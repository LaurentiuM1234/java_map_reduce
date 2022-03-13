

public interface Reducer<K, E, T, A>{
    /**
     * Method used to reduce the map output.
     *
     * @param key is the value used to find the result of the reduce operation
     *            in the resulting collector.
     * @param mapOutput is the information resulting from the map operations
     *                  to be reduced
     * @param reduceOutput is a container which collects the result of the operation.
     */
    void reduce(K key, Iterable<E> mapOutput, Collector<T, A> reduceOutput);
}
