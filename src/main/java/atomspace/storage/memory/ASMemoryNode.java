package atomspace.storage.memory;

import atomspace.storage.ASNode;

public class ASMemoryNode extends ASMemoryAtom implements ASNode {

    private final String value;

    public ASMemoryNode(long id, String type, String value) {
        super(id, type);
        this.value = value;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return String.format("%s('%s')", type, value);
    }
}
