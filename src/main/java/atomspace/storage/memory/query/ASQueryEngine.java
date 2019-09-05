package atomspace.storage.memory.query;

import atomspace.storage.ASAtom;

import java.util.Iterator;

public interface ASQueryEngine {

    Iterator<ASAtom> query(ASAtom atom);
}
