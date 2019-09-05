package atomspace.storage.memory.query;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.memory.query.basic.ASBasicQueryEngine;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Iterator;

public class BasicQueryEngineTest {

    @Test
    @Ignore
    public void test() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom atom = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("ObjectNode", "object"));

        ASAtom query = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("VariableNode", "$WHAT"));

        Iterator<ASAtom> iter = as.getAtoms();
        while (iter.hasNext()) {
            System.out.printf("atom: %s%n", iter.next());
        }

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        Iterator<ASAtom> res = queryEngine.query(query);

        while (res.hasNext()) {
            System.out.printf("result: %s%n", res.next());
        }
    }
}
