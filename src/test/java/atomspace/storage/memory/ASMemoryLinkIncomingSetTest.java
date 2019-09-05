package atomspace.storage.memory;

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
        ASAtom link = as.get("Link", node);


        ASIncomingSet incomingSet = node.getIncomingSet();

        Iterator<ASLink> iter1 = incomingSet.getIncomingSet("Node", 0);
        Assert.assertFalse(iter1.hasNext());

        Iterator<ASLink> iter2 = incomingSet.getIncomingSet("Link", 0);
        Assert.assertTrue(iter2.hasNext());

        Iterator<ASLink> iter3 = incomingSet.getIncomingSet("Link", 1);
        Assert.assertFalse(iter3.hasNext());
    }
}
