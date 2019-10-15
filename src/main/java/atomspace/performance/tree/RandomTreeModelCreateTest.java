package atomspace.performance.tree;

import atomspace.performance.PerformanceModel;
import atomspace.performance.PerformanceModelConfiguration;
import atomspace.performance.PerformanceModelParameters;
import atomspace.performance.runner.*;

import java.util.List;

import static atomspace.performance.runner.RunnerStorages.*;

public class RandomTreeModelCreateTest {

    public static void main(String[] args) throws Exception {

        String prefix = "create";

        StorageWrapper[] wrappers = new StorageWrapper[]{
                getMemoryStorageWrapper(prefix),
                getRelationalDBStorageWrapper(prefix),
                getNeo4jStorageWrapper(prefix),
                getJanusGraphStorageWrapper(prefix),
        };

        int[] statements = {100, 200, 300, 400, 500};
        ModelRunner runner = new RandomTreeCreateModelRunner(3, 3, 2);
        WarmupProperties warmup = new WarmupProperties(1, statements[2]);

        List<Measurement> results = RunnerUtils.measure(runner, wrappers, statements, warmup);

        for (Measurement result : results) {
            System.out.printf("result: %s%n", result);
        }

        RunnerUtils.showPlotter(results);
    }

    static class RandomTreeCreateModelRunner implements ModelRunner {

        final int randomTreeSize;
        final int maxTypes;
        final int maxVariables;

        public RandomTreeCreateModelRunner(int randomTreeSize, int maxTypes, int maxVariables) {
            this.randomTreeSize = randomTreeSize;
            this.maxTypes = maxTypes;
            this.maxVariables = maxVariables;
        }


        @Override
        public PerformanceModel getModel(int statements) {
            PerformanceModelConfiguration config = new PerformanceModelConfiguration(maxTypes, maxTypes, maxTypes, true);
            PerformanceModelParameters params = new PerformanceModelParameters(statements, -1, 10);
            RandomTreeModelParameters treeParams = new RandomTreeModelParameters(randomTreeSize, randomTreeSize, maxVariables);
            return new RandomTreeModel(config, params, treeParams);
        }

        @Override
        public void init(PerformanceModel model, StorageWrapper wrapper) throws Exception {
            wrapper.clean();
        }

        @Override
        public void run(PerformanceModel model, StorageWrapper wrapper) throws Exception {
            model.createAtoms(wrapper.getStorage());
        }
    }
}

