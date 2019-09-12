package atomspace.query;

import atomspace.ASTestUtils;
import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import org.junit.Test;

import static atomspace.ASTestUtils.KeyWithValue;

public class BasicQueryEngineLinkTest {

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
                as.get("VariableNode", "$LIST_LINK"));


        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new KeyWithValue[]{
                        new KeyWithValue<>("$LIST_LINK", as.get("ListLink",
                                as.get("ConceptNode", "subject"),
                                as.get("ConceptNode", "object")))});
    }

    @Test
    public void test2() {
        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();

        ASAtom link1 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate1"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject1"),
                        as.get("ConceptNode", "object1")));

        ASAtom link2 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate1"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject2"),
                        as.get("ConceptNode", "object2")));

        ASAtom link3 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate2"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject1"),
                        as.get("ConceptNode", "object1")));

        ASAtom link4 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate2"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject2"),
                        as.get("ConceptNode", "object2")));

        ASAtom link5 = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate3"),
                as.get("ListLink",
                        as.get("ConceptNode", "subject3"),
                        as.get("ConceptNode", "object3")));

        ASAtom query = as.get("EvaluationLink",
                as.get("PredicateNode", "predicate1"),
                as.get("VariableNode", "$LIST_LINK"));


        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        ASTestUtils.assertIteratorOfMapsEqual(queryEngine.match(query),
                new KeyWithValue[]{
                        new KeyWithValue<>("$LIST_LINK", as.get("ListLink",
                                as.get("ConceptNode", "subject1"),
                                as.get("ConceptNode", "object1")))},
                new KeyWithValue[]{
                        new KeyWithValue<>("$LIST_LINK", as.get("ListLink",
                                as.get("ConceptNode", "subject2"),
                                as.get("ConceptNode", "object2")))});

    }
}
