package atomspace.performance.tree;

import atomspace.performance.PerformanceModelParameters;
import atomspace.performance.utils.AtomspaceStoragePerformanceUtils;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;
import atomspace.performance.PerformanceModel;
import atomspace.performance.PerformanceModelConfiguration;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorage;
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
    AtomspaceMemoryStorage memoryStorage;
    AtomspaceRelationalDBStorage relationalDBStorage;
    AtomSpaceNeo4jStorage neo4jStorage;
    AtomspaceJanusGraphStorage janusGraphStorage;

    @Setup
    public void setUp() {
        PerformanceModelParameters params = new PerformanceModelParameters(statements, -1);
        model = new RandomTreeModel(config, params, new RandomTreeModelParameters(treeParams.maxWidth, treeParams.maxWidth, 1));
        memoryStorage = AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        relationalDBStorage = AtomspaceStoragePerformanceUtils.getCleanRelationalDBStorage();
        neo4jStorage = AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
        janusGraphStorage = AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage();
    }

    @Benchmark
    public void create1Memory() throws Exception {
        model.createAtoms(memoryStorage);
    }

    @Benchmark
    public void create2RelationDB() throws Exception {
        model.createAtoms(relationalDBStorage);
    }

    @Benchmark
    public void create3Neo4j() throws Exception {
        model.createAtoms(neo4jStorage);
    }

    @Benchmark
    public void create4JanusGraph() throws Exception {
        model.createAtoms(janusGraphStorage);
    }

    @TearDown
    public void tearDown() throws Exception {
        relationalDBStorage.close();
        neo4jStorage.close();
        janusGraphStorage.close();
    }

    public static void main(String[] args) throws Exception {

        AtomspaceStoragePerformanceUtils.runJMHTest(RandomTreeCreateAtomsBenchmark.class, "statements", true);
    }
}
