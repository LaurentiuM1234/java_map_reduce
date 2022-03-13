

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


/**
 * Class which holds information about a fragment to be
 * processed by the map operations.
 */
public class Fragment implements Text<String> {
    private final FileEntry file;
    private int offset;
    private int size;

    public static String WORD_SPLITTER
            = ";:/? ̃\\.,><‘[]{}()!@#$%ˆ&-_+'=*”|\n\r\t ";

    public Fragment(FileEntry file, int offset, int size) {
        this.file = file;
        this.offset = offset;
        this.size = size;
    }

    public FileEntry getFile() {
        return file;
    }

    /**
     * Adjust fragment offset in order to make it point
     * to a beginning of a word or a delimiter.
     * @throws IOException if any of the file-related operations fail.
     */
    private void adjustFragmentBeginning() throws IOException {
        if (this.offset != 0) {

            // prepare buffer for single character
            byte[] buffer = new byte[1];

            int adjustedOffset = this.offset;

            // open file for reading
            RandomAccessFile file
                    = new RandomAccessFile(this.file.getPathname(), "r");

            while (adjustedOffset - this.offset < this.size) {
                file.seek(adjustedOffset);
                file.readFully(buffer, 0, 1);

                // check if current character is a word separator
                if (WORD_SPLITTER.indexOf(buffer[0]) != -1) {
                    break;
                } else {
                    // offset currently pointing to letter, check previous character to see
                    // if it's a separator
                    file.seek(adjustedOffset - 1);
                    file.readFully(buffer, 0, 1);

                    if (WORD_SPLITTER.indexOf(buffer[0]) != -1) {
                        // current character is the beginning of a word
                        break;
                    } else {
                        adjustedOffset += 1;
                    }
                }
            }
            file.close();
            this.size -= (adjustedOffset - this.offset);
            this.offset = adjustedOffset;
        }
    }

    /**
     * Adjust fragment ending in order to make it point to
     * the end of a word.
     *
     * @throws IOException if any of the file-related operations fail
     */
    private void adjustFragmentEnding() throws IOException {

        // compute file size
        int fileSize = (int) Files.size(Paths.get(this.file.getPathname()));

        if (this.size != fileSize) {

            // prepare buffer for single character
            byte[] buffer = new byte[1];

            int adjustedSize = this.size;

            // open file for reading
            RandomAccessFile file
                    = new RandomAccessFile(this.file.getPathname(), "r");

            while (this.offset + adjustedSize < fileSize) {
                // read current last character
                file.seek(this.offset + adjustedSize - 1);
                file.readFully(buffer, 0, 1);

                if (WORD_SPLITTER.indexOf(buffer[0]) != -1) {
                    break;
                } else {
                    // size currently points to middle of word,
                    // try next character
                    file.seek(this.offset + adjustedSize);
                    file.readFully(buffer, 0, 1);

                    if (WORD_SPLITTER.indexOf(buffer[0]) != -1) {
                        // current character is the beginning of a word
                        break;
                    } else {
                        adjustedSize += 1;
                    }
                }
            }

            this.size = adjustedSize;

            file.close();
        }
    }

    /**
     * Extract the actual text (from the file) to be processed
     * by map operations using the offset and fragment size.
     *
     * @return String object which represents the part of text that
     * was assigned to the Fragment object.
     *
     * @throws IOException if any file-related operations fail
     */
    @Override
    public String getText() throws IOException {
        // before extracting text, adjust beginning and end
        adjustFragmentBeginning();
        adjustFragmentEnding();

        // open file for reading
        RandomAccessFile file
                = new RandomAccessFile(this.file.getPathname(), "r");

        // seek to offset
        file.seek(this.offset);

        // prepare buffer
        byte[] buffer = new byte[this.size];

        // read from offset
        file.readFully(buffer, 0, this.size);

        // close file before finishing
        file.close();

        // convert read bytes to string and return them
        return new String(buffer, StandardCharsets.UTF_8);
    }
}
