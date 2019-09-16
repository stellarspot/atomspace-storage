package atomspace.storage.performance.tree;

import atomspace.storage.performance.PerformanceModelConfiguration;

public interface RandomTreeBenchmarkConstants {

    int DEFAULT_AVERAGE_WIDTH = 5;
    int DEFAULT_AVERAGE_DEPTH = 5;
    int DEFAULT_AVERAGE_VARIABLES = 2;
    PerformanceModelConfiguration DEFAULT_CONFIG = new PerformanceModelConfiguration(5, 5, 5);
}
