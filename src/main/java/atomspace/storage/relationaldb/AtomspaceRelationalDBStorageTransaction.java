package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorageTransaction;

import java.sql.*;
import java.util.Iterator;

import static atomspace.storage.relationaldb.AtomSpaceRelationalDBStorage.TABLE_ATOMS;

public class AtomspaceRelationalDBStorageTransaction implements AtomspaceStorageTransaction {

    static final String QUERY_NODE = String.format(
            "SELECT id from %s where type = ? and value = ?",
            TABLE_ATOMS);

    static final String INSERT_NODE = String.format(
            "INSERT INTO %s (type, value) values (?, ?)",
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

        return null;
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

    void reset() {
    }

    private static long[] getIds(ASAtom... atoms) {
        long[] ids = new long[atoms.length];

        for (int i = 0; i < atoms.length; i++) {
            ids[i] = atoms[i].getId();
        }
        return ids;
    }
}
