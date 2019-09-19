package atomspace.storage.performance.tree;

import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;
import atomspace.storage.performance.AtomspaceStoragePerformanceUtils;
import atomspace.storage.performance.PerformanceModel;
import atomspace.storage.performance.PerformanceModelConfiguration;
import atomspace.storage.performance.PerformanceModelParameters;
import atomspace.storage.performance.result.PerformanceResult;
import atomspace.storage.performance.result.PerformanceResultPlotter;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 1, batchSize = 1)
@Measurement(iterations = 1, batchSize = 3)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class RandomTreeCreateAtomsBenchmark {


    private static final PerformanceModelConfiguration config = RandomTreeBenchmarkConstants.DEFAULT_CONFIG;
    private static final int averageWidth = RandomTreeBenchmarkConstants.DEFAULT_AVERAGE_WIDTH;
    private static final int averageDepth = RandomTreeBenchmarkConstants.DEFAULT_AVERAGE_DEPTH;

    @Param({"250", "500", "750", "1000"})
    int statements = -1;

    PerformanceModel model;
    AtomspaceMemoryStorage atomspaceMemory;
    AtomSpaceNeo4jStorage atomspaceNeo4j;
    AtomspaceJanusGraphStorage janusGraphStorage;

    @Setup
    public void setUp() {
        PerformanceModelParameters params = new PerformanceModelParameters(statements, -1);
        model = new RandomTreePerformanceModel(config, params, averageWidth, averageDepth, -1);
        atomspaceMemory = AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        atomspaceNeo4j = AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
//        janusGraphStorage = AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage();
    }

    @Benchmark
    public void createMemory() throws Exception {
        model.createAtoms(atomspaceMemory);
    }

    @Benchmark
    public void createNeo4j() throws Exception {
        model.createAtoms(atomspaceNeo4j);
    }

//    @Benchmark
//    public void createJanusGraph() throws Exception {
//        model.createAtoms(janusGraphStorage);
//    }

    public static void main(String[] args) throws Exception {

        Map<String, List<PerformanceResult.ParamWithTime>> measurements =
                PerformanceResult.runJMHTest(RandomTreeCreateAtomsBenchmark.class, "statements");

        PerformanceResultPlotter.PlotterProperties props = new PerformanceResultPlotter.PlotterProperties();
        props.title = "Create";
        props.timeUnits = "ms";
        props.sameChart = false;
        props.measurements = measurements;

        PerformanceResultPlotter.showMeasurements(props);

    }
}
