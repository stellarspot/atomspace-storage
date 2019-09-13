package atomspace.query.basic;

import atomspace.query.ASQueryEngine;
import atomspace.storage.*;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ASBasicQueryEngine implements ASQueryEngine {

    private static Logger LOG = LoggerFactory.getLogger(ASBasicQueryEngine.class);

    public static final String TYPE_NODE_VARIABLE = "VariableNode";

    private static final int ROOT_DIRECTION = -1;
    private static final int UNDEFINED_DIRECTION = -2;

    @Override
    public Iterator<Map<String, ASAtom>> match(ASAtom atom) {

        LOG.trace("query {}", atom);

        QueryTreeNode root = new QueryTreeNode(null, atom, ROOT_DIRECTION);
        QueryTreeNode startNode = findStartNode(root);
        LOG.trace("start node {}", startNode.atom);

        Queue<QueryMatcherNode> queries = new ArrayDeque<>();
        queries.add(new QueryMatcherNode(UNDEFINED_DIRECTION, startNode, startNode.atom, new HashMap<>()));

        List<Map<String, ASAtom>> results = new LinkedList<>();

        while (!queries.isEmpty()) {
            QueryMatcherNode match = queries.poll();
            LOG.trace("node to match {}", match.rightAtom);

            // match subtree
            if (!matchSubTree(match)) {
                continue;
            }

            // match root
            if (match.leftTreeNode.isRoot()) {
                LOG.trace("node accepted {}", match.rightAtom);
                results.add(match.variables);
                continue;
            }

            // match parent
            if (match.direction == ROOT_DIRECTION) {
                continue;
            }

            ASAtom rightAtom = match.rightAtom;

            ASIncomingSet incomingSet = rightAtom.getIncomingSet();

            QueryTreeNode parent = match.leftTreeNode.parent;
            String parentType = parent.atom.getType();
            int parentSize = parent.size;
            int parentPosition = match.leftTreeNode.parentPosition;

            Iterator<ASLink> iter = incomingSet.getIncomingSet(parentType, parentSize, parentPosition);

            while (iter.hasNext()) {
                ASLink link = iter.next();
                QueryMatcherNode parentMatch = new QueryMatcherNode(parentPosition, parent, link, match.copyVariables());
                queries.add(parentMatch);
            }
        }

        return results.iterator();
    }

    QueryTreeNode findStartNode(QueryTreeNode root) {

        NodeWithCost current = null;

        String type = root.atom.getType();
        int size = root.size;
        for (int i = 0; i < root.children.length; i++) {
            NodeWithCost child = findStartNode(root.children[i], type, size, i);

            if (child == null) {
                continue;
            }

            if (current == null || child.cost < current.cost) {
                current = child;
            }
        }

        return current.node;
    }

    NodeWithCost findStartNode(QueryTreeNode node, String parentType, int parentSize, int position) {

        if (node.isVariable) {
            return null;
        }


        QueryTreeNode[] children = node.children;

        if (children.length == 0) {
            int cost = getCost(node.atom, parentType, parentSize, position);
            return new NodeWithCost(node, cost);
        }

        QueryTreeNode currentNode = null;
        String type = node.atom.getType();
        int size = node.size;
        int currentCost = 0;

        for (int i = 0; i < children.length; i++) {
            NodeWithCost child = findStartNode(children[i], type, size, i);

            if (child == null) {
                continue;
            }

            if (currentNode == null || child.cost <= currentCost) {
                currentNode = child.node;
                currentCost = child.cost;
            }
        }

        return new NodeWithCost(currentNode, currentCost);
    }

    boolean matchSubTree(QueryMatcherNode match) {

        ASAtom rightAtom = match.rightAtom;
        ASAtom leftAtom = match.leftTreeNode.atom;

        // match right variable
        if (isVariable(rightAtom.getType())) {
            return false;
        }

        // match left variable
        if (match.leftTreeNode.isVariable) {

            String name = ((ASNode) leftAtom).getValue();
            ASAtom value = match.variables.get(name);

            if (value == null) {
                match.variables.put(name, match.rightAtom);
                return true;
            }

            return value.equals(rightAtom);
        }

        // match node
        if (match.leftTreeNode.isLeaf()) {
            return leftAtom.equals(rightAtom);
        }

        // match link
        if (!leftAtom.getType().equals(rightAtom.getType())) {
            return false;
        }

        ASOutgoingList outgoingList = ((ASLink) rightAtom).getOutgoingList();

        QueryTreeNode[] children = match.leftTreeNode.children;

        for (int i = 0; i < children.length; i++) {

            // Already visited
            if (match.direction == i) {
                continue;
            }

            QueryMatcherNode childMatch = new QueryMatcherNode(ROOT_DIRECTION, children[i], outgoingList.getAtom(i), match.variables);
            if (!matchSubTree(childMatch)) {
                return false;
            }
        }

        return true;
    }


    static boolean isVariable(String type) {
        return TYPE_NODE_VARIABLE.equals(type);
    }

    static int getCost(ASAtom atom, String type, int size, int position) {
        ASIncomingSet incomingSet = atom.getIncomingSet();
        return incomingSet.getIncomingSetSize(type, size, position);
    }

    static class NodeWithCost {
        final QueryTreeNode node;
        final int cost;

        public NodeWithCost(QueryTreeNode node, int cost) {
            this.node = node;
            this.cost = cost;
        }
    }

    static class QueryMatcherNode {
        final int direction;
        final QueryTreeNode leftTreeNode;
        final ASAtom rightAtom;
        final HashMap<String, ASAtom> variables;

        public QueryMatcherNode(int direction, QueryTreeNode leftTreeNode, ASAtom rightAtom, HashMap<String, ASAtom> variables) {
            this.direction = direction;
            this.leftTreeNode = leftTreeNode;
            this.rightAtom = rightAtom;
            this.variables = variables;
        }

        public HashMap<String, ASAtom> copyVariables() {
            return (HashMap<String, ASAtom>) (variables.clone());
        }
    }

    static class QueryTreeNode {

        final ASAtom atom;
        final int parentPosition;
        final QueryTreeNode parent;
        final QueryTreeNode[] children;
        final int size;
        final boolean isVariable;

        private static final QueryTreeNode[] EMPTY_CHILDREN = new QueryTreeNode[0];

        public QueryTreeNode(QueryTreeNode parent, ASAtom atom, int parentPosition) {
            this.parent = parent;
            this.atom = atom;
            this.parentPosition = parentPosition;

            if (atom instanceof ASNode) {
                this.isVariable = isVariable(atom.getType());
                this.children = EMPTY_CHILDREN;
                this.size = 0;
            } else {
                this.isVariable = false;
                ASLink link = (ASLink) atom;
                ASOutgoingList outgoingList = link.getOutgoingList();
                int n = outgoingList.getSize();
                this.children = new QueryTreeNode[n];
                this.size = n;

                for (int i = 0; i < n; i++) {
                    this.children[i] = new QueryTreeNode(this, outgoingList.getAtom(i), i);
                }
            }
        }

        public boolean isRoot() {
            return parent == null;
        }

        public boolean isLeaf() {
            return size == 0;
        }
    }
}
