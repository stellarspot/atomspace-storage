package atomspace.storage.memory;

import atomspace.storage.AtomspaceStorageTransaction;
import org.junit.Test;
import org.junit.Assert;
import atomspace.storage.AtomspaceStorage;

public class ASMemoryNodeTest {

    @Test
    public void testNotNull() {

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
        Assert.assertNotNull(
                as.get("Node", "value"));
    }

    @Test
    public void testEquals() {

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
        Assert.assertEquals(
                as.get("Node", "value"),
                as.get("Node", "value"));
    }

    @Test
    public void testHashcode() {

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
        Assert.assertEquals(
                as.get("Node", "value").hashCode(),
                as.get("Node", "value").hashCode());

    }

    @Test
    public void testSame() {

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
        Assert.assertEquals(
                as.get("Node", "value"),
                as.get("Node", "value"));
    }

    @Test
    public void testToString() {

        AtomspaceStorageTransaction as = new AtomspaceMemoryStorage().getTx();
        Assert.assertEquals(
                "Node('value')",
                as.get("Node", "value").toString());
    }
}
