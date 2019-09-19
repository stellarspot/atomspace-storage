package atomspace.storage.performance.tree;

import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.performance.PerformanceModelConfiguration;
import atomspace.storage.performance.PerformanceModelParameters;
import atomspace.storage.performance.utils.AtomspaceStoragePerformanceUtils;

public class RandomTreeCreateProfile {


    private static AtomspaceStorage getStorage() {
        return AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        //return AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
        //return AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage();
    }

    public static void main(String[] args) throws Exception {

        int atomsNumber = 100;

        PerformanceModelConfiguration config = new PerformanceModelConfiguration(5, 5, 2);
        PerformanceModelParameters params = new PerformanceModelParameters(atomsNumber, 0);
        RandomTreePerformanceModel model = new RandomTreePerformanceModel(config, params, 3, 3, 2);

        try (AtomspaceStorage atomspace = getStorage();
             AtomspaceStorageTransaction tx = atomspace.getTx()) {

            // Wait console input
            AtomspaceStoragePerformanceUtils.waitForProfiler();

            model.createAtoms(atomspace);
        }
    }
}
