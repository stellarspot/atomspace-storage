package atomspace.storage.performance.tree;

import atomspace.storage.performance.PerformanceModelConfiguration;

public interface RandomTreeDefaultConstants {

    PerformanceModelConfiguration DEFAULT_CONFIG = new PerformanceModelConfiguration(5, 5, 5, false);
    RandomTreeModelParameters DEFAULT_TREE_PARAMETERS = new RandomTreeModelParameters(5, 5, 2);
}
