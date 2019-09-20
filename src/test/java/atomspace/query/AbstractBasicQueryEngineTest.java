package atomspace.query;

import atomspace.ASTestUtils;
import atomspace.storage.ASAbstractTest;
import atomspace.storage.ASAtom;
import atomspace.query.basic.ASBasicQueryEngine;
import org.junit.Test;

import static atomspace.ASTestUtils.KeyWithValue;

public abstract class AbstractBasicQueryEngineTest extends ASAbstractTest {

    @Test
    public void test1() throws Exception {
        testAtomspaceStorage(as -> {

            ASAtom atom = as.get("PredicateLink",
                    as.get("SubjectNode", "subject"),
                    as.get("ObjectNode", "object"));

            ASAtom query = as.get("PredicateLink",
                    as.get("SubjectNode", "subject"),
                    as.get("VariableNode", "$WHAT"));

            ASQueryEngine queryEngine = new ASBasicQueryEngine();

            ASTestUtils.assertQueryResultsEqual(queryEngine.match(query),
                    new KeyWithValue[]{new KeyWithValue("$WHAT", as.get("ObjectNode", "object"))});
        });
    }

    @Test
    public void test2() throws Exception {
        testAtomspaceStorage(as -> {

            ASAtom atom1 = as.get("PredicateLink",
                    as.get("SubjectNode", "subject"),
                    as.get("ObjectNode", "object1"));

            ASAtom atom2 = as.get("PredicateLink",
                    as.get("SubjectNode", "subject"),
                    as.get("ObjectNode", "object2"));

            ASAtom atom3 = as.get("PredicateLink",
                    as.get("SubjectNode", "subject3"),
                    as.get("ObjectNode", "object3"));

            ASAtom query = as.get("PredicateLink",
                    as.get("SubjectNode", "subject"),
                    as.get("VariableNode", "$WHAT"));

            ASQueryEngine queryEngine = new ASBasicQueryEngine();

            ASTestUtils.assertQueryResultsEqual(queryEngine.match(query),
                    new KeyWithValue[]{new KeyWithValue("$WHAT", as.get("ObjectNode", "object1"))},
                    new KeyWithValue[]{new KeyWithValue("$WHAT", as.get("ObjectNode", "object2"))});
        });
    }
}
