

public interface Collector<K, V> {
    /**
     * Store pair of (key, value).
     *
     * @param key is used to locate the value.
     * @param value is the value to be stored.
     */
    void collect(K key, V value);
}
