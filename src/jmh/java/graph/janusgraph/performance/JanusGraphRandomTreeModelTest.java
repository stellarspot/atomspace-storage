package graph.janusgraph.performance;

import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageTransaction;
import atomspace.storage.performance.PerformanceModelConfiguration;
import atomspace.storage.performance.PerformanceModelParameters;
import atomspace.storage.performance.tree.RandomTreeModel;
import atomspace.storage.performance.tree.RandomTreeModelParameters;
import atomspace.storage.performance.utils.AtomspaceStoragePerformanceUtils;
import atomspace.storage.util.AtomspaceStorageHelper;

public class JanusGraphRandomTreeModelTest {

    public static void main(String[] args) throws Exception {

        AtomspaceStoragePerformanceUtils.waitForProfiler();
        PerformanceModelConfiguration config = new PerformanceModelConfiguration(3, 3, 3, true);
        PerformanceModelParameters params = new PerformanceModelParameters(500, 1, 1);
        RandomTreeModelParameters treeParams = new RandomTreeModelParameters(3, 3, 2);
        RandomTreeModel model = new RandomTreeModel(config, params, treeParams);
//        model.dump();

        try (AtomspaceJanusGraphStorage atomspace = AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage()) {
            try (AtomspaceJanusGraphStorageTransaction tx = atomspace.getTx()) {

                long time = System.currentTimeMillis();
                model.createAtoms(atomspace);
                tx.commit();
                System.out.printf("elapsed time: %ds%n", System.currentTimeMillis() - time);
            }
        }
    }

    private static void printStatistics(AtomspaceJanusGraphStorage atomspace, String msg) {
        try (AtomspaceJanusGraphStorageTransaction tx = atomspace.getTx()) {
            AtomspaceStorageHelper helper = new AtomspaceJanusGraphStorageHelper(tx);
            helper.printStatistics(msg);
        }
    }
}
