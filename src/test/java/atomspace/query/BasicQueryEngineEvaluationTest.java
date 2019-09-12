package atomspace.query;

import atomspace.ASTestUtils;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.query.basic.ASBasicQueryEngine;
import org.junit.Test;

import static atomspace.ASTestUtils.KeyWithValue;

public class BasicQueryEngineEvaluationTest {

    @Test
    public void test1() {
        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();

        ASAtom link = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("ConceptNode", "object")));

        ASAtom query = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("VariableNode", "$OBJECT")));


        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new KeyWithValue[]{new KeyWithValue("$OBJECT", as.get("ConceptNode", "object"))});
    }

    @Test
    public void test2() {
        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();

        ASAtom link1 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("ConceptNode", "object1")));

        ASAtom link2 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("ConceptNode", "object2")));

        ASAtom query = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("VariableNode", "$OBJECT")));

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new KeyWithValue[]{new KeyWithValue("$OBJECT", as.get("ConceptNode", "object1"))},
                new KeyWithValue[]{new KeyWithValue("$OBJECT", as.get("ConceptNode", "object2"))});
    }

    @Test
    public void test3() {
        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();

        ASAtom link1 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("ConceptNode", "object1")));

        ASAtom link2 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("ConceptNode", "object2")));

        ASAtom link3 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject3"),
                        as.get("ConceptNode", "object3")));

        ASAtom link4 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate1"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("ConceptNode", "object")));

        ASAtom query = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject"),
                        as.get("VariableNode", "$OBJECT")));

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new KeyWithValue[]{new KeyWithValue("$OBJECT", as.get("ConceptNode", "object1"))},
                new KeyWithValue[]{new KeyWithValue("$OBJECT", as.get("ConceptNode", "object2"))});
    }
}
