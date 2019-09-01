package atomspace.storage;

public interface AtomspaceStorage {

    ASAtom get(String type, String value);

    ASAtom get(String type, ASAtom... atoms);
}
