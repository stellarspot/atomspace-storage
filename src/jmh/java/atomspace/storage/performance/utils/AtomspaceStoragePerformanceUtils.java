package atomspace.storage.performance.utils;

import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;
import atomspace.storage.neo4j.AtomspaceNeo4jStorageHelper;
import atomspace.storage.neo4j.AtomspaceNeo4jStorageTransaction;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.util.*;

import static atomspace.storage.performance.utils.PerformanceResultPlotter.PointDouble;


public class AtomspaceStoragePerformanceUtils {

    private static final String STORAGE_DIR = "/tmp/atomspace-storage/perf";

    public static AtomspaceMemoryStorage getCleanMemoryStorage() {
        return new AtomspaceMemoryStorage();
    }

    public static AtomSpaceNeo4jStorage getCleanNeo4jStorage() {

        String storageDir = String.format("%s/neo4j", STORAGE_DIR);
        AtomSpaceNeo4jStorage storage = new AtomSpaceNeo4jStorage(storageDir);

        try (AtomspaceNeo4jStorageTransaction tx = storage.getTx()) {
            AtomspaceNeo4jStorageHelper helper = new AtomspaceNeo4jStorageHelper(tx);
            helper.reset();
        }

        return storage;
    }

    public static AtomspaceJanusGraphStorage getCleanJanusGraphStorage() {

        String storageDir = String.format("%s/janusgraph", STORAGE_DIR);
        AtomspaceJanusGraphStorage storage = new AtomspaceJanusGraphStorage(storageDir);

        try (AtomspaceJanusGraphStorageTransaction tx = storage.getTx()) {
            AtomspaceJanusGraphStorageHelper helper = new AtomspaceJanusGraphStorageHelper(tx);
            helper.reset();
        }

        return storage;
    }

    public static void runJMHTest(Class cls, String paramName, boolean drawChart) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(cls.getSimpleName())
                .build();

        Collection<RunResult> results = new Runner(opt).run();

        if (drawChart) {
            showJMHResults(paramName, results);
        }
    }

    public static void showJMHResults(String paramName, Collection<RunResult> results) {

        final Map<String, List<PointDouble>> measurements = new HashMap<>();

        for (RunResult result : results) {

            Result r = result.getPrimaryResult();
            String label = r.getLabel();
            String param = result.getParams().getParam(paramName);
            double score = r.getScore();

            List<PointDouble> list = measurements.computeIfAbsent(label, key -> new ArrayList<>());

            list.add(new PointDouble(Integer.parseInt(param), score));
        }

        PerformanceResultPlotter.PlotterProperties props = new PerformanceResultPlotter.PlotterProperties();

        props.timeUnits = "ms";
        props.measurements = measurements;

        PerformanceResultPlotter.showMeasurements(props);
    }
}
