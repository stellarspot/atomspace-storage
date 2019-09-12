package atomspace.storage;

public interface AtomspaceStorage extends AutoCloseable {

    AtomspaceStorageTransaction getTx();
}
