package atomspace.performance.tree;

import atomspace.performance.PerformanceModel;
import atomspace.performance.PerformanceModelConfiguration;
import atomspace.performance.PerformanceModelParameters;
import atomspace.performance.plotter.PerformanceResultPlotter;
import atomspace.performance.plotter.PerformanceResultPlotter.PointDouble;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.janusgraph.ASJanusGraphTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.memory.ASMemoryTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.memory.AtomspaceMemoryStorageHelper;
import atomspace.storage.neo4j.ASNeo4jTransaction;
import atomspace.storage.neo4j.AtomspaceNeo4jStorage;
import atomspace.storage.neo4j.AtomspaceNeo4jStorageHelper;
import atomspace.storage.relationaldb.ASRelationalDBTransaction;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorage;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorageHelper;
import atomspace.storage.util.AtomspaceStorageUtils;

import java.util.*;

import static atomspace.performance.plotter.PerformanceResultPlotter.PlotterProperties.toPointList;

public class RandomTreeModelTest {

    public static void main(String[] args) throws Exception {

        StorageWrapper[] wrappers = {
                new MemoryStorageWrapper(),
                new RelationalDBStorageWrapper(),
                new Neo4jStorageWrapper(),
                new JanusGraphStorageWrapper()
        };

        int[] statements = {100, 200, 300, 400, 500};

        ModelRunner runner = new RandomTreeCreateModelRunner(3, 3, 2);

        List<Measurement> results = measure(runner, wrappers, statements);

        for (Measurement result : results) {
            System.out.printf("result: %s%n", result);
        }

        showPlotter(results);
    }

    public static List<Measurement> measure(ModelRunner runner, StorageWrapper[] wrappers, int[] xs) throws Exception {

        Map<String, List<Long>> map = new HashMap<>();

        // warmup
        PerformanceModel model = runner.getModel(10);
        for (StorageWrapper wrapper : wrappers) {
            runner.init(model, wrapper);
            runner.run(model, wrapper);
        }

        // performance measurement
        for (int x : xs) {
            model = runner.getModel(x);
            System.out.printf("x: %d, %s%n", x, model);
            for (StorageWrapper wrapper : wrappers) {
                runner.init(model, wrapper);
                long time = System.currentTimeMillis();
                runner.run(model, wrapper);
                long elapsedTime = System.currentTimeMillis() - time;
                wrapper.printStatistics();

                List<Long> ys = map.computeIfAbsent(wrapper.getName(), (key) -> new LinkedList<>());
                ys.add(elapsedTime);
            }
        }

        for (StorageWrapper wrapper : wrappers) {
            wrapper.close();
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

    private static AtomspaceNeo4jStorage getNeo4jStorage() {
        String dir = "/tmp/atomspace-storage/performance/neo4j";
        AtomspaceStorageUtils.removeDirectory(dir);
        return new AtomspaceNeo4jStorage(dir);
    }

//    private static AtomspaceJanusGraphStorage getJanusGraphStorage() {
//        return AtomspaceJanusGraphStorageHelper.getJanusGraphInMemoryStorage();
//    }

    private static AtomspaceJanusGraphStorage getJanusGraphStorage() {
        String dir = "/tmp/atomspace-storage/performance/janusgraph";
        AtomspaceStorageUtils.removeDirectory(dir);
        return AtomspaceJanusGraphStorageHelper.getJanusGraphBerkeleyDBStorage(dir);
    }

    private static void showPlotter(List<Measurement> measurements) {
        Map<String, List<PointDouble>> map = new HashMap<>();
        for (Measurement measurement : measurements) {
            double[] xs = measurement.xs;
            double[] ys = measurement.ys;
            map.put(measurement.name, toPointList(xs, ys));
        }

        PerformanceResultPlotter.PlotterProperties properties = PerformanceResultPlotter.PlotterProperties.differentCharts(map);
        PerformanceResultPlotter.showMeasurements(properties);
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

        void printStatistics();

        default void close() throws Exception {
            getStorage().close();
        }
    }

    static class MemoryStorageWrapper implements StorageWrapper {

        AtomspaceMemoryStorage storage = new AtomspaceMemoryStorage();
        AtomspaceMemoryStorageHelper helper = new AtomspaceMemoryStorageHelper(storage);

        @Override
        public String getName() {
            return "create1Memory";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASMemoryTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "memory");
            }
        }

        @Override
        public void clean() {
            try (ASMemoryTransaction tx = storage.getTx()) {
                helper.reset(tx);
            }
        }
    }

    static class RelationalDBStorageWrapper implements StorageWrapper {

        AtomspaceRelationalDBStorage storage = getRelationalDBStorage();
        AtomspaceRelationalDBStorageHelper helper = new AtomspaceRelationalDBStorageHelper(storage);

        @Override
        public String getName() {
            return "create2RelationalDB";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASRelationalDBTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "relational db");
            }
        }

        @Override
        public void clean() {
            try (ASRelationalDBTransaction tx = storage.getTx()) {
                helper.reset(tx);
            }
        }
    }

    static class Neo4jStorageWrapper implements StorageWrapper {

        AtomspaceNeo4jStorage storage = getNeo4jStorage();
        AtomspaceNeo4jStorageHelper helper = new AtomspaceNeo4jStorageHelper(storage);

        @Override
        public String getName() {
            return "create3Neo4j";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASNeo4jTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "neo4j");
            }
        }

        @Override
        public void clean() {
            try (ASNeo4jTransaction tx = storage.getTx()) {
                helper.reset(tx);
            }
        }
    }

    static class JanusGraphStorageWrapper implements StorageWrapper {

        AtomspaceJanusGraphStorage storage = getJanusGraphStorage();
        AtomspaceJanusGraphStorageHelper helper = new AtomspaceJanusGraphStorageHelper(storage);

        @Override
        public String getName() {
            return "create4JanusGraph";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASJanusGraphTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "janusgraph");
                helper.dump(tx);
            }
        }

        @Override
        public void clean() {
            try (ASJanusGraphTransaction tx = storage.getTx()) {
                helper.reset(tx);
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

