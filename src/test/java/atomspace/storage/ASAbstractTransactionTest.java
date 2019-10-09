package atomspace.storage;

import org.junit.Assert;
import org.junit.Test;

import java.util.Iterator;

public abstract class ASAbstractTransactionTest extends ASAbstractTest {

    @Test
    public void testTransaction() throws Exception {

        AtomspaceStorage storage = getStorage();

        try (ASTransaction tx = storage.getTx()) {
            Iterator<ASAtom> iter = tx.getAtoms();
            Assert.assertFalse(iter.hasNext());
        }

        try (ASTransaction tx = storage.getTx()) {
            tx.get("Node", "value");
            Iterator<ASAtom> iter = tx.getAtoms();
            Assert.assertTrue(iter.hasNext());
            ASAtom atom = iter.next();
            Assert.assertEquals("Node", atom.getType());
            Assert.assertFalse(iter.hasNext());
        }

        try (ASTransaction tx = storage.getTx()) {
            tx.get("Node", "value");
            Iterator<ASAtom> iter = tx.getAtoms();
            Assert.assertTrue(iter.hasNext());
            ASAtom atom = iter.next();
            Assert.assertEquals("Node", atom.getType());
            Assert.assertFalse(iter.hasNext());
        }
    }
}
