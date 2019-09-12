package atomspace.storage;

import atomspace.ASTestUtils;
import atomspace.storage.*;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;
import java.util.Random;

public abstract class ASAbstractIncomingSetTest extends ASAbstractTest {

    // Node("value")
    @Test
    public void testEmptyIncomingSet() throws Exception {

        testAtomspaceStorage(as -> {

            ASAtom node = as.get("Node", "value");
            assertIncomingSet(node, "Node", 0, 0);
        });
    }

    // Link(Node("value"))
    @Test
    public void testIncomingSet1() throws Exception {

        testAtomspaceStorage(as -> {

            ASAtom node = as.get("Node", "value");
            ASLink link = (ASLink) as.get("Link", node);

            ASIncomingSet nodeIncomingSet = node.getIncomingSet();
            assertIncomingSet(nodeIncomingSet, "Node", 0, 0);
            assertIncomingSet(nodeIncomingSet, "Link", 1, 0, link);
            assertIncomingSet(nodeIncomingSet, "Link", 0, 1);
        });
    }


    // Link(Node("value"), Node("value"))
    @Test
    public void testIncomingSet11() throws Exception {

        testAtomspaceStorage(as -> {

            ASAtom node = as.get("Node", "value");
            ASLink link = (ASLink) as.get("Link", node, node);

            ASIncomingSet nodeIncomingSet = node.getIncomingSet();
            assertIncomingSet(nodeIncomingSet, "Link", 1, 0, link);
            assertIncomingSet(nodeIncomingSet, "Link", 1, 1, link);
        });
    }


    // Link(Node1("value"), Node2("value"))
    @Test
    public void testIncomingSet12() throws Exception {

        testAtomspaceStorage(as -> {

            ASAtom node1 = as.get("Node1", "value");
            ASAtom node2 = as.get("Node2", "value");
            ASLink link = (ASLink) as.get("Link", node1, node2);

            // node1
            ASIncomingSet nodeIncomingSet1 = node1.getIncomingSet();

            assertIncomingSet(nodeIncomingSet1, "Link", 1, 0, link);
            assertIncomingSet(nodeIncomingSet1, "Link", 0, 1);

            // node2
            ASIncomingSet nodeIncomingSet2 = node2.getIncomingSet();

            assertIncomingSet(nodeIncomingSet2, "Link", 0, 0);
            assertIncomingSet(nodeIncomingSet2, "Link", 1, 1, link);
        });
    }


    // Link1(Node("value")), Link2(Node("value"))
    @Test
    public void testIncomingSetLinks12() throws Exception {

        testAtomspaceStorage(as -> {

            ASAtom node = as.get("Node", "value");
            ASLink link1 = (ASLink) as.get("Link1", node);
            ASLink link2 = (ASLink) as.get("Link2", node);

            ASIncomingSet incomingSet = node.getIncomingSet();

            assertIncomingSet(incomingSet, "Link1", 1, 0, link1);
            assertIncomingSet(incomingSet, "Link2", 1, 0, link2);
        });
    }

    private static void assertIncomingSet(ASAtom atom,
                                          String type,
                                          int size,
                                          int position,
                                          ASLink... links) {
        assertIncomingSet(atom.getIncomingSet(), type, size, position, links);
    }

    private static void assertIncomingSet(ASIncomingSet incomingSet,
                                          String type,
                                          int size,
                                          int position,
                                          ASLink... links) {
        Assert.assertEquals(size, incomingSet.getIncomingSetSize(type, position));
        Iterator<ASLink> iter = incomingSet.getIncomingSet(type, position);
        ASTestUtils.assertIteratorEquals(iter, links);
    }
}
