

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReduceSupervisor {
    private static final int OPERATION_TIMEOUT = 100;

    /**
     * Start the reduce operations.
     *
     * @param mapOutput is the Collector object resulting from
     *                  the map operations.
     * @param outputContainer is a container used to store
     *                        the reduce results.
     * @param workerCount is the number of workers to be started.
     *
     * @throws InterruptedException if the thread calling this
     * methods is interrupted while waiting for the workers to be finished.
     */
    public static void startOperations(MapOutput mapOutput,
                                       ReduceOutput outputContainer,
                                       int workerCount)
            throws InterruptedException {

        ExecutorService es = Executors.newFixedThreadPool(workerCount);

        for (var entry : mapOutput.getMap().entrySet()) {
            // submit new task to executor service
            es.submit(new Reduce(entry.getKey(),
                    entry.getValue(),
                    outputContainer));
        }

        es.shutdown();

        // wait for all operations to finish
        es.awaitTermination(OPERATION_TIMEOUT, TimeUnit.SECONDS);
    }
}
