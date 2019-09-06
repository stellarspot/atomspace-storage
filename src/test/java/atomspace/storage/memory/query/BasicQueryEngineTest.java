package atomspace.storage.memory.query;

import atomspace.ASTestUtils;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.memory.query.basic.ASBasicQueryEngine;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

public class BasicQueryEngineTest {

    @Test
    public void test() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom atom = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("ObjectNode", "object"));

        ASAtom query = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("VariableNode", "$WHAT"));

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        Map<String, ASAtom> variables = new HashMap<>();
        variables.put("$WHAT", as.get("ObjectNode", "object"));
        ASTestUtils.assertIteratorEquals(queryEngine.match(query), variables);
    }
}
