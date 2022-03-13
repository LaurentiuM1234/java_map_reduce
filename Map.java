

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Class representing the map operation from MapReduce paradigm
 */
public class Map implements Mapper<FileEntry, FileEntry,
        FragmentInfo, String>, Runnable{
    private final MapOutput outputContainer;
    private final Fragment assignedFragment;
    private final FileEntry key;

    private static final String wordSplitRegex =
            "[ ;:/\\? ̃\\\\\\.,><‘\\[\\]\\{\\}\\(\\)!@#\\$%\\^&\\-\\+'=\\*”\\|\n\r\t_]";


    public Map(MapOutput outputContainer, Fragment assignedFragment,
               FileEntry key) {
        this.outputContainer = outputContainer;
        this.assignedFragment = assignedFragment;
        this.key = key;
    }


    @Override
    public void map(FileEntry key, Text<String> text,
                    Collector<FileEntry, FragmentInfo> output) {
        try {
            String string = text.getText();

            if (string.length() == 0) {
                // ignore empty texts
                return;
            }

            // split text into tokens
            List<String> tokens = Arrays.asList(string.split(wordSplitRegex));

            // filter out any words with length equals to 0
            tokens = tokens.stream()
                    .filter(str -> str.length() > 0)
                    .collect(Collectors.toList());

            // compute length list
            List<Integer> lengthList = tokens.stream()
                    .map(String::length)
                    .collect(Collectors.toList());

            // find max length
            int maxLength = Collections.max(lengthList);

            // filter out words which don't have max length
            List<String> maxLengthWords = tokens.stream()
                    .filter(str -> str.length() == maxLength)
                    .collect(Collectors.toList());

            // add all computed information to container
            FragmentInfo fragmentInfo = new FragmentInfo();

            fragmentInfo.batchUpdateMap(lengthList);
            fragmentInfo.batchUpdateList(maxLengthWords);

            // add information to universal collector
            output.collect(key, fragmentInfo);
        } catch (IOException ioException) {
            ioException.printStackTrace();
        }
    }

    @Override
    public void run() {
        this.map(key, assignedFragment, outputContainer);
    }
}
