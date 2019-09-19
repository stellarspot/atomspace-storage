package atomspace.storage.performance.tree;

import atomspace.query.ASQueryEngine;
import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;
import atomspace.storage.performance.utils.AtomspaceStoragePerformanceUtils;
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
    private static final int statements = 300;

    @Param({"100", "150", "200", "250", "300"})
    int queries = -1;

    PerformanceModel model;
    ASQueryEngine queryEngine;
    AtomspaceMemoryStorage atomspaceMemory;
    AtomSpaceNeo4jStorage atomspaceNeo4j;
    AtomspaceJanusGraphStorage atomspaceJanusGraph;

    @Setup
    public void setUp() throws Exception {
        PerformanceModelParameters params = new PerformanceModelParameters(statements, queries);
        model = new RandomTreePerformanceModel(config, params, averageWidth, averageDepth, averageVariables);
        queryEngine = new ASBasicQueryEngine();

        atomspaceMemory = AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        atomspaceNeo4j = AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
        atomspaceJanusGraph = AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage();

        model.createAtoms(atomspaceMemory);
        model.createAtoms(atomspaceNeo4j);
        model.createAtoms(atomspaceJanusGraph);
    }

    @Benchmark
    public void queryMemory() throws Exception {
        model.queryAtoms(atomspaceMemory, queryEngine);
    }

    @Benchmark
    public void queryNeo4j() throws Exception {
        model.queryAtoms(atomspaceNeo4j, queryEngine);
    }

    @Benchmark
    public void queryJanusGraph() throws Exception {
        model.queryAtoms(atomspaceJanusGraph, queryEngine);
    }

    @TearDown
    public void tearDown() throws Exception {
        atomspaceNeo4j.close();
        atomspaceJanusGraph.close();
    }

    public static void main(String[] args) throws Exception {

        AtomspaceStoragePerformanceUtils.runJMHTest(RandomTreeQueryAtomsBenchmark.class, "queries", true);
    }
}
