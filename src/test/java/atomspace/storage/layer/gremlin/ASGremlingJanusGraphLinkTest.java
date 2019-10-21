package atomspace.storage.layer.gremlin;

import atomspace.layer.gremlin.AtomspaceGremlinStorageHelper;
import atomspace.storage.ASAbstractLinkTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.janusgraph.ASJanusGraphTestUtils;
import org.junit.Test;

public class ASGremlingJanusGraphLinkTest extends ASAbstractLinkTest {

    @Override
    protected AtomspaceStorage getStorage() {
        return AtomspaceGremlinStorageHelper.getInMemoryJanusGraph(true);
    }
}
