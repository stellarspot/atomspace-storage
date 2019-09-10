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

    public static <K, V> void assertIteratorOfMapsEqual(Iterator<Map<K, V>> iter, KeyWithValue<K, V>[]... elements) {

        Set<Map<K, V>> actual = new HashSet<>();

        while (iter.hasNext()) {
            actual.add(iter.next());
        }

        Set<Map<K, V>> expected = new HashSet<>();

        for (KeyWithValue<K, V>[] kvs : elements) {
            Map<K, V> map = new HashMap<>();
            for (KeyWithValue<K, V> kv : kvs) {
                map.put(kv.key, kv.value);
            }
            expected.add(map);
        }

        Assert.assertEquals(expected, actual);
    }

    public static class KeyWithValue<K, V> {

        public final K key;
        public final V value;

        public KeyWithValue(K key, V value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o instanceof KeyWithValue) {
                KeyWithValue<K, V> that = (KeyWithValue<K, V>) o;
                return Objects.equals(key, that.key) &&
                        Objects.equals(value, that.value);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value);
        }
    }
}
