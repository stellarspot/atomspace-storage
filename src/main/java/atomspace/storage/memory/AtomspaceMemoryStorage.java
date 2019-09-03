package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.AtomspaceStorage;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class AtomspaceMemoryStorage implements AtomspaceStorage {

    private static long UNIQUE_ID = 0;
    private final Map<String, Long> ids = new HashMap<>();
    private final Map<Long, ASAtom> atoms = new HashMap<>();

    @Override
    public ASAtom get(String type, String value) {

        String key = getNodeKey(type, value);
        Long id = ids.get(key);

        if (id == null) {
            id = nextId();
            ASNode node = new ASNode(id, type, value);
            this.ids.put(key, id);
            this.atoms.put(id, node);
            return node;
        }

        return this.atoms.get(id);
    }


    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        String key = getListKey(type, atoms);
        Long id = ids.get(key);

        if (id == null) {
            id = nextId();
            ASLink link = new ASLink(id, type, atoms);
            this.ids.put(key, id);
            this.atoms.put(id, link);
            return link;
        }

        return this.atoms.get(id);
    }

    @Override
    public Iterator<ASAtom> getAtoms() {
        return atoms.values().iterator();
    }

    private long nextId() {
        return UNIQUE_ID++;
    }

    private static String getNodeKey(String type, String value) {
        return String.format("%s:%s", type, value);
    }

    private static String getListKey(String type, ASAtom... atoms) {
        StringBuilder b = new StringBuilder();
        b.append(type);
        Arrays.stream(atoms).forEach(atom -> b.append(":").append(atom.id));
        return b.toString();
    }
}
