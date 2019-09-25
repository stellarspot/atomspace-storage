package atomspace.storage.util;

public interface AtomspaceStorageHelper {

    void dump();

    void reset();

    default void printStatistics(String msg) {
        System.out.printf("Print statistics is not implemented!%n");
    }
}
