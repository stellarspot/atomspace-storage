package atomspace.storage;

public interface AtomspaceStorage {

    ASNode getOrCreateNode(String type, String value);
}
