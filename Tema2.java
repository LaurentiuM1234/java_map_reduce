


import java.io.IOException;
import java.util.List;

public class Tema2 {

    public static void main(String[] args) {
        if (args.length < 3) {
            System.err.println("Usage: com.map.com.base.Tema2 <workers> <in_file> <out_file>");
            return;
        }

        try {
            List<Fragment> fragments = new InputParser(args[1]).split();

            MapOutput outputContainer = new MapOutput();

            ReduceOutput reduceOutput = new ReduceOutput();

            MapSupervisor.startOperations(fragments,
                    outputContainer,
                    Integer.parseInt(args[0]));

            ReduceSupervisor.startOperations(outputContainer,
                    reduceOutput,
                    Integer.parseInt(args[0]));

            OutputWriter.writeReduceResult(reduceOutput, args[2]);
        } catch (InterruptedException | IOException exception) {
            exception.printStackTrace();
        }
    }
}
