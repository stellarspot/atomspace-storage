package atomspace.query;

import atomspace.ASTestUtils;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.query.basic.ASBasicQueryEngine;
import org.junit.Test;

public class BasicQueryEngineEvaluationTest {

    @Test
    public void test1() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

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
                new Object[][]{{"$OBJECT", as.get("ConceptNode", "object")}});
    }

    @Test
    public void test2() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

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
                new Object[][]{{"$OBJECT", as.get("ConceptNode", "object1")}},
                new Object[][]{{"$OBJECT", as.get("ConceptNode", "object2")}});
    }

    @Test
    public void test3() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

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
                new Object[][]{{"$OBJECT", as.get("ConceptNode", "object1")}},
                new Object[][]{{"$OBJECT", as.get("ConceptNode", "object2")}});
    }
}
