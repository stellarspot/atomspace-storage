package atomspace;

import org.junit.Assert;

import java.util.Iterator;

public class ASTestUtils {

    public static <T> void assertIteratorEquals(Iterator<T> iter, T... elems) {

        for (int i = 0; i < elems.length; i++) {
            Assert.assertTrue(iter.hasNext());
            Assert.assertEquals(elems[i], iter.next());
        }

        Assert.assertFalse(iter.hasNext());
    }
}
