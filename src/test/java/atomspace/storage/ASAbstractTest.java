package atomspace.storage;

public abstract class ASAbstractTest {

    protected abstract AtomspaceStorage getStorage();

    protected void testAtomspaceStorage(AtomspaceStorageTest test) throws Exception {

        try (AtomspaceStorageTransaction tx = getStorage().getTx()) {

            test.run(tx);
            tx.commit();
        }
    }

    interface AtomspaceStorageTest {

        void run(AtomspaceStorageTransaction tx) throws Exception;
    }
}
