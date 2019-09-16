package atomspace.storage.performance.tree;

import org.openjdk.jmh.annotations.*;

@Fork(1)
@Warmup(iterations = 1, batchSize = 1)
@Measurement(iterations = 1, batchSize = 3)
@BenchmarkMode(Mode.SingleShotTime)
@State(Scope.Benchmark)
public class RandomTreeTest {

    @Benchmark
    public void sampleTest() {
    }
}
