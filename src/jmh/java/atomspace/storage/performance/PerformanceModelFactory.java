package atomspace.storage.performance;

public interface PerformanceModelFactory {

    PerformanceModel getModel(int atoms, int queries);
}
