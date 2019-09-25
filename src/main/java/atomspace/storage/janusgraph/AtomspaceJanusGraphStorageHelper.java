package atomspace.storage.janusgraph;

import atomspace.storage.util.AtomspaceStorageHelper;

public class AtomspaceJanusGraphStorageHelper implements AtomspaceStorageHelper {


    private final AtomspaceJanusGraphStorageTransaction tx;

    public AtomspaceJanusGraphStorageHelper(AtomspaceJanusGraphStorageTransaction tx) {
        this.tx = tx;
    }

    @Override
    public void dump() {
    }

    @Override
    public void reset() {
        tx.reset();
    }

    @Override
    public void printStatistics(String msg) {
        long vertices = tx.g.V().count().next();
        long edges = tx.g.E().count().next();
        System.out.printf("%s vertices: %s, edges: %s%n", msg, vertices, edges);
    }
}
