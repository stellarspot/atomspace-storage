package atomspace.storage.relationaldb;

import atomspace.storage.AtomspaceStorage;

import java.sql.*;

public class AtomSpaceRelationalDBStorage implements AtomspaceStorage {


    static final String TABLE_ATOMS = "ATOMS";
    static final String TABLE_INCOMING_SET = "INCOMING_SET";

    static final String CREATE_TABLE_ATOMS = String.format(
            "CREATE TABLE %s(" +
                    "id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                    "type VARCHAR(255)," +
                    "value VARCHAR(255)," +
                    "size INTEGER NOT NULL," +
                    "ids VARCHAR(1024)" + // Use LONG VARCHAR
                    ")",
            TABLE_ATOMS);

    static final String CREATE_TABLE_INCOMING_SET = String.format(
            "CREATE TABLE %s(" +
                    "id BIGINT," +
                    "type_arity_pos VARCHAR(255)," +
                    "parent_id BIGINT," +
                    "PRIMARY KEY (id, type_arity_pos, parent_id))",
            TABLE_INCOMING_SET);

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
        initTable(TABLE_ATOMS, CREATE_TABLE_ATOMS);
        initTable(TABLE_ATOMS, CREATE_TABLE_INCOMING_SET);
    }

    private void initTable(String tableName, String sql) throws SQLException {
        DatabaseMetaData dbmd = connection.getMetaData();
        try (ResultSet rs = dbmd.getTables(null, null, tableName, null)) {
            if (!rs.next()) {
                try (Statement statement = connection.createStatement()) {
                    statement.execute(sql);
                }
            }
        }
    }
}
