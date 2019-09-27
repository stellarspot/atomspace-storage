package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.util.AtomspaceStorageUtils;

import java.sql.*;
import java.util.Iterator;

import static atomspace.storage.relationaldb.AtomSpaceRelationalDBStorage.TABLE_ATOMS;
import static atomspace.storage.relationaldb.AtomSpaceRelationalDBStorage.TABLE_INCOMING_SET;

public class AtomspaceRelationalDBStorageTransaction implements AtomspaceStorageTransaction {

    static final String QUERY = String.format(
            "SELECT id from %s where type = ? and value = ? and size = ? and ids = ?",
            TABLE_ATOMS);

    static final String INSERT = String.format(
            "INSERT INTO %s (type, value, size, ids) values (?, ?, ?, ?)",
            TABLE_ATOMS);

    final Connection connection;

    public AtomspaceRelationalDBStorageTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ASAtom get(String type, String value) {

        long id = get(type, value, 0, "");
        return new ASRelationalDBNode(connection, id, type, value);
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        String ids = AtomspaceStorageUtils.convertIdsToString(atoms);
        long id = get(type, "", atoms.length, ids);
        return new ASRelationalDBLink(connection, id, type, atoms);
    }

    private long get(String type, String value, int size, String ids) {

        try (PreparedStatement statement = connection.prepareStatement(QUERY)) {

            statement.setString(1, type);
            statement.setString(2, value);
            statement.setInt(3, size);
            statement.setString(4, ids);

            try (ResultSet resultSet = statement.executeQuery()) {

                if (resultSet.next()) {
                    long id = resultSet.getLong("id");
                    return id;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement statement = connection
                .prepareStatement(INSERT, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, type);
            statement.setString(2, value);
            statement.setInt(3, size);
            statement.setString(4, ids);

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                long id = generatedKeys.getLong(1);
                return id;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Iterator<ASAtom> getAtoms() {
        return null;
    }

    @Override
    public void commit() {

    }

    @Override
    public void close() {
    }

    void reset() throws SQLException {
        resetTable(TABLE_ATOMS);
        resetTable(TABLE_INCOMING_SET);
    }

    void resetTable(String table) throws SQLException {

        String sql = String.format("TRUNCATE TABLE %s", table);

        DatabaseMetaData dbmd = connection.getMetaData();
        try (ResultSet rs = dbmd.getTables(null, null, table, null)) {
            if (rs.next()) {
                try (Statement statement = connection.createStatement()) {
                    statement.executeUpdate(sql);
                }
            }
        }
    }

    void dump(String msg) {
        System.out.printf("dump %s%n", msg);
        try {
            dump();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void dump() throws SQLException {

        String sql = String.format("select * from %s", TABLE_ATOMS);

        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    System.out.print(rs.getString(i) + " ");
                }
                System.out.println();
            }
        }
    }
}
