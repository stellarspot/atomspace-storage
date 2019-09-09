package atomspace.query;

import atomspace.ASTestUtils;
import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import org.junit.Ignore;
import org.junit.Test;

public class BasicQueryEngineRepeatedVariablesTest {

    @Test
    @Ignore
    // Scan whole atomspace
    public void test1() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom atom1 = as.get("Link",
                as.get("Node", "value"),
                as.get("Node", "value"));


        ASAtom query = as.get("Link",
                as.get("VariableNode", "$VALUE"),
                as.get("VariableNode", "$VALUE"));

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new Object[][]{{"$VALUE", as.get("Node", "value")}});
    }

    @Test
    public void test2() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom atom1 = as.get("Link",
                as.get("Node", "A"),
                as.get("Node", "A"),
                as.get("Node", "A"));


        ASAtom atom2 = as.get("Link",
                as.get("Node", "A"),
                as.get("Node", "A"),
                as.get("Node", "B"));


        ASAtom atom3 = as.get("Link",
                as.get("Node", "B"),
                as.get("Node", "A"),
                as.get("Node", "B"));


        ASAtom query = as.get("Link",
                as.get("VariableNode", "$VALUE"),
                as.get("Node", "A"),
                as.get("VariableNode", "$VALUE"));

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new Object[][]{{"$VALUE", as.get("Node", "A")}},
                new Object[][]{{"$VALUE", as.get("Node", "B")}});
    }
}
