package atomspace.storage.relationaldb;

import atomspace.storage.AtomspaceStorage;

import java.sql.*;

public class AtomSpaceRelationalDBStorage implements AtomspaceStorage {


    static final String TABLE_ATOMS = "ATOMS";
    static final String TABLE_OUTGOING_LIST = "OUTGOING_LIST";
    static final String CREATE_TABLE_ATOMS = String.format(
            "CREATE TABLE %s(" +
                    "id BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY PRIMARY KEY," +
                    "type VARCHAR(255)," +
                    "value VARCHAR(255)," +
                    "size INTEGER NOT NULL" +
                    ")",
            TABLE_ATOMS);

    static final String CREATE_TABLE_OUTGOING_LIST = String.format(
            "CREATE TABLE %s(" +
                    " parent_id BIGINT PRIMARY KEY," +
                    " child_id BIGINT NOT NULL," +
                    " position INTEGER NOT NULL" +
                    ")",
            TABLE_OUTGOING_LIST);

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
        initTable(TABLE_OUTGOING_LIST, CREATE_TABLE_OUTGOING_LIST);
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
