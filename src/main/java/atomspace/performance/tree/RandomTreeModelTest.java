package atomspace.performance.tree;

import atomspace.performance.PerformanceModelConfiguration;
import atomspace.performance.PerformanceModelParameters;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.util.AtomspaceStorageHelper;

public class RandomTreeModelTest {


    private static AtomspaceStorage getStorage() {
//        return new AtomspaceMemoryStorage();
        return AtomspaceJanusGraphStorageHelper.getJanusGraphInMemoryStorage();
    }

    public static void main(String[] args) throws Exception {

        //AtomspaceStoragePerformanceUtils.waitForProfiler();

        PerformanceModelConfiguration config = new PerformanceModelConfiguration(3, 3, 3, true);
        PerformanceModelParameters params = new PerformanceModelParameters(500, 1, 1);
        RandomTreeModelParameters treeParams = new RandomTreeModelParameters(3, 3, 2);
        RandomTreeModel model = new RandomTreeModel(config, params, treeParams);
//        model.dump();

        try (AtomspaceStorage atomspace = getStorage()) {
            try (AtomspaceStorageTransaction tx = atomspace.getTx()) {

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
