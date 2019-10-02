package atomspace.storage.relationaldb;

import atomspace.storage.ASAtom;
import atomspace.storage.ASIncomingSet;
import atomspace.storage.ASLink;
import atomspace.storage.util.AtomspaceStorageUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static atomspace.storage.util.AtomspaceStorageUtils.*;

import static atomspace.storage.relationaldb.AtomspaceRelationalDBStorageTransaction.QUERY_ATOM;
import static atomspace.storage.relationaldb.AtomspaceRelationalDBStorageTransaction.QUERY_INCOMING_SET;
import static atomspace.storage.relationaldb.AtomspaceRelationalDBStorageTransaction.QUERY_INCOMING_SET_ARITY;


public abstract class ASRelationalDBAtom implements ASAtom {

    final Connection connection;
    final long id;
    final String type;
    final ASIncomingSet incomingSet = new ASRelationalDBIncomingSet();

    public ASRelationalDBAtom(Connection connection, long id, String type) {
        this.connection = connection;
        this.id = id;
        this.type = type;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public String getType() {
        return type;
    }

    @Override
    public ASIncomingSet getIncomingSet() {
        return incomingSet;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof ASRelationalDBAtom) {
            ASRelationalDBAtom that = (ASRelationalDBAtom) obj;
            return this.getId() == that.getId();
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Long.hashCode(getId());
    }

    ASRelationalDBAtom getAtom(long id) {

        try (PreparedStatement statement = connection.prepareStatement(QUERY_ATOM)) {

            statement.setLong(1, id);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {

                    String type = resultSet.getString(1);
                    int arity = resultSet.getInt(3);

                    if (arity == 0) {
                        String value = resultSet.getString("value");
                        return new ASRelationalDBNode(connection, id, type, value);
                    } else {
                        String childIds = resultSet.getString("ids");
                        long[] ids = AtomspaceStorageUtils.getIds(childIds);
                        ASAtom[] children = new ASAtom[arity];
                        for (int i = 0; i < arity; i++) {
                            children[i] = getAtom(ids[i]);
                        }
                        return new ASRelationalDBLink(connection, id, type, children);
                    }
                }

                String msg = String.format("Atom with id %d was not found!", id);
                throw new RuntimeException(msg);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    class ASRelationalDBIncomingSet implements ASIncomingSet {

        public void add(ASLink link, int arity, int position) {
        }

        @Override
        public int getIncomingSetArity(String type, int arity, int position) {

            try (PreparedStatement statement = connection.prepareStatement(QUERY_INCOMING_SET_ARITY)) {

                long id = ASRelationalDBAtom.this.getId();
                String key = getKey(type, arity, position);
                statement.setLong(1, id);
                statement.setString(2, key);
                try (ResultSet resultSet = statement.executeQuery()) {
                    if (resultSet.next()) {
                        return resultSet.getInt(1);
                    }
                    return 0;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public Iterator<ASLink> getIncomingSet(String type, int arity, int position) {

            List<ASLink> links = new ArrayList<>();
            try (PreparedStatement statement = connection.prepareStatement(QUERY_INCOMING_SET)) {

                long id = ASRelationalDBAtom.this.getId();
                String key = AtomspaceStorageUtils.getKey(type, arity, position);
                statement.setLong(1, id);
                statement.setString(2, key);
                try (ResultSet resultSet = statement.executeQuery()) {
                    while (resultSet.next()) {
                        long parentId = resultSet.getLong(1);
                        links.add((ASLink) getAtom(parentId));
                    }
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
            return links.iterator();
        }
    }
}
