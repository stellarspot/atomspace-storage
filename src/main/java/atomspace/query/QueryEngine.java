package atomspace.query;

import atomspace.storage.ASAtom;

import java.util.Iterator;

public interface QueryEngine {

    Iterator<ASAtom> query(ASAtom atom);
}
