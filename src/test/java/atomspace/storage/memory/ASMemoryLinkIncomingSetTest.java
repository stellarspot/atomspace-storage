package atomspace.storage.memory;

import atomspace.ASTestUtils;
import atomspace.storage.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.Random;

public class ASMemoryLinkIncomingSetTest {

    // Node("value")
    @Test
    public void testEmptyIncomingSet() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom node = as.get("Node", "value");
        ASIncomingSet incomingSet = node.getIncomingSet();
        Iterator<ASLink> iter = incomingSet.getIncomingSet("Node", 0);
        Assert.assertFalse(iter.hasNext());
    }

    // Link(Node("value"))
    @Test
    public void testIncomingSet1() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom node = as.get("Node", "value");
        ASLink link = (ASLink) as.get("Link", node);

        ASIncomingSet nodeIncomingSet = node.getIncomingSet();

        Iterator<ASLink> iter1 = nodeIncomingSet.getIncomingSet("Node", 0);
        ASTestUtils.assertIteratorEquals(iter1);

        Iterator<ASLink> iter2 = nodeIncomingSet.getIncomingSet("Link", 0);
        ASTestUtils.assertIteratorEquals(iter2, link);

        Iterator<ASLink> iter3 = nodeIncomingSet.getIncomingSet("Link", 1);
        ASTestUtils.assertIteratorEquals(iter3);
    }
}
