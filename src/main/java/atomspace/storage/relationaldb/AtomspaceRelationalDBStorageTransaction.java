package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.util.AtomspaceStorageUtils;

import java.sql.*;
import java.util.Iterator;

import static atomspace.storage.relationaldb.AtomSpaceRelationalDBStorage.TABLE_ATOMS;

public class AtomspaceRelationalDBStorageTransaction implements AtomspaceStorageTransaction {

    static final String QUERY_NODE = String.format(
            "SELECT id from %s where type = ? and value = ?",
            TABLE_ATOMS);

    static final String INSERT_NODE = String.format(
            "INSERT INTO %s (type, value, size) values (?, ?, 0)",
            TABLE_ATOMS);

    static final String QUERY_LIST = String.format(
            "SELECT id from %s where type = ? and size = ? and ids = ?",
            TABLE_ATOMS);

    static final String INSERT_LIST = String.format(
            "INSERT INTO %s (type, size, ids) values (?, ?, ?)",
            TABLE_ATOMS);

    final Connection connection;

    public AtomspaceRelationalDBStorageTransaction(Connection connection) {
        this.connection = connection;
    }

    @Override
    public ASAtom get(String type, String value) {

        try (PreparedStatement statement = connection.prepareStatement(QUERY_NODE)) {

            statement.setString(1, type);
            statement.setString(2, value);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                return new ASRelationalDBNode(connection, id, type, value);
            }


        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement statement = connection
                .prepareStatement(INSERT_NODE, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, type);
            statement.setString(2, value);

            long id = statement.executeUpdate();
            return new ASRelationalDBNode(connection, id, type, value);

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ASAtom get(String type, ASAtom... atoms) {

        String ids = AtomspaceStorageUtils.convertIdsToString(atoms);

        try (PreparedStatement statement = connection.prepareStatement(QUERY_LIST)) {

            statement.setString(1, type);
            statement.setInt(2, atoms.length);
            statement.setString(3, ids);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                long id = resultSet.getLong("id");
                return new ASRelationalDBLink(connection, id, type, atoms);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        try (PreparedStatement statement = connection
                .prepareStatement(INSERT_LIST, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, type);
            statement.setInt(2, atoms.length);
            statement.setString(3, ids);

            long id = statement.executeUpdate();
            return new ASRelationalDBLink(connection, id, type, atoms);

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
        resetTable(AtomSpaceRelationalDBStorage.TABLE_ATOMS);
        resetTable(AtomSpaceRelationalDBStorage.TABLE_INCOMING_SET);
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
}
