

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Class which assigns the mapping operations to workers
 * using the ExecutorService model.
 */
public class MapSupervisor {
    private static final int OPERATION_TIMEOUT = 100;

    /**
     * Start the mapping operations.
     *
     * @param fragmentList is the list of fragments on which the mappings
     *                     are done.
     * @param outputContainer is a container used to store the map results.
     * @param workerCount is the number of workers to be started.
     * @throws InterruptedException if the thread calling this methods
     * is interrupted while waiting for the workers to be finished.
     */
    public static void startOperations(List<Fragment> fragmentList,
                                       MapOutput outputContainer,
                                       int workerCount)
            throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(workerCount);

        for (Fragment fragment : fragmentList) {
            // submit new task to executor service
            es.submit(new Map(outputContainer, fragment,
                    fragment.getFile()));
        }

        es.shutdown();

        // wait for all operations to finish
        es.awaitTermination(OPERATION_TIMEOUT, TimeUnit.SECONDS);
    }
}
