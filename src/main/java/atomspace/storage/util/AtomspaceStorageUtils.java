package atomspace.storage.util;

import atomspace.storage.ASAtom;

public class AtomspaceStorageUtils {

    public static String getKey(String type, int arity, int position) {
        return String.format("%s_%d_%d", type, arity, position);
    }

    public static long[] getIds(ASAtom... atoms) {
        long[] ids = new long[atoms.length];

        for (int i = 0; i < atoms.length; i++) {
            ids[i] = atoms[i].getId();
        }
        return ids;
    }

    public static long[] getIds(String str) {
        String[] split = str.split(":");

        long[] ids = new long[split.length];

        for (int i = 0; i < split.length; i++) {
            ids[i] = Long.parseLong(split[i]);
        }
        return ids;
    }

    public static String idsToString(long... ids) {

        switch (ids.length) {
            case 0:
                return "";
            case 1:
                return Long.toString(ids[0]);
            default: {

                StringBuilder builder = new StringBuilder();
                for (long id : ids) {
                    builder.append(id).append(':');
                }
                return builder.toString();
            }
        }
    }
}
