package atomspace.storage.relationaldb;

import atomspace.storage.AtomspaceStorage;
import atomspace.storage.neo4j.AtomspaceNeo4jStorageTransaction;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;

import java.io.File;
import java.sql.*;

public class AtomSpaceRelationalDBStorage implements AtomspaceStorage {


    static final String TABLE_ATOMS = "ATOMS";
    static final String CREATE_TABLE_ATOMS = String.format(
            "CREATE TABLE %s(" +
                    "id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY," +
                    "type VARCHAR(255)," +
                    "value VARCHAR(255)" +
                    ")",
            TABLE_ATOMS);

    final Connection connection;

    public AtomSpaceRelationalDBStorage(String dbURL) throws SQLException {
        this.connection = DriverManager.getConnection(dbURL);
        this.initDB();
    }

    @Override
    public AtomspaceRelationalDBStorageTransaction getTx() {
        return new AtomspaceRelationalDBStorageTransaction(connection);
    }

    @Override
    public void close() throws SQLException {
        connection.close();
    }

    void initDB() throws SQLException {
        DatabaseMetaData dbmd = connection.getMetaData();
        try (ResultSet rs = dbmd.getTables(null, null, TABLE_ATOMS, null)) {
            if (!rs.next()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(CREATE_TABLE_ATOMS);
                }
            }
        }
    }
}
