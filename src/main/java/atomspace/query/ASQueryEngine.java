package atomspace.query;

import atomspace.storage.ASAtom;

import java.util.Iterator;
import java.util.Map;

public interface ASQueryEngine {

    Iterator<Map<String, ASAtom>> match(ASAtom atom);
}
