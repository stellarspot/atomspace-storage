package atomspace.query;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.query.basic.BasicQueryEngine;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Iterator;

public class BasicQueryEngineTest {

    @Test
    @Ignore
    public void test() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        as.get("PredicateLink",
                as.get("SubjectNode", "subject1"),
                as.get("ObjectNode", "object1"));

        as.get("PredicateLink",
                as.get("SubjectNode", "subject2"),
                as.get("ObjectNode", "object2"));

        Iterator<ASAtom> iter = as.getAtoms();
        while (iter.hasNext()) {
            System.out.printf("atom: %s%n", iter.next());
        }

        ASAtom query = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("VariableNode", "$WHAT"));


        QueryEngine queryEngine = new BasicQueryEngine();

        Iterator<ASAtom> res = queryEngine.query(query);

        while (res.hasNext()) {
            System.out.printf("result: %s%n", res.next());
        }
    }
}
