package atomspace.performance.tree;

import atomspace.performance.PerformanceModelParameters;
import atomspace.performance.utils.AtomspaceStoragePerformanceUtils;
import atomspace.query.ASQueryEngine;
import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.neo4j.AtomSpaceNeo4jStorage;
import atomspace.performance.PerformanceModel;
import atomspace.performance.PerformanceModelConfiguration;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 1, batchSize = 1)
@Measurement(iterations = 1, batchSize = 3)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class RandomTreeQueryAtomsBenchmark {


    private static final PerformanceModelConfiguration config = RandomTreeDefaultConstants.DEFAULT_CONFIG;
    private static final RandomTreeModelParameters treeParams = RandomTreeDefaultConstants.DEFAULT_TREE_PARAMETERS;
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
        model = new RandomTreeModel(config, params, treeParams);
        queryEngine = new ASBasicQueryEngine();

        atomspaceMemory = AtomspaceStoragePerformanceUtils.getCleanMemoryStorage();
        atomspaceNeo4j = AtomspaceStoragePerformanceUtils.getCleanNeo4jStorage();
        atomspaceJanusGraph = AtomspaceStoragePerformanceUtils.getCleanJanusGraphStorage();

        model.createAtoms(atomspaceMemory);
        model.createAtoms(atomspaceNeo4j);
        model.createAtoms(atomspaceJanusGraph);
    }

    @Benchmark
    public void query1Memory() throws Exception {
        model.queryAtoms(atomspaceMemory, queryEngine);
    }

    @Benchmark
    public void query1Neo4j() throws Exception {
        model.queryAtoms(atomspaceNeo4j, queryEngine);
    }

    @Benchmark
    public void query3JanusGraph() throws Exception {
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
