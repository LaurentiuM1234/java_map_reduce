


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class used to hold information which results
 * from a map operation.
 */
public class MapOutput implements Collector<FileEntry, FragmentInfo> {
    ConcurrentHashMap<FileEntry, List<FragmentInfo>> map;


    public MapOutput() {
        map = new ConcurrentHashMap<>();
    }

    public ConcurrentHashMap<FileEntry, List<FragmentInfo>> getMap() {
        return map;
    }

    @Override
    public void collect(FileEntry key, FragmentInfo value) {
        List<FragmentInfo> entry = new ArrayList<>();
        entry.add(value);

        if (map.putIfAbsent(key, entry) != null) {
            // before doing computations, make sure to obtain lock on the list
            synchronized (map.get(key)) {
                List<FragmentInfo> oldEntry = map.get(key);
                oldEntry.addAll(entry);
                map.put(key, oldEntry);
            }
        }
    }


}
