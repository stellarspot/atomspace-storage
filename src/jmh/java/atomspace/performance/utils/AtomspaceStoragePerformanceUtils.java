package atomspace.performance.utils;

import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomspaceNeo4jStorage;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorage;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorageHelper;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

import static atomspace.performance.utils.PerformanceResultPlotter.PointDouble;


public class AtomspaceStoragePerformanceUtils {

    private static final String STORAGE_DIR = "/tmp/atomspace-storage/perf";

    public static AtomspaceMemoryStorage getCleanMemoryStorage() {
        return new AtomspaceMemoryStorage();
    }

    public static AtomspaceRelationalDBStorage getCleanRelationalDBStorage() {
        String storageDir = getCleanNormalizedTempDir("atomspace-storage-jmh-relationaldb");
        return AtomspaceRelationalDBStorageHelper.getInMemoryStorage(storageDir);
    }

    public static AtomspaceNeo4jStorage getCleanNeo4jStorage() {
        String storageDir = getCleanNormalizedTempDir("atomspace-storage-jmh-neo4j");
        return new AtomspaceNeo4jStorage(storageDir);
    }

    public static AtomspaceJanusGraphStorage getCleanJanusGraphStorage() {
        String storageDir = getCleanNormalizedTempDir("atomspace-storage-jmh-janusgraph");
        AtomspaceJanusGraphStorage storage = AtomspaceJanusGraphStorageHelper
                .getJanusGraphBerkeleyDBStorage(storageDir);

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
        props.sameChart = false;
        props.measurements = measurements;

        PerformanceResultPlotter.showMeasurements(props);
    }

    public static void waitForProfiler() {
        System.out.printf("Type any key when profiler is started.");
        Scanner in = new Scanner(System.in);
        String s = in.nextLine();
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

    public static String getTempDir(String prefix) {
        try {
            return Files.createTempDirectory(prefix).toAbsolutePath().toString();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String getCleanNormalizedTempDir(String prefix) {
        String path = getTempDir(prefix);
        removeDirectory(path);
        return path.replace('\\', '/');
    }

}
