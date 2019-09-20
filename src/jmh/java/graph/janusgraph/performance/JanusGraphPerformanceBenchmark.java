package graph.janusgraph.performance;

import atomspace.storage.performance.utils.AtomspaceStoragePerformanceUtils;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversal;
import org.apache.tinkerpop.gremlin.structure.Vertex;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphTransaction;
import org.janusgraph.core.JanusGraphVertex;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import java.util.concurrent.TimeUnit;

@Fork(1)
@Warmup(iterations = 1, batchSize = 1)
@Measurement(iterations = 1, batchSize = 3)
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)

public class JanusGraphPerformanceBenchmark {

    @Param({"100", "200", "300", "400", "500"})
    int param = -1;

    private JanusGraph graph;

    @Setup
    public void setUp() {
        graph = JanusGraphPerformanceUtils.getJanusGraph();
        System.out.printf("vertices before: %d%n", JanusGraphPerformanceUtils.getVerticesCount(graph));
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
    public void upsertVertices(Blackhole blackhole) {

        String label = JanusGraphPerformanceUtils.getLabel();
        String key = JanusGraphPerformanceUtils.getKey();

        try (JanusGraphTransaction tx = graph.newTransaction()) {
            for (int i = 0; i < param; i++) {

                String value = JanusGraphPerformanceUtils.getValue(i);

                GraphTraversal<Vertex, Vertex> iter = tx.traversal().V()
                        .hasLabel(label)
                        .has(key, value);

                if (!iter.hasNext()) {
                    JanusGraphVertex v = tx.addVertex(label);
                    v.property(key, value);
                    blackhole.consume(v);
                }
            }

            tx.commit();
        }
    }

    @Benchmark
    public void upsertHalfVertices(Blackhole blackhole) {

        String label = JanusGraphPerformanceUtils.getLabel();
        String key = JanusGraphPerformanceUtils.getKey();

        try (JanusGraphTransaction tx = graph.newTransaction()) {
            for (int i = 0; i < param; i++) {

                String value = JanusGraphPerformanceUtils.getValue((i << 1) % param);

                GraphTraversal<Vertex, Vertex> iter = tx.traversal().V()
                        .hasLabel(label)
                        .has(key, value);

                if (!iter.hasNext()) {
                    JanusGraphVertex v = tx.addVertex(label);
                    v.property(key, value);
                    blackhole.consume(v);
                }
            }

            tx.commit();
        }
    }


    @TearDown
    public void tearDown() {
        System.out.printf("vertices after: %d%n", JanusGraphPerformanceUtils.getVerticesCount(graph));
        graph.close();
    }

    public static void main(String[] args) throws Exception {

        AtomspaceStoragePerformanceUtils.runJMHTest(JanusGraphPerformanceBenchmark.class, "param", true);
    }

}
