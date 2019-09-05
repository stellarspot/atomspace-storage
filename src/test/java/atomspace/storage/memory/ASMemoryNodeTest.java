package atomspace.storage.memory;

import org.junit.Test;
import org.junit.Assert;
import atomspace.storage.AtomspaceStorage;

public class ASMemoryNodeTest {

    @Test
    public void testNotNull() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertNotNull(
                as.get("Node", "value"));
    }

    @Test
    public void testEquals() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.get("Node", "value"),
                as.get("Node", "value"));
    }

    @Test
    public void testHashcode() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.get("Node", "value").hashCode(),
                as.get("Node", "value").hashCode());

    }

    @Test
    public void testSame() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.get("Node", "value"),
                as.get("Node", "value"));
    }

    @Test
    public void testToString() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                "Node('value')",
                as.get("Node", "value").toString());
    }
}
