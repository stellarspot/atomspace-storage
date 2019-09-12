package atomspace.storage;

public interface ASLink extends ASAtom {

    ASOutgoingList getOutgoingList();

    default String toString(ASLink link) {

        StringBuilder builder = new StringBuilder();
        builder.append(link.getType()).append("(");

        ASOutgoingList outgoingList = link.getOutgoingList();
        for (int i = 0; i < outgoingList.getSize(); i++) {
            if (i != 0) {
                builder.append(",");
            }
            builder.append(outgoingList.getAtom(i));
        }

        builder.append(")");
        return builder.toString();
    }
}
