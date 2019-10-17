package atomspace.storage.layer.gremlin;

import atomspace.layer.gremlin.AtomspaceGremlinStorageHelper;
import atomspace.storage.ASAbstractNodeTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.janusgraph.ASJanusGraphTestUtils;

public class ASGremlinJanusGraphNodeTest extends ASAbstractNodeTest {

    @Override
    public AtomspaceStorage getStorage() {
        return AtomspaceGremlinStorageHelper.getInMemoryJanusGraph();
    }
}
