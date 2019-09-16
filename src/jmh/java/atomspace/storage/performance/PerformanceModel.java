package atomspace.storage.performance;

import atomspace.storage.AtomspaceStorage;

public interface PerformanceModel {

    void createAtoms(AtomspaceStorage atomspace);

    void queryAtoms(AtomspaceStorage atomspace);
}