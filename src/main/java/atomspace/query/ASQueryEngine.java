package atomspace.query;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public interface ASQueryEngine {

    <T> Iterator<T> match(AtomspaceStorageTransaction tx, ASAtom query, Function<ASQueryResult, T> mapper);

    default Iterator<ASQueryResult> match(AtomspaceStorageTransaction tx, ASAtom query) {
        return match(tx, query, Function.identity());
    }

    interface ASQueryResult {

        ASAtom getAtom();

        Map<String, ASAtom> getVariables();
    }
}
