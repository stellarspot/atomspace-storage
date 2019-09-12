package atomspace.storage.memory;

import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import org.junit.Assert;
import org.junit.Test;

public class ASMemoryLinkTest {

    @Test
    public void testNotNull() {

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();

        Assert.assertNotNull(
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value-2")));
    }

    @Test
    public void testEquals() {

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
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

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
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

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
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

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
        Assert.assertEquals(
                "Link(Node1('value1'),Node2('value2'))",
                as.get("Link",
                        as.get("Node1", "value1"),
                        as.get("Node2", "value2")).toString());
    }
}
