package atomspace.storage.performance;

import atomspace.storage.AtomspaceStorage;

public interface PerformanceModel {

    void createAtoms(AtomspaceStorage atomspace) throws Exception;

    void queryAtoms(AtomspaceStorage atomspace) throws Exception;
}