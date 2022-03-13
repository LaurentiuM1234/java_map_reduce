

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Class used to parse input.
 */
public class InputParser {
    private final String pathname;

    public InputParser(String pathname) {
        this.pathname = pathname;
    }

    /**
     * Split all the provided files into Fragment objects.
     *
     * @return list of resulting fragments.
     * @throws IOException if any of the file-related operations fail
     */
    public List<Fragment> split() throws IOException {
        List<Fragment> fragmentList = new ArrayList<>();

        File file = new File(this.pathname);
        Scanner scanner = new Scanner(file);

        int fragmentSize = scanner.nextInt();
        scanner.nextLine();

        int fileCount = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < fileCount; i++) {
            String pathname = scanner.nextLine();

            int fileSize = (int) Files.size(Paths.get(pathname));

            for (int j = 0; j < fileSize; j += fragmentSize) {
                if (j + fragmentSize > fileSize) {
                    // if we can't add a fragment of given size then truncate fragment size
                    fragmentList.add(new Fragment(new FileEntry(pathname, i),
                            j,
                            fileSize - j));
                } else {
                    fragmentList.add(new Fragment(new FileEntry(pathname, i),
                            j,
                            fragmentSize));
                }
            }
        }
        scanner.close();

        return fragmentList;
    }
}
