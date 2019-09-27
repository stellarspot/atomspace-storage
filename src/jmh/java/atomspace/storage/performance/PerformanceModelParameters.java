package atomspace.storage.performance;

public class PerformanceModelParameters {
    public final int statements;
    public final int queries;
    public final int iterationsBeforeCommit;


    public PerformanceModelParameters(int statements, int queries) {
        this(statements, queries, 1);
    }

    public PerformanceModelParameters(int statements, int queries, int iterationsBeforeCommit) {
        this.statements = statements;
        this.queries = queries;
        this.iterationsBeforeCommit = iterationsBeforeCommit;
    }
}
