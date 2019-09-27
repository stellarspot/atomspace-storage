package atomspace.performance.tree;

import atomspace.performance.PerformanceModelConfiguration;

public interface RandomTreeDefaultConstants {

    PerformanceModelConfiguration DEFAULT_CONFIG = new PerformanceModelConfiguration(5, 5, 5, false);
    RandomTreeModelParameters DEFAULT_TREE_PARAMETERS = new RandomTreeModelParameters(5, 5, 2);
}
