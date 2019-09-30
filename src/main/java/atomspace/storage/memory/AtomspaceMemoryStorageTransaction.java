package atomspace.storage.memory;

import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.AtomspaceStorageTransaction;

import java.util.*;

public class AtomspaceMemoryStorageTransaction implements AtomspaceStorageTransaction {

    private final AtomspaceMemoryStorage storage;

    public AtomspaceMemoryStorageTransaction(AtomspaceMemoryStorage storage) {
        this.storage = storage;
    }

    @Override
    public ASAtom get(String type, String value) {

        String key = getNodeKey(type, value);
        ASNode node = storage.nodesInverseIndex.get(key);

        if (node == null) {
            node = new ASMemoryNode(storage.nextId(), type, value);
            storage.nodesInverseIndex.put(key, node);
        }

        return node;
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        String key = getListKey(type, atoms);
        ASLink link = storage.linksInverseIndex.get(key);

        if (link == null) {
            link = new ASMemoryLink(storage.nextId(), type, atoms);
            storage.linksInverseIndex.put(key, link);

            int size = atoms.length;
            for (int i = 0; i < atoms.length; i++) {
                ((ASMemoryAtom.ASMemoryIncomingSet)atoms[i].getIncomingSet()).add(link, size, i);
            }
        }

        return link;
    }

    @Override
    public Iterator<ASAtom> getAtoms() {
        List<ASAtom> atoms = new ArrayList<>(storage.nodesInverseIndex.size() + storage.linksInverseIndex.size());
        atoms.addAll(storage.nodesInverseIndex.values());
        atoms.addAll(storage.linksInverseIndex.values());
        return atoms.iterator();
    }

    @Override
    public void commit() {
    }

    @Override
    public void close() {
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

    void reset() {
        storage.reset();
    }
}
