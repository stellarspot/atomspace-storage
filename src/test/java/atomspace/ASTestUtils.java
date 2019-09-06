package atomspace;

import org.junit.Assert;

import java.util.*;

public class ASTestUtils {

    public static <T> void assertIteratorEquals(Iterator<T> iter, T... elems) {

        List<T> expected = Arrays.asList(elems);
        List<T> actual = new ArrayList<>();

        while (iter.hasNext()) {
            actual.add(iter.next());
        }


        Assert.assertEquals(expected, actual);
    }

    public static <K, V> void assertIteratorOfMapsEqual(Iterator<Map<K, V>> iter, Object[][]... variables) {

        Set<Map<K, V>> actual = new HashSet<>();

        while (iter.hasNext()) {
            actual.add(iter.next());
        }

        Set<Map<K, V>> expected = new HashSet<>();
        for (Object[][] variableMap : variables) {
            Map<K, V> map = new HashMap<>();
            for (Object[] variable : variableMap) {
                K key = (K) variable[0];
                V value = (V) variable[1];
                map.put(key, value);
            }
            expected.add(map);
        }

        Assert.assertEquals(expected, actual);
    }
}
