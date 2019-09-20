package atomspace;

import atomspace.query.ASQueryEngine.ASQueryResult;
import atomspace.storage.ASAtom;
import org.junit.Assert;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;

public class ASTestUtils {

    public static <T> void assertIteratorEquals(Iterator<T> iter, T... elems) {

        List<T> expected = Arrays.asList(elems);
        List<T> actual = new ArrayList<>();

        while (iter.hasNext()) {
            actual.add(iter.next());
        }


        Assert.assertEquals(expected, actual);
    }

    public static void assertQueryResultsEqual(Iterator<ASQueryResult> iter, KeyWithValue<String, ASAtom>[]... elements) {
        assertIteratorOfMapsEqual(map(iter, res -> res.getVariables()), elements);
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

    public static <T> int count(Iterator<T> iter) {

        int count = 0;
        while (iter.hasNext()) {
            iter.next();
            count++;
        }

        return count;
    }

    public static <T, R> Iterator<R> map(Iterator<T> iter, Function<T, R> mapper) {
        return new Iterator<R>() {
            @Override
            public boolean hasNext() {
                return iter.hasNext();
            }

            @Override
            public R next() {
                return mapper.apply(iter.next());
            }
        };
    }

    public static void removeDirectory(String directory) {
        Path pathToBeDeleted = Paths.get(directory);

        if (!Files.exists(pathToBeDeleted)) {
            return;
        }

        try {
            Files.walk(pathToBeDeleted)
                    .sorted(Comparator.reverseOrder())
                    .map(Path::toFile)
                    .forEach(File::delete);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
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
