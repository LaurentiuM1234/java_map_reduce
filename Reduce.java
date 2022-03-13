


public class Reduce implements Reducer<FileEntry,
        FragmentInfo, FileEntry, DocumentInfo>, Runnable{
    private final FileEntry key;
    private final Iterable<FragmentInfo> mapOutput;
    private final ReduceOutput collector;

    public Reduce(FileEntry key, Iterable<FragmentInfo> mapOutput,
                  ReduceOutput collector) {
        this.key = key;
        this.mapOutput = mapOutput;
        this.collector = collector;
    }

    /**
     * Reduce the number of objects resulting from the map
     * operations by merging them.
     *
     * @param mapOutput is the result of the map operations.
     * @param info is the object which holds information relating
     *             the document assigned to the reduce operation.
     *
     * @return resulting FragmentInfo object.
     */
    private FragmentInfo reduceFragments(Iterable<FragmentInfo> mapOutput,
                                         DocumentInfo info) {
        FragmentInfo result = new FragmentInfo();

        for (FragmentInfo i : mapOutput) {
            FragmentInfo.mergeFragmentInfos(result, i);
        }

        // leave only max length words in list
        result.filterNonMaxWords();

        // add max length and occurrences of max length to info object
        info.setMaxLength(result.getList().get(0).length());
        info.setMaxLengthOccurrences(result.getMap().get(info.getMaxLength()));

        return result;
    }

    /**
     * Compute the number of words found in assigned document.
     *
     * @param fragmentInfo is an object holding information about
     *                     the assigned fragment.
     *
     * @return number of words found in assigned document.
     */
    private int computeWordCount(FragmentInfo fragmentInfo) {
        int wordCount = 0;

        for (var entry : fragmentInfo.getMap().entrySet()) {
            wordCount += entry.getValue();
        }

        return wordCount;
    }

    /**
     * Compute the rank of a document.
     *
     * @param fragmentInfo is an object holding information about
     *                     the assigned fragment.
     * @param info is the object which holds information relating
     *             the document assigned to the reduce operation.
     */
    private void processFragment(FragmentInfo fragmentInfo,
                                 DocumentInfo info) {

        int wordCount = computeWordCount(fragmentInfo);

        int sum = 0;

        for (var entry : fragmentInfo.getMap().entrySet()) {
            int length = entry.getKey();
            int occurrences = entry.getValue();

            sum += Util.fib(length + 1) * occurrences;
        }

        info.setRank((float) sum / wordCount);
    }


    @Override
    public void reduce(FileEntry key, Iterable<FragmentInfo> mapOutput,
                       Collector<FileEntry, DocumentInfo> reduceOutput) {

        DocumentInfo result = new DocumentInfo();

        // reduce stage
        FragmentInfo reducedFragment = reduceFragments(mapOutput, result);

        // processing stage
        processFragment(reducedFragment, result);

        // add result of processing stage to container
        reduceOutput.collect(key, result);
    }

    @Override
    public void run() {
        this.reduce(this.key, this.mapOutput, this.collector);
    }
}
