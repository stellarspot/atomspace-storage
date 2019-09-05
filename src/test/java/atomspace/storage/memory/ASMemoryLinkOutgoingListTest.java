package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.AtomspaceStorage;
import org.junit.Assert;
import org.junit.Test;

import java.util.Random;

public class ASMemoryLinkOutgoingListTest {

    @Test
    public void testEmptyOutgoingList() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASLink link = (ASLink) as.get("Link");
        ASOutgoingList outgoingList = link.getOutgoingList();

        Assert.assertEquals(0, outgoingList.getSize());
    }

    @Test
    public void testOutgoingList1() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom node = as.get("Node", "value");
        ASLink link = (ASLink) as.get("Link", node);
        ASOutgoingList outgoingList = link.getOutgoingList();

        Assert.assertEquals(1, outgoingList.getSize());
        Assert.assertEquals(node, outgoingList.getAtom(0));
    }

    @Test
    public void testOutgoingList2() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        ASAtom node1 = as.get("Node1", "value1");
        ASAtom node2 = as.get("Node2", "value2");
        ASLink link = (ASLink) as.get("Link", node1, node2);
        ASOutgoingList outgoingList = link.getOutgoingList();

        Assert.assertEquals(2, outgoingList.getSize());
        Assert.assertEquals(node1, outgoingList.getAtom(0));
        Assert.assertEquals(node2, outgoingList.getAtom(1));
    }

    @Test
    public void testOutgoingListN() {
        AtomspaceStorage as = new AtomspaceMemoryStorage();

        final int max_values = 100;
        int n = new Random().nextInt(max_values);
        ASAtom[] atoms = new ASAtom[n];

        for (int i = 0; i < n; i++) {
            atoms[i] = as.get("Node", Integer.toString(i));
        }
        ASLink link = (ASLink) as.get("Link", atoms);
        ASOutgoingList outgoingList = link.getOutgoingList();

        Assert.assertEquals(n, outgoingList.getSize());
        for (int i = 0; i < n; i++) {
            Assert.assertEquals(atoms[i], outgoingList.getAtom(i));
        }
    }
}
