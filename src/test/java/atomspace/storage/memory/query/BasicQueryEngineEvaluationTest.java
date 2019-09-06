package atomspace.storage.memory.query;

import atomspace.ASTestUtils;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.memory.query.basic.ASBasicQueryEngine;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

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
}
