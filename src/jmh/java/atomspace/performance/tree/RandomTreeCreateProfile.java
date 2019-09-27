package atomspace.performance.tree;

import atomspace.performance.PerformanceModelParameters;
import atomspace.performance.utils.AtomspaceStoragePerformanceUtils;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.performance.PerformanceModelConfiguration;

public class RandomTreeCreateProfile {


    private static AtomspaceStorage getStorage() {
        return AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        //return AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
        //return AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage();
    }

    public static void main(String[] args) throws Exception {

        int atomsNumber = 100;

        PerformanceModelConfiguration config = new PerformanceModelConfiguration(5, 5, 2, false);
        PerformanceModelParameters params = new PerformanceModelParameters(atomsNumber, 0);
        RandomTreeModelParameters treeParameters = new RandomTreeModelParameters(3, 3, 2);
        RandomTreeModel model = new RandomTreeModel(config, params, treeParameters);

        try (AtomspaceStorage atomspace = getStorage();
             AtomspaceStorageTransaction tx = atomspace.getTx()) {

            // Wait console input
            AtomspaceStoragePerformanceUtils.waitForProfiler();

            model.createAtoms(atomspace);
        }
    }
}
