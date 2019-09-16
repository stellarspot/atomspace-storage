package atomspace.storage.performance.tree;

import atomspace.query.ASQueryEngine;
import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;
import atomspace.storage.performance.AtomspaceStoragePerformanceUtils;
import atomspace.storage.performance.PerformanceModel;
import atomspace.storage.performance.PerformanceModelConfiguration;
import atomspace.storage.performance.PerformanceModelParameters;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 1, batchSize = 1)
@Measurement(iterations = 1, batchSize = 3)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class RandomTreeQueryAtomsBenchmark {


    private static final PerformanceModelConfiguration config = RandomTreeBenchmarkConstants.DEFAULT_CONFIG;
    private static final int averageWidth = RandomTreeBenchmarkConstants.DEFAULT_AVERAGE_WIDTH;
    private static final int averageDepth = RandomTreeBenchmarkConstants.DEFAULT_AVERAGE_DEPTH;
    private static final int averageVariables = RandomTreeBenchmarkConstants.DEFAULT_AVERAGE_VARIABLES;
    private static final int statements = 10_000;

    @Param({"2500", "5000", "7500", "10000"})
    int queries = -1;

    PerformanceModel model;
    ASQueryEngine queryEngine;
    AtomspaceMemoryStorage atomspaceMemory;
    AtomSpaceNeo4jStorage atomspaceNeo4j;

    @Setup
    public void setUp() {
        PerformanceModelParameters params = new PerformanceModelParameters(statements, queries);
        model = new RandomTreePerformanceModel(config, params, averageWidth, averageDepth, averageVariables);
        queryEngine = new ASBasicQueryEngine();
        atomspaceMemory = AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        atomspaceNeo4j = AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
    }

//    @Benchmark
    public void queryMemory() throws Exception {
        model.queryAtoms(atomspaceMemory, queryEngine);
    }

//    @Benchmark
    public void queryNeo4j() throws Exception {
        model.queryAtoms(atomspaceNeo4j, queryEngine);
    }
}
