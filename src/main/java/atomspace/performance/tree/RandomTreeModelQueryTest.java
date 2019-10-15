package atomspace.performance.tree;

import atomspace.performance.PerformanceModel;
import atomspace.performance.PerformanceModelConfiguration;
import atomspace.performance.PerformanceModelParameters;
import atomspace.performance.runner.*;
import atomspace.performance.runner.RunnerStorages.JanusGraphStorageWrapper;
import atomspace.performance.runner.RunnerStorages.MemoryStorageWrapper;
import atomspace.performance.runner.RunnerStorages.Neo4jStorageWrapper;
import atomspace.query.ASQueryEngine;
import atomspace.query.basic.ASBasicQueryEngine;

import java.util.List;

import static atomspace.performance.runner.RunnerStorages.*;

public class RandomTreeModelQueryTest {

    public static void main(String[] args) throws Exception {

        StorageWrapper[] wrappers = new StorageWrapper[]{
                new MemoryStorageWrapper(),
                new RelationalDBStorageWrapper(),
                new Neo4jStorageWrapper(),
                new JanusGraphStorageWrapper(),
        };

        int[] statements = {100, 200, 300, 400, 500};
        ModelRunner runner = new RandomTreeQueryModelRunner(3, 3, 3, 200);
        WarmupProperties warmup = new WarmupProperties(1, statements[2]);

        List<Measurement> results = RunnerUtils.measure(runner, wrappers, statements, warmup);

        for (Measurement result : results) {
            System.out.printf("result: %s%n", result);
        }

        RunnerUtils.showPlotter(results);
    }

    static class RandomTreeQueryModelRunner implements ModelRunner {

        final int randomTreeSize;
        final int maxTypes;
        final int maxVariables;
        final int statements;

        final ASQueryEngine queryEngine = new ASBasicQueryEngine();

        public RandomTreeQueryModelRunner(int randomTreeSize, int maxTypes, int maxVariables, int statements) {
            this.randomTreeSize = randomTreeSize;
            this.maxTypes = maxTypes;
            this.maxVariables = maxVariables;
            this.statements = statements;
        }


        @Override
        public PerformanceModel getModel(int queries) {
            PerformanceModelConfiguration config = new PerformanceModelConfiguration(maxTypes, maxTypes, maxTypes, true);
            PerformanceModelParameters params = new PerformanceModelParameters(statements, queries, 10);
            RandomTreeModelParameters treeParams = new RandomTreeModelParameters(randomTreeSize, randomTreeSize, maxVariables);
            return new RandomTreeModel(config, params, treeParams);
        }

        @Override
        public void init(PerformanceModel model, StorageWrapper wrapper) throws Exception {
            wrapper.clean();
            model.createAtoms(wrapper.getStorage());
        }

        @Override
        public void run(PerformanceModel model, StorageWrapper wrapper) throws Exception {
            model.queryAtoms(wrapper.getStorage(), queryEngine);
        }
    }
}

