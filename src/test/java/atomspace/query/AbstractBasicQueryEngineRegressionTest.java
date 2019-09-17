package atomspace.query;

import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.ASAbstractTest;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import org.junit.Test;

public abstract class AbstractBasicQueryEngineRegressionTest extends ASAbstractTest {

    @Test
    public void testMatchSubTreeWithDifferentSize() throws Exception {

        try (AtomspaceStorage atomspace = new AtomspaceMemoryStorage();
             AtomspaceStorageTransaction tx = atomspace.getTx()) {

            // Link1(
            //  Node1("value1"),
            //  List2(
            //      Node2("value2"),
            //      Node3("value2")))
            ASAtom atom =
                    tx.get("Link1",
                            tx.get("Node1", "Value1"),
                            tx.get("Link2",
                                    tx.get("Node2", "Value2"),
                                    tx.get("Node3", "Value3")));

            System.out.printf("%s%n", atom);

            // Link1(
            //  Node1("value1"),
            //  List2(
            //      VariableNode("$VARIABLE")))
            ASAtom query =
                    tx.get("Link1",
                            tx.get("Node1", "Value1"),
                            tx.get("Link2",
                                    tx.get("VariableNode", "$VARIABLE")));

            ASQueryEngine queryEngine = new ASBasicQueryEngine();
            queryEngine.match(query);
        }
    }
}
