package atomspace.layer.gremlin;

import atomspace.layer.gremlin.AtomspaceGremlinStorage.TransactionWithSource;
import atomspace.storage.ASTransaction;
import atomspace.storage.janusgraph.AtomspaceJanusGraphStorageHelper;
import atomspace.storage.janusgraph.JanusGraphUtils;
import atomspace.storage.util.AtomspaceStorageHelper;
import org.apache.commons.configuration.BaseConfiguration;
import org.apache.commons.configuration.Configuration;
import org.apache.tinkerpop.gremlin.process.traversal.dsl.graph.GraphTraversalSource;
import org.janusgraph.core.JanusGraph;
import org.janusgraph.core.JanusGraphFactory;
import org.janusgraph.graphdb.database.StandardJanusGraph;
import org.janusgraph.graphdb.idmanagement.IDManager;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicLong;

public class AtomspaceGremlinStorageHelper implements AtomspaceStorageHelper {

    @Override
    public void dump(ASTransaction tx) {
        ((ASAbstractGremlinTransaction) tx).dump();
    }

    @Override
    public void reset(ASTransaction tx) {
        ((ASAbstractGremlinTransaction) tx).reset();
    }

    @Override
    public void printStatistics(ASTransaction tx, String msg) {
        ((ASAbstractGremlinTransaction) tx).printStatistics(msg);
    }

    public static AtomspaceGremlinStorage getRemoteJanusGraph(String host, int port, boolean oneRequest, boolean useCustomIds) {
        GremlinRemoteStorage storage = new GremlinRemoteStorage(host, port, oneRequest, useCustomIds);
        return new AtomspaceGremlinStorage(storage);
    }

    public static AtomspaceGremlinStorage getInMemoryJanusGraph(boolean useCustomIds) {
        JanusGraph graph = AtomspaceJanusGraphStorageHelper.getInMemoryJanusGraph(useCustomIds);
        GremlinJanusGraphStorage storage = new GremlinJanusGraphStorage(graph, useCustomIds);
        return new AtomspaceGremlinStorage(storage);
    }

    public static class GremlinRemoteStorage implements AtomspaceGremlinStorage.Storage {

        private final GraphTraversalSource g;
        private final boolean oneRequestTransaction;
        private final boolean useCustomIds;
        private final AtomicLong currentId = new AtomicLong();


        public GremlinRemoteStorage(String host, int port, boolean oneRequestTransaction, boolean useCustomIds) {
            this.oneRequestTransaction = oneRequestTransaction;
            this.useCustomIds = useCustomIds;

            try {
                this.g = JanusGraphFactory
                        .open("inmemory")
                        .traversal()
                        .withRemote(getConfig(host, port));
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public TransactionWithSource get() {
            return new TransactionWithSource(g.tx(), g);
        }

        @Override
        public void initGraph() {
        }

        @Override
        public boolean oneRequest() {
            return oneRequestTransaction;
        }

        @Override
        public boolean useCustomIds() {
            return useCustomIds;
        }

        @Override
        public long getNextId() {
            return currentId.incrementAndGet();
        }

        @Override
        public void close() throws IOException {
            try {
                g.close();
            } catch (Exception e) {
                throw new IOException(e);
            }
        }

        private Configuration getConfig(String host, int port) {
            Configuration config = new BaseConfiguration();
            config.setProperty("clusterConfiguration.hosts", host);
            config.setProperty("clusterConfiguration.port", port);
            config.setProperty(
                    "clusterConfiguration.serializer.className",
                    "org.apache.tinkerpop.gremlin.driver.ser.GryoMessageSerializerV1d0");
            config.setProperty(
                    "serializer.config.ioRegistries",
                    "org.janusgraph.graphdb.tinkerpop.JanusGraphIoRegistry");
            config.setProperty(
                    "gremlin.remote.remoteConnectionClass",
                    "org.apache.tinkerpop.gremlin.driver.remote.DriverRemoteConnection");
            config.setProperty("gremlin.remote.driver.sourceName", "g");
            return config;
        }
    }

    public static class GremlinJanusGraphStorage implements AtomspaceGremlinStorage.Storage {

        private final JanusGraph graph;
        private final IDManager idManager;
        private final boolean useCustomIds;
        private final AtomicLong currentId = new AtomicLong();


        public GremlinJanusGraphStorage(JanusGraph graph, boolean useCustomIds) {
            this.graph = graph;
            this.useCustomIds = useCustomIds;
            this.idManager = ((StandardJanusGraph) graph).getIDManager();
        }

        @Override
        public TransactionWithSource get() {
            GraphTraversalSource g = graph.newTransaction().traversal();
            return new TransactionWithSource(g.tx(), g);
        }

        @Override
        public void initGraph() {
            JanusGraphUtils.makeIndices(graph);
        }

        @Override
        public boolean oneRequest() {
            return false;
        }

        @Override
        public boolean useCustomIds() {
            return useCustomIds;
        }

        @Override
        public long getNextId() {
            return JanusGraphUtils.getNextId(idManager, currentId);
        }

        @Override
        public void close() {
            graph.close();
        }
    }
}
