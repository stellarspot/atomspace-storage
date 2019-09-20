package atomspace.query;

import atomspace.storage.ASAtom;

import java.util.Iterator;
import java.util.Map;
import java.util.function.Function;

public interface ASQueryEngine {

    <T> Iterator<T> match(ASAtom atom, Function<ASQueryResult, T> mapper);

    default Iterator<ASQueryResult> match(ASAtom atom) {
        return match(atom, Function.identity());
    }

    interface ASQueryResult {

        ASAtom getAtom();

        Map<String, ASAtom> getVariables();
    }
}
