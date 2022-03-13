

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class used to facilitate the output writing operation.
 */
public class OutputWriter {

    /**
     * Class used to sort the information in the Collector object
     * provided by the reduce operations.
     */
    private static class OutputInformation
            implements Comparable<OutputInformation> {
        private final FileEntry file;
        private final float rank;
        private final int maxLength;
        private final int maxLengthOccurrences;

        public OutputInformation(FileEntry file, DocumentInfo info) {
            this.file = file;
            this.file.truncatePathnameToFilename();
            this.rank = info.getRank();
            this.maxLength = info.getMaxLength();
            this.maxLengthOccurrences = info.getMaxLengthOccurrences();
        }

        public FileEntry getFile() {
            return file;
        }

        public float getRank() {
            return rank;
        }

        public int getMaxLength() {
            return maxLength;
        }

        public int getMaxLengthOccurrences() {
            return maxLengthOccurrences;
        }

        /**
         * Convert the Collector object from reduce operations into a list
         * of objects of type OutputInformation.
         *
         * @param output is the Collector object resulting from the reduce operations.
         * @return list of OutputInformation objects.
         */
        public static List<OutputInformation> convertReduceOutput(ReduceOutput output) {
            List<OutputInformation> result = new ArrayList<>();

            for (var entry : output.getMap().entrySet()) {
                result.add(new OutputInformation(entry.getKey(), entry.getValue()));
            }

            Collections.sort(result);

            return result;
        }

        @Override
        public int compareTo(OutputInformation o) {
            if (this.rank > o.rank) {
                return -1;
            } else if (this.rank < o.rank) {
                return 1;
            } else {
                return Integer.compare(this.file.getIndex(), o.file.getIndex());
            }
        }
    }

    /**
     * Write the results of the reduce operations to file.
     *
     * @param output is the Collector object resulting from the reduce operations.
     * @param pathname is the path to the file in which the writing is done.
     * @throws IOException if any of the file-related operations fail.
     */
    public static void writeReduceResult(ReduceOutput output, String pathname)
            throws IOException {
        FileWriter fileWriter = new FileWriter(pathname);

        List<OutputInformation> outputList
                = OutputInformation.convertReduceOutput(output);

        for (var entry : outputList) {
            fileWriter.write(entry.getFile().getPathname() + "," +
                    String.format("%.2f", entry.getRank()) +
                    "," + entry.getMaxLength() + "," +
                    entry.getMaxLengthOccurrences() + "\n");
        }

        fileWriter.close();
    }
}
