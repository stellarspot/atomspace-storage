package atomspace.storage;

public class ASLink extends ASAtom {
    public final ASAtom[] atoms;

    public ASLink(long id, String type, ASAtom... atoms) {
        super(id, type);
        this.atoms = atoms;
    }

    @Override
    public String toString() {

        StringBuilder builder = new StringBuilder();
        builder.append(type).append("(");

        boolean first = true;
        for (ASAtom atom : atoms) {
            if (first) {
                first = false;
            } else {
                builder.append(",");
            }
            builder.append(atom);
        }

        builder.append(")");
        return builder.toString();
    }
}
