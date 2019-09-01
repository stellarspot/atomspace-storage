package atomspace.storage;

public class ASNode extends ASAtom {

    public final String value;

    public ASNode(long id, String type, String value) {
        super(id, type);
        this.value = value;
    }

    @Override
    public String toString() {
        return String.format("%s('%s')", type, value);
    }
}
