package atomspace.storage.performance.tree;

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
@Warmup(iterations = 0, batchSize = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class RandomTreeCreateAtomsBenchmark {


    private static final PerformanceModelConfiguration config = RandomTreeDefaultConstants.DEFAULT_CONFIG;
    private static final RandomTreeModelParameters treeParams = RandomTreeDefaultConstants.DEFAULT_TREE_PARAMETERS;

    @Param({"100", "200", "300", "400", "500"})
    int statements = -1;

    PerformanceModel model;
    AtomspaceMemoryStorage atomspaceMemory;
    AtomSpaceNeo4jStorage atomspaceNeo4j;
    AtomspaceJanusGraphStorage janusGraphStorage;

    @Setup
    public void setUp() {
        PerformanceModelParameters params = new PerformanceModelParameters(statements, -1);
        model = new RandomTreeModel(config, params, new RandomTreeModelParameters(treeParams.maxWidth, treeParams.maxWidth, 1));
        atomspaceMemory = AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        atomspaceNeo4j = AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
        janusGraphStorage = AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage();
    }

    @Benchmark
    public void create1Memory() throws Exception {
        model.createAtoms(atomspaceMemory);
    }

    @Benchmark
    public void create2Neo4j() throws Exception {
        model.createAtoms(atomspaceNeo4j);
    }

    @Benchmark
    public void create3JanusGraph() throws Exception {
        model.createAtoms(janusGraphStorage);
    }

    @TearDown
    public void tearDown() throws Exception {
        atomspaceNeo4j.close();
        janusGraphStorage.close();
    }

    public static void main(String[] args) throws Exception {

        AtomspaceStoragePerformanceUtils.runJMHTest(RandomTreeCreateAtomsBenchmark.class, "statements", true);
    }
}
