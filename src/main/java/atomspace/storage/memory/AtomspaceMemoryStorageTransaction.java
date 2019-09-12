package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.AtomspaceStorageTransaction;

import java.util.*;

public class AtomspaceMemoryStorageTransaction implements AtomspaceStorageTransaction {

    private static long UNIQUE_ID = 0;
    private final Map<String, ASNode> nodesInverseIndex = new HashMap<>();
    private final Map<String, ASLink> linksInverseIndex = new HashMap<>();

    @Override
    public ASAtom get(String type, String value) {

        String key = getNodeKey(type, value);
        ASNode node = nodesInverseIndex.get(key);

        if (node == null) {
            node = new ASMemoryNode(nextId(), type, value);
            this.nodesInverseIndex.put(key, node);
        }

        return node;
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        String key = getListKey(type, atoms);
        ASLink link = linksInverseIndex.get(key);

        if (link == null) {
            link = new ASMemoryLink(nextId(), type, atoms);
            this.linksInverseIndex.put(key, link);
        }

        return link;
    }

    @Override
    public Iterator<ASAtom> getAtoms() {
        List<ASAtom> atoms = new ArrayList<>(nodesInverseIndex.size() + linksInverseIndex.size());
        atoms.addAll(nodesInverseIndex.values());
        atoms.addAll(linksInverseIndex.values());
        return atoms.iterator();
    }

    @Override
    public void commit() {
    }

    @Override
    public void close() {
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
        Arrays.stream(atoms).forEach(atom -> b.append(":").append(atom.getId()));
        return b.toString();
    }

}
