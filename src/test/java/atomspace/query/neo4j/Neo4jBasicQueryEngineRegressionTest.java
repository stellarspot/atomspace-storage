package atomspace.query.neo4j;

import atomspace.query.AbstractBasicQueryEngineRegressionTest;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.ASMemoryTestUtils;
import atomspace.storage.neo4j.ASNeo4jTestUtils;

public class Neo4jBasicQueryEngineRegressionTest extends AbstractBasicQueryEngineRegressionTest {

    @Override
    protected AtomspaceStorage getStorage() {
        return ASNeo4jTestUtils.getTestStorage();
    }

}
