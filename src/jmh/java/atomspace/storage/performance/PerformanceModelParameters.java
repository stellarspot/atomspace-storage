package atomspace.storage.performance;

public class PerformanceModelParameters {
    public final int statements;
    public final int queries;


    public PerformanceModelParameters(int statements, int queries) {
        this.statements = statements;
        this.queries = queries;
    }
}
