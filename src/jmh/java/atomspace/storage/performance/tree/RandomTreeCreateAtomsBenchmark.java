package atomspace.storage.performance.tree;

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
public class RandomTreeCreateAtomsBenchmark {


    private static final PerformanceModelConfiguration config = new PerformanceModelConfiguration(5, 5, 5);
    private static final int averageWidth = 5;
    private static final int averageDepth = 5;

    @Param({"250", "500", "750", "1000"})
    int statements = -1;

    PerformanceModel model;
    AtomspaceMemoryStorage atomspaceMemory;
    AtomSpaceNeo4jStorage atomspaceNeo4j;

    @Setup
    public void setUp() {
        PerformanceModelParameters params = new PerformanceModelParameters(statements, -1);
        model = new RandomTreePerformanceModel(config, params, averageWidth, averageDepth);
        atomspaceMemory = AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        atomspaceNeo4j = AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
    }

    @Benchmark
    public void createMemory() throws Exception {
        model.createAtoms(atomspaceMemory);
    }

    @Benchmark
    public void createNeo4j() throws Exception {
        model.createAtoms(atomspaceNeo4j);
    }
}
