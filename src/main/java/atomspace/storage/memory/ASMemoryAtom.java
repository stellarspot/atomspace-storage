package atomspace.storage.memory;

import atomspace.storage.ASAtom;

public abstract class ASMemoryAtom implements ASAtom {

    public final long id;
    public final String type;

    public ASMemoryAtom(long id, String type) {
        this.id = id;
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ASMemoryAtom) {
            ASMemoryAtom that = (ASMemoryAtom) obj;
            return this.id == that.id;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(id);
    }
}
