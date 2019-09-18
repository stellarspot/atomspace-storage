package atomspace.storage;

public abstract class ASAbstractTest {

    protected abstract AtomspaceStorage getStorage();

    protected void testAtomspaceStorage(AtomspaceStorageTest test) throws Exception {

        AtomspaceStorage storage = getStorage();
        try (AtomspaceStorageTransaction tx = storage.getTx()) {

            test.run(tx);
            tx.commit();
        }
    }

    protected interface AtomspaceStorageTest {

        void run(AtomspaceStorageTransaction tx) throws Exception;
    }
}
