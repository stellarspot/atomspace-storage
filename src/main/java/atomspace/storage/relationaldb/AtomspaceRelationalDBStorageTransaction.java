package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorageTransaction;

import java.sql.*;
import java.util.Iterator;

import static atomspace.storage.relationaldb.AtomspaceRelationalDBStorage.TABLE_ATOMS;
import static atomspace.storage.relationaldb.AtomspaceRelationalDBStorage.TABLE_INCOMING_SET;
import static atomspace.storage.util.AtomspaceStorageUtils.*;

public class AtomspaceRelationalDBStorageTransaction implements AtomspaceStorageTransaction {

    static final String QUERY_ATOM_ID = String.format(
            "SELECT id from %s where type = ? and value = ? and size = ? and ids = ?",
            TABLE_ATOMS);

    static final String QUERY_ATOM = String.format(
            "SELECT type, value, size, ids from %s where id = ?",
            TABLE_ATOMS);

    static final String INSERT_ATOM = String.format(
            "INSERT INTO %s (type, value, size, ids) values (?, ?, ?, ?)",
            TABLE_ATOMS);

    static final String UPDATE_INCOMING_SET = String.format(
            "INSERT INTO %s (id, type_arity_pos, parent_id) values (?, ?, ?)",
            TABLE_INCOMING_SET);

    static final String QUERY_INCOMING_SET_ARITY = String.format(
            "SELECT count(*) as total from %s where id = ? and type_arity_pos = ?",
            TABLE_INCOMING_SET);

    static final String QUERY_INCOMING_SET = String.format(
            "SELECT parent_id from %s where id = ? and type_arity_pos = ?",
            TABLE_INCOMING_SET);


    final Connection connection;

    public AtomspaceRelationalDBStorageTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ASAtom get(String type, String value) {

        long id = get(type, value, 0);
        return new ASRelationalDBNode(connection, id, type, value);
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        long[] ids = getIds(atoms);
        long id = get(type, "", atoms.length, ids);
        return new ASRelationalDBLink(connection, id, type, atoms);
    }

    private long get(String type, String value, int size, long... ids) {

        try (PreparedStatement statement = connection.prepareStatement(QUERY_ATOM_ID)) {

            statement.setString(1, type);
            statement.setString(2, value);
            statement.setInt(3, size);
            statement.setString(4, idsToString(ids));

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
                .prepareStatement(INSERT_ATOM, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, type);
            statement.setString(2, value);
            statement.setInt(3, size);
            statement.setString(4, idsToString(ids));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                long id = generatedKeys.getLong(1);

                if (size > 0) {
                    updateIncomingSet(id, type, ids);
                }

                return id;
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void updateIncomingSet(long parentId, String type, long... ids) {

        try (PreparedStatement statement = connection.prepareStatement(UPDATE_INCOMING_SET)) {

            int arity = ids.length;
            for (int i = 0; i < arity; i++) {

                long id = ids[i];
                String key = getKey(type, arity, i);

                statement.setLong(1, id);
                statement.setString(2, key);
                statement.setLong(3, parentId);

                statement.addBatch();
            }

            statement.executeBatch();

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

    void dump(String msg, Object... args) {
        System.out.printf(msg, args);
        try {
            dump();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    void dump() throws SQLException {

        System.out.printf("--- Dump ---%n");
        dumpTable(TABLE_ATOMS);
        dumpTable(TABLE_INCOMING_SET);
        System.out.printf("--- ---- ---%n");
    }

    void dumpTable(String table) throws SQLException {

        String sql = String.format("select * from %s", table);

        System.out.printf("Table: %s%n", table);
        try (Statement st = connection.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnsNumber = rsmd.getColumnCount();

            while (rs.next()) {
                for (int i = 1; i <= columnsNumber; i++) {
                    String cell = rs.getObject(i).toString();
                    cell = cell.isEmpty() ? "''" : cell;
                    System.out.printf("%s ", cell);
                }
                System.out.println();
            }
        }
    }
}
