package atomspace.query;

import atomspace.ASTestUtils;
import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.ASAbstractTest;
import atomspace.storage.ASAtom;
import org.junit.Ignore;
import org.junit.Test;

import static atomspace.ASTestUtils.KeyWithValue;

public abstract class AbstractBasicQueryEngineRepeatedVariablesTest extends ASAbstractTest {

    @Test
    @Ignore
    // Scan whole atomspace
    public void test1() throws Exception {
        testAtomspaceStorage(as -> {

            ASAtom atom1 = as.get("Link",
                    as.get("Node", "value"),
                    as.get("Node", "value"));


            ASAtom query = as.get("Link",
                    as.get("VariableNode", "$VALUE"),
                    as.get("VariableNode", "$VALUE"));

            ASQueryEngine queryEngine = new ASBasicQueryEngine();

            ASTestUtils.assertQueryResultsEqual(queryEngine.match(query),
                    new KeyWithValue[]{new KeyWithValue("$VALUE", as.get("Node", "value"))});
        });
    }

    @Test
    public void test2() throws Exception {
        testAtomspaceStorage(as -> {

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

            ASTestUtils.assertQueryResultsEqual(queryEngine.match(query),
                    new KeyWithValue[]{new KeyWithValue("$VALUE", as.get("Node", "A"))},
                    new KeyWithValue[]{new KeyWithValue("$VALUE", as.get("Node", "B"))});
        });
    }
}
