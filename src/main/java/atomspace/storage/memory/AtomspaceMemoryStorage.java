package atomspace.storage.memory;

import atomspace.storage.ASNode;
import atomspace.storage.AtomspaceStorage;

import java.util.HashMap;
import java.util.Map;

public class AtomspaceMemoryStorage implements AtomspaceStorage {

    private static long UNIQUE_ID = 0;
    private final Map<String, Long> nodeIds = new HashMap<>();
    private final Map<Long, ASNode> nodes = new HashMap<>();

    @Override
    public ASNode getOrCreateNode(String type, String value) {
        String key = String.format("%s:%s", type, value);
        Long id = nodeIds.computeIfAbsent(key, (k) -> {
            Long i = nextId();
            ASNode node = new ASNode(i, type, value);
            nodeIds.put(k, i);
            nodes.put(i, node);
            return i;
        });
        return nodes.get(id);
    }

    private long nextId() {
        return UNIQUE_ID++;
    }
}
