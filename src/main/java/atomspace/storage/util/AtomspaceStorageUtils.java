package atomspace.storage.util;

import atomspace.storage.ASAtom;

public class AtomspaceStorageUtils {

    public static long[] getIds(ASAtom... atoms) {
        long[] ids = new long[atoms.length];

        for (int i = 0; i < atoms.length; i++) {
            ids[i] = atoms[i].getId();
        }
        return ids;
    }
}
