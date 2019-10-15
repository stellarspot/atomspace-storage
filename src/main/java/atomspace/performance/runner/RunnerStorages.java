package atomspace.performance.runner;

import atomspace.storage.AtomspaceStorage;
import atomspace.storage.janusgraph.ASJanusGraphTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorage;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.memory.ASMemoryTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.memory.AtomspaceMemoryStorageHelper;
import atomspace.storage.neo4j.ASNeo4jTransaction;
import atomspace.storage.neo4j.AtomspaceNeo4jStorage;
import atomspace.storage.neo4j.AtomspaceNeo4jStorageHelper;
import atomspace.storage.relationaldb.ASRelationalDBTransaction;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorage;
import atomspace.storage.relationaldb.AtomspaceRelationalDBStorageHelper;
import atomspace.storage.util.AtomspaceStorageUtils;

public class RunnerStorages {

    public static AtomspaceMemoryStorage getMemoryStorage() {
        return new AtomspaceMemoryStorage();
    }

    public static AtomspaceRelationalDBStorage getRelationalDBStorage() {
        String dir = "/tmp/atomspace-storage/performance/relationaldb";
        AtomspaceStorageUtils.removeDirectory(dir);
        return AtomspaceRelationalDBStorageHelper.getInMemoryStorage(dir);
    }

    public static AtomspaceNeo4jStorage getNeo4jStorage() {
        String dir = "/tmp/atomspace-storage/performance/neo4j";
        AtomspaceStorageUtils.removeDirectory(dir);
        return new AtomspaceNeo4jStorage(dir);
    }

//    private static AtomspaceJanusGraphStorage getJanusGraphStorage() {
//        return AtomspaceJanusGraphStorageHelper.getJanusGraphInMemoryStorage();
//    }

    public static AtomspaceJanusGraphStorage getJanusGraphStorage() {
        String dir = "/tmp/atomspace-storage/performance/janusgraph";
        AtomspaceStorageUtils.removeDirectory(dir);
        return AtomspaceJanusGraphStorageHelper.getJanusGraphBerkeleyDBStorage(dir);
    }

    public static class MemoryStorageWrapper implements StorageWrapper {

        AtomspaceMemoryStorage storage = getMemoryStorage();
        AtomspaceMemoryStorageHelper helper = new AtomspaceMemoryStorageHelper(storage);

        @Override
        public String getName() {
            return "create1Memory";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASMemoryTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "memory");
            }
        }

        @Override
        public void clean() {
            try (ASMemoryTransaction tx = storage.getTx()) {
                helper.reset(tx);
                tx.commit();
            }
        }
    }

    public static class RelationalDBStorageWrapper implements StorageWrapper {

        AtomspaceRelationalDBStorage storage = getRelationalDBStorage();
        AtomspaceRelationalDBStorageHelper helper = new AtomspaceRelationalDBStorageHelper(storage);

        @Override
        public String getName() {
            return "create2RelationalDB";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASRelationalDBTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "relational db");
            }
        }

        @Override
        public void clean() {
            try (ASRelationalDBTransaction tx = storage.getTx()) {
                helper.reset(tx);
                tx.commit();
            }
        }
    }

    public static class Neo4jStorageWrapper implements StorageWrapper {

        AtomspaceNeo4jStorage storage = getNeo4jStorage();
        AtomspaceNeo4jStorageHelper helper = new AtomspaceNeo4jStorageHelper(storage);

        @Override
        public String getName() {
            return "create3Neo4j";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASNeo4jTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "neo4j");
            }
        }

        @Override
        public void clean() {
            try (ASNeo4jTransaction tx = storage.getTx()) {
                helper.reset(tx);
                tx.commit();
            }
        }
    }

    public static class JanusGraphStorageWrapper implements StorageWrapper {

        AtomspaceJanusGraphStorage storage = getJanusGraphStorage();
        AtomspaceJanusGraphStorageHelper helper = new AtomspaceJanusGraphStorageHelper(storage);

        @Override
        public String getName() {
            return "create4JanusGraph";
        }

        @Override
        public AtomspaceStorage getStorage() {
            return storage;
        }

        @Override
        public void printStatistics() {
            try (ASJanusGraphTransaction tx = storage.getTx()) {
                helper.printStatistics(tx, "janusgraph");
            }
        }

        @Override
        public void clean() {
            try (ASJanusGraphTransaction tx = storage.getTx()) {
                helper.reset(tx);
                tx.commit();
            }
        }
    }

}
