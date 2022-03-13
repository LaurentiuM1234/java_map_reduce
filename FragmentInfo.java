

import java.util.*;
import java.util.stream.Collectors;

/**
 * Class which holds information obtained by
 * processing a Fragment object
 */
public class FragmentInfo {
    HashMap<Integer, Integer> map;
    List<String> list;

    public FragmentInfo() {
        map = new HashMap<>();
        list = new ArrayList<>();
    }

    public HashMap<Integer, Integer> getMap() {
        return map;
    }

    public List<String> getList() {
        return list;
    }

    /**
     * Update map by adding new entry. If
     * entry already exists, increment its value by 1
     *
     * @param length is key to be added to the map.
     */
    void updateMap(Integer length) {
        if (!map.containsKey(length)) {
            map.put(length, 1);
        } else {
            // if given length already in map, add new occurrence
            map.merge(length, 1, Integer::sum);
        }
    }

    /**
     * Update map in batch mode using given list of keys.
     *
     * @param lengthList is the list of keys to be inserted
     *                   in the map.
     */
    void batchUpdateMap(List<Integer> lengthList) {
        for (Integer i : lengthList) {
            this.updateMap(i);
        }
    }

    /**
     * Update list by adding a new entry.
     *
     * @param word is the entry to be added to list
     */
    void updateList(String word) {
        list.add(word);
    }

    /**
     * Update list in batch mode by adding all the elements
     * from given list.
     *
     * @param wordList is the list of entries to be added to
     *                 list.
     */
    void batchUpdateList(List<String> wordList) {
        for (String word : wordList) {
            this.updateList(word);
        }
    }

    /**
     * Filter out any words which don't have the maximum length.
     */
    public void filterNonMaxWords() {
        List<Integer> lengthList = list
                .stream()
                .map(String::length)
                .collect(Collectors.toList());

        int maxLength = Collections.max(lengthList);

        list = list
                .stream()
                .filter(str -> str.length() == maxLength)
                .collect(Collectors.toList());
    }

    /**
     * Merge two FragmentInfo objects.
     *
     * @param dest is the object in which the merge result is stored.
     * @param src is the object which will be merged into dest.
     */
    static void mergeFragmentInfos(FragmentInfo dest, FragmentInfo src) {
        src.map.forEach((key, value) ->
                dest.getMap().merge(key, value, Integer::sum));

        dest.getList().addAll(src.getList());
    }
}
