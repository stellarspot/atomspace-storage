package atomspace.performance.tree;

import atomspace.performance.PerformanceModelConfiguration;
import atomspace.performance.PerformanceModelParameters;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.ASTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.janusgraph.ASJanusGraphTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorageHelper;
import atomspace.storage.util.AtomspaceStorageHelper;
import atomspace.storage.util.AtomspaceStorageUtils;

import java.util.Scanner;

public class RandomTreeModelTest {

    private static AtomspaceStorage getStorage() {
        return getRelationalDBStorage();
    }

    private static AtomspaceStorage getMemoryGraphStorage() {
        return new AtomspaceMemoryStorage();
    }

    private static AtomspaceStorage getRelationalDBStorage() {
        String dir = "/tmp/atomspace-storage/performance/relationaldb";
        AtomspaceStorageUtils.removeDirectory(dir);
        return AtomspaceRelationalDBStorageHelper.getInMemoryStorage(dir);
    }

    private static AtomspaceStorage getJanusGraphStorage() {
        return AtomspaceJanusGraphStorageHelper.getJanusGraphInMemoryStorage();
    }

    public static void main(String[] args) throws Exception {


        int statements = 100;
        int queries = 1;
        PerformanceModelConfiguration config = new PerformanceModelConfiguration(3, 3, 3, true);
        PerformanceModelParameters params = new PerformanceModelParameters(statements, queries, 100);
        RandomTreeModelParameters treeParams = new RandomTreeModelParameters(3, 3, 2);
        RandomTreeModel model = new RandomTreeModel(config, params, treeParams);
        //model.dump();

        //waitForProfiler();

        long time = System.currentTimeMillis();
        try (AtomspaceStorage atomspace = getStorage()) {
            try (ASTransaction tx = atomspace.getTx()) {

                model.createAtoms(atomspace);
                tx.commit();
            }
        }
        System.out.printf("elapsed time: %ds%n", System.currentTimeMillis() - time);
    }

    private static void printStatistics(AtomspaceJanusGraphStorage atomspace, String msg) {
        try (ASJanusGraphTransaction tx = atomspace.getTx()) {
            AtomspaceStorageHelper helper = new AtomspaceJanusGraphStorageHelper(tx);
            helper.printStatistics(msg);
        }
    }

    public static void waitForProfiler() {
        System.out.printf("Type any key when profiler is started.");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
    }
}
