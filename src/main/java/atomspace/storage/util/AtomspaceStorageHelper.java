package atomspace.storage.util;

import atomspace.storage.ASTransaction;

public interface AtomspaceStorageHelper {

    void dump(ASTransaction tx);

    void reset(ASTransaction tx);

    default void printStatistics(ASTransaction tx, String msg) {
        System.out.printf("Print statistics is not implemented!%n");
    }
}
