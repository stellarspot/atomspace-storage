package atomspace.storage;

import org.junit.Assert;
import org.junit.Test;

public abstract class ASAbstractLinkTest extends ASAbstractTest {

    @Test
    public void testNotNull() throws Exception {

        testAtomspaceStorage(as ->
                Assert.assertNotNull(
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value-2"))));
    }

    @Test
    public void testEquals() throws Exception {

        testAtomspaceStorage(as ->
                Assert.assertEquals(
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value-2")),
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value-2"))));
    }

    @Test
    public void testHashcode() throws Exception {

        testAtomspaceStorage(as ->
                Assert.assertEquals(
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value-2")).hashCode(),
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value-2")).hashCode()));
    }

    @Test
    public void testSame() throws Exception {

        testAtomspaceStorage(as ->
                Assert.assertSame(
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value-2")),
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value-2"))));
    }

    @Test
    public void testToString() throws Exception {

        testAtomspaceStorage(as ->
                Assert.assertEquals(
                        "Link(Node1('value1'),Node2('value2'))",
                        as.get("Link",
                                as.get("Node1", "value1"),
                                as.get("Node2", "value2")).toString()));
    }
}
