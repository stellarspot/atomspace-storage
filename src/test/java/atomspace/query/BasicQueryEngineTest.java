package atomspace.query;

import atomspace.ASTestUtils;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.query.basic.ASBasicQueryEngine;
import org.junit.Test;

import java.util.*;

import static atomspace.ASTestUtils.KeyWithValue;

public class BasicQueryEngineTest {

    @Test
    public void test1() {
        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();

        ASAtom atom = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("ObjectNode", "object"));

        ASAtom query = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("VariableNode", "$WHAT"));

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new KeyWithValue[]{new KeyWithValue("$WHAT", as.get("ObjectNode", "object"))});
    }

    @Test
    public void test2() {
        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();

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

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new KeyWithValue[]{new KeyWithValue("$WHAT", as.get("ObjectNode", "object1"))},
                new KeyWithValue[]{new KeyWithValue("$WHAT", as.get("ObjectNode", "object2"))});
    }
}
