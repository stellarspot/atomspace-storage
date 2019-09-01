package atomspace.storage.memory;

import atomspace.storage.AtomspaceStorage;
import org.junit.Assert;
import org.junit.Test;

public class AtomspaceMemoryStorageLinkTest {

    @Test
    public void testNotNull() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertNotNull(
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")));
    }

    @Test
    public void testEquals() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")),
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")));
    }

    @Test
    public void testHashcode() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")).hashCode(),
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")).hashCode());
    }

    @Test
    public void testSame() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertSame(
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")),
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")));
    }

    @Test
    public void testToString() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                "Link(Node1('value1'),Node2('value2'))",
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value2")).toString());
    }
}
