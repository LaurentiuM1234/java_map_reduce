

import java.io.IOException;

public interface Text<K> {
    /**
     * Method used to obtain the object to be
     * processed by the map operations.
     *
     * @return object to be processed.
     *
     * @throws IOException if there are any I/O operations that failed.
     */
    K getText() throws IOException;
}
