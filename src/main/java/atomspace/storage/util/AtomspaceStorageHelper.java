package atomspace.storage.util;

import atomspace.storage.ASTransaction;

public interface AtomspaceStorageHelper<Transaction extends ASTransaction> {

    void dump(Transaction tx);

    void reset(Transaction tx);

    default void printStatistics(Transaction tx, String msg) {
        System.out.printf("Print statistics is not implemented!%n");
    }
}
