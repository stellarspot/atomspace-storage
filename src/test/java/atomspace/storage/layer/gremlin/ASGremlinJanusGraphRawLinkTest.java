package atomspace.storage.layer.gremlin;

import atomspace.layer.gremlin.AtomspaceGremlinStorageHelper;
import atomspace.storage.ASAbstractRawLinkTest;
import atomspace.storage.AtomspaceStorage;

public class ASGremlinJanusGraphRawLinkTest extends ASAbstractRawLinkTest {

    @Override
    protected AtomspaceStorage getStorage() {
        return AtomspaceGremlinStorageHelper.getInMemoryJanusGraph(true, true);
    }
}
