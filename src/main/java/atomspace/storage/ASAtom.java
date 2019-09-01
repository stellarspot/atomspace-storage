package atomspace.storage;

public class ASAtom  {

    public final long id;
    public final String type;

    public ASAtom(long id, String type) {
        this.id = id;
        this.type = type;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ASAtom) {
            ASAtom that = (ASAtom) obj;
            return this.id == that.id;
        }

        return false;
    }
}
