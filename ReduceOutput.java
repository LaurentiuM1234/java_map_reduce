

import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to hold information which results
 * from a reduce operation.
 */
public class ReduceOutput implements Collector<FileEntry, DocumentInfo>{
    private final ConcurrentHashMap<FileEntry, DocumentInfo> map;

    public ReduceOutput() {
        this.map = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<FileEntry, DocumentInfo> getMap() {
        return map;
    }

    @Override
    public void collect(FileEntry key, DocumentInfo value) {
        map.put(key, value);
    }
}
