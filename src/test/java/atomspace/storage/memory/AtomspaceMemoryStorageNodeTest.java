package atomspace.storage.memory;

import org.junit.Test;
import org.junit.Assert;
import atomspace.storage.AtomspaceStorage;

public class AtomspaceMemoryStorageNodeTest {

    @Test
    public void testEquals() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.getOrCreateNode("Concept", "item-1"),
                as.getOrCreateNode("Concept", "item-1"));
    }

    @Test
    public void testHashcode() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.getOrCreateNode("Concept", "item-1").hashCode(),
                as.getOrCreateNode("Concept", "item-1").hashCode());

    }

    @Test
    public void testSame() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                as.getOrCreateNode("Concept", "value"),
                as.getOrCreateNode("Concept", "value"));
    }

    @Test
    public void testToString() {

        AtomspaceStorage as = new AtomspaceMemoryStorage();
        Assert.assertEquals(
                "Concept('value')",
                as.getOrCreateNode("Concept", "value").toString());
    }
}
