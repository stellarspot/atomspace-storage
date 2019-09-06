package atomspace.storage.memory.query;

import atomspace.ASTestUtils;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.memory.query.basic.ASBasicQueryEngine;
import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class BasicQueryEngineTest {

    @Test
    public void test1() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom atom = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("ObjectNode", "object"));

        ASAtom query = as.get("PredicateLink",
                as.get("SubjectNode", "subject"),
                as.get("VariableNode", "$WHAT"));

        ASQueryEngine queryEngine = new ASBasicQueryEngine();

        assertVariables(queryEngine.match(query), new Object[][]{{"$WHAT", as.get("ObjectNode", "object")}});
    }

    @Test
    public void test2() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

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

        Map<String, ASAtom> variables1 = new HashMap<>();
        variables1.put("$WHAT", as.get("ObjectNode", "object1"));

        assertVariables(queryEngine.match(query),
                new Object[][]{{"$WHAT", as.get("ObjectNode", "object1")}},
                new Object[][]{{"$WHAT", as.get("ObjectNode", "object2")}});
    }

    private static void assertVariables(Iterator<Map<String, ASAtom>> iter, Object[][]... variables) {

        Set<Map<String, ASAtom>> actual = new HashSet<>();

        while (iter.hasNext()) {
            actual.add(iter.next());
        }

        Set<Map<String, ASAtom>> expected = new HashSet<>();
        for (Object[][] variableMap : variables) {
            Map<String, ASAtom> map = new HashMap<>();
            for (Object[] variable : variableMap) {
                String key = (String) variable[0];
                ASAtom value = (ASAtom) variable[1];
                map.put(key, value);
            }
            expected.add(map);
        }

        Assert.assertEquals(expected, actual);
    }
}
