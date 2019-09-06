package atomspace.storage.memory.query.basic;

import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASOutgoingList;
import atomspace.storage.memory.query.ASQueryEngine;
import atomspace.storage.ASAtom;

import java.util.Iterator;

public class ASBasicQueryEngine implements ASQueryEngine {

    public static final String TYPE_NODE_VARIABLE = "VariableNode";

    @Override
    public Iterator<ASAtom> query(ASAtom atom) {

        ASBasicQueryTreeNode root = new ASBasicQueryTreeNode(null, atom, -2);

        return null;
    }
}
