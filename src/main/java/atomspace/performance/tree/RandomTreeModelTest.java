package atomspace.performance.tree;

import atomspace.performance.PerformanceModel;
import atomspace.performance.PerformanceModelConfiguration;
import atomspace.performance.PerformanceModelParameters;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.relationaldb.ASRelationalDBTransaction;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorage;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorageHelper;
import atomspace.storage.util.AtomspaceStorageUtils;

import java.util.*;

public class RandomTreeModelTest {

    public static void main(String[] args) throws Exception {

        StorageWrapper[] wrappers = {
                new MemoryStorageWrapper(),
                new RelationalDBStorageWrapper()
        };

        int[] statements = {10, 20};

        ModelRunner runner = new RandomTreeCreateModelRunner(3, 3, 2);

        List<Measurement> results = measure(runner, wrappers, statements);

        for (Measurement result : results) {
            System.out.printf("result: %s%n", result);
        }
    }

    public static List<Measurement> measure(ModelRunner runner, StorageWrapper[] wrappers, int[] xs) throws Exception {

        Map<String, List<Long>> map = new HashMap<>();

        for (int x : xs) {
            PerformanceModel model = runner.getModel(x);
            for (StorageWrapper wrapper : wrappers) {
                runner.init(model, wrapper);
                long time = System.currentTimeMillis();
                runner.run(model, wrapper);
                long elapsedTime = System.currentTimeMillis() - time;

                List<Long> ys = map.computeIfAbsent(wrapper.getName(), (key) -> new LinkedList<>());
                ys.add(elapsedTime);
            }
        }

        double[] xsd = Arrays.stream(xs).mapToDouble(x -> x).toArray();
        List<Measurement> measurements = new ArrayList<>(wrappers.length);

        for (Map.Entry<String, List<Long>> entry : map.entrySet()) {
            String name = entry.getKey();
            double[] ysd = entry.getValue().stream().mapToDouble(y -> y).toArray();
            Measurement measurement = new Measurement(name, xsd, ysd);
            measurements.add(measurement);
        }

        return measurements;
    }

    private static AtomspaceRelationalDBStorage getRelationalDBStorage() {
        String dir = "/tmp/atomspace-storage/performance/relationaldb";
        AtomspaceStorageUtils.removeDirectory(dir);
        return AtomspaceRelationalDBStorageHelper.getInMemoryStorage(dir);
    }

    static class Measurement {

        public final String name;
        public final double[] xs;
        public final double[] ys;

        public Measurement(String name, double[] xs, double[] ys) {
            this.name = name;
            this.xs = xs;
            this.ys = ys;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append(name);
            for (int i = 0; i < xs.length; i++) {
                builder
                        .append(" (")
                        .append(xs[i])
                        .append(", ")
                        .append(ys[i])
                        .append(")");
            }
            return builder.toString();
        }
    }

    interface StorageWrapper {

        String getName();

        AtomspaceStorage getStorage();

        void clean();

        default void close() throws Exception {
            getStorage().close();
        }
    }

    static class MemoryStorageWrapper implements StorageWrapper {

        @Override
        public String getName() {
            return "Memory";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return new AtomspaceMemoryStorage();
        }

        @Override
        public void clean() {
        }
    }

    static class RelationalDBStorageWrapper implements StorageWrapper {

        AtomspaceRelationalDBStorage storage = getRelationalDBStorage();

        @Override
        public String getName() {
            return "RelationalDB";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void clean() {
            try (ASRelationalDBTransaction tx = storage.getTx()) {
                AtomspaceRelationalDBStorageHelper helper = new AtomspaceRelationalDBStorageHelper(tx);
                helper.reset();
            }
        }
    }

    interface ModelRunner {

        PerformanceModel getModel(int param);

        void init(PerformanceModel model, StorageWrapper wrapper) throws Exception;

        void run(PerformanceModel model, StorageWrapper wrapper) throws Exception;
    }

    static class RandomTreeCreateModelRunner implements ModelRunner {

        final int randomTreeSize;
        final int maxTypes;
        final int maxVariables;

        public RandomTreeCreateModelRunner(int randomTreeSize, int maxTypes, int maxVariables) {
            this.randomTreeSize = randomTreeSize;
            this.maxTypes = maxTypes;
            this.maxVariables = maxVariables;
        }


        @Override
        public PerformanceModel getModel(int statements) {
            PerformanceModelConfiguration config = new PerformanceModelConfiguration(maxTypes, maxTypes, maxTypes, true);
            PerformanceModelParameters params = new PerformanceModelParameters(statements, -1, 100);
            RandomTreeModelParameters treeParams = new RandomTreeModelParameters(randomTreeSize, randomTreeSize, maxVariables);
            return new RandomTreeModel(config, params, treeParams);
        }

        @Override
        public void init(PerformanceModel model, StorageWrapper wrapper) {
            wrapper.clean();
        }

        @Override
        public void run(PerformanceModel model, StorageWrapper wrapper) throws Exception {
            model.createAtoms(wrapper.getStorage());
        }
    }
}

