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

    public static String convertIdsToString(ASAtom... atoms) {

        switch (atoms.length) {
            case 0:
                return "";
            case 1:
                return Long.toString(atoms[0].getId());
            default: {

                StringBuilder builder = new StringBuilder();
                for (ASAtom atom : atoms) {
                    builder.append(atom.getId()).append(':');
                }
                return builder.toString();
            }
        }
    }
}
