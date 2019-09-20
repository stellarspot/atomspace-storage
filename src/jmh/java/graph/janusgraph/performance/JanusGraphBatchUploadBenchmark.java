package graph.janusgraph.performance;

import atomspace.storage.performance.utils.AtomspaceStoragePerformanceUtils;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;
import org.janusgraph.core.TransactionBuilder;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 0, batchSize = 0)
@Measurement(iterations = 1, batchSize = 1)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
public class JanusGraphBatchUploadBenchmark {

    @Param({"10", "20", "30", "40", "50"})
    int param = -1;

    private JanusGraph graph;

    @Setup
    public void setUp() {
        graph = JanusGraphPerformanceUtils.getJanusGraph();
        JanusGraphPerformanceUtils.assertVerticesCount(graph, "vertices before", 0);
    }

    @Benchmark
    public void addVertices(Blackhole blackhole) {

        String label = JanusGraphPerformanceUtils.getLabel();
        String key = JanusGraphPerformanceUtils.getKey();

        try (JanusGraphTransaction tx = graph.newTransaction()) {
            for (int i = 0; i < param; i++) {
                JanusGraphVertex v = tx.addVertex(label);
                v.property(key, JanusGraphPerformanceUtils.getValue(i));
                blackhole.consume(v);
            }

            tx.commit();
        }
    }

    @Benchmark
    public void addVerticesBatch(Blackhole blackhole) {

        String label = JanusGraphPerformanceUtils.getLabel();
        String key = JanusGraphPerformanceUtils.getKey();

        TransactionBuilder builder = graph
                .buildTransaction()
                .enableBatchLoading()
                .consistencyChecks(false);

        try (JanusGraphTransaction tx = builder.start()) {
            for (int i = 0; i < param; i++) {
                String value = JanusGraphPerformanceUtils.getValue(i);
                JanusGraphVertex v = tx.addVertex(label);
                v.property(key, value);
                blackhole.consume(v);
            }

            tx.commit();
        }
    }

    @TearDown
    public void tearDown() {
        try {
            JanusGraphPerformanceUtils.assertVerticesCount(graph, "vertices after", param);
        } finally {
            graph.close();
        }
    }

    public static void main(String[] args) throws Exception {

        AtomspaceStoragePerformanceUtils.runJMHTest(JanusGraphBatchUploadBenchmark.class, "param", true);
    }
}