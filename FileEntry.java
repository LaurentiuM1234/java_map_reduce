

import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Class which holds information about a file
 * taken from the input file
 */
public class FileEntry {
    private String pathname;
    private final int index;

    public FileEntry(String pathname, int index) {
        this.pathname = pathname;
        this.index = index;
    }

    public String getPathname() {
        return pathname;
    }

    public int getIndex() {
        return index;
    }

    /**
     * Extract file name from path name and update
     * the pathname attribute.
     */
    public void truncatePathnameToFilename() {
        Path path = Paths.get(this.pathname);

        this.pathname = path.getFileName().toString();
    }

    @Override
    public int hashCode() {
        return this.pathname.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof FileEntry)
                && (this.hashCode() == obj.hashCode());
    }
}
