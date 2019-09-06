package atomspace.storage.memory.query.basic;

import atomspace.storage.*;
import atomspace.storage.memory.query.ASQueryEngine;

import java.util.*;

public class ASBasicQueryEngine implements ASQueryEngine {

    public static final String TYPE_NODE_VARIABLE = "VariableNode";

    private static final int ROOT_DIRECTION = -1;
    private static final int UNDEFINED_DIRECTION = -2;

    @Override
    public Iterator<Map<String, ASAtom>> match(ASAtom atom) {

        QueryTreeNode root = new QueryTreeNode(null, atom, ROOT_DIRECTION);
        QueryTreeNode startNode = findStartNode(root);

        Queue<QueryMatcherNode> queries = new ArrayDeque<>();
        queries.add(new QueryMatcherNode(UNDEFINED_DIRECTION, startNode, startNode.atom, new HashMap<>()));

        List<Map<String, ASAtom>> results = new LinkedList<>();

        while (!queries.isEmpty()) {
            QueryMatcherNode match = queries.poll();

            // match subtree
            if (!matchSubTree(match)) {
                continue;
            }

            // match root
            if (match.leftTreeNode.parent == null) {
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
            int parentPosition = match.leftTreeNode.parentPosition;

            Iterator<ASLink> iter = incomingSet.getIncomingSet(parentType, parentPosition);

            while (iter.hasNext()) {
                ASLink link = iter.next();
                QueryMatcherNode parentMatch = new QueryMatcherNode(ROOT_DIRECTION, parent, link, match.copyVariables());
                queries.add(parentMatch);
            }
        }

        return results.iterator();
    }

    QueryTreeNode findStartNode(QueryTreeNode root) {

        NodeWithCost current = null;

        String type = root.atom.getType();
        for (int i = 0; i < root.children.length; i++) {
            NodeWithCost child = findStartNode(root.children[i], type, i);

            if (child == null) {
                continue;
            }

            if (current == null || child.cost < current.cost) {
                current = child;
            }
        }

        return current.node;
    }

    NodeWithCost findStartNode(QueryTreeNode node, String parentType, int position) {

        String type = node.atom.getType();
        if (isVariable(type)) {
            return null;
        }


        QueryTreeNode[] children = node.children;

        if (children.length == 0) {
            int cost = getCost(node.atom, parentType, position);
            return new NodeWithCost(node, cost);
        }

        QueryTreeNode currentNode = null;
        int currentCost = 0;

        for (int i = 0; i < children.length; i++) {
            NodeWithCost child = findStartNode(children[i], type, i);

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

        // matchSubTree right variable
        if (isVariable(rightAtom.getType())) {
            return false;
        }

        // matchSubTree left variable
        if (isVariable(leftAtom.getType())) {

            String name = ((ASNode) leftAtom).getValue();
            ASAtom value = match.variables.get(name);

            if (value == null) {
                match.variables.put(name, match.rightAtom);
                return true;
            }

            return value.equals(rightAtom);
        }

        QueryTreeNode[] children = match.leftTreeNode.children;

        // matchSubTree nodes
        if (children.length == 0) {
            return leftAtom.equals(rightAtom);
        }

        // matchSubTree links
        if (!leftAtom.getType().equals(rightAtom.getType())) {
            return false;
        }

        ASOutgoingList outgoingList = ((ASLink) rightAtom).getOutgoingList();

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

    static int getCost(ASAtom atom, String type, int position) {
        ASIncomingSet incomingSet = atom.getIncomingSet();
        return incomingSet.getIncomingSetSize(type, position);
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

        public QueryTreeNode(QueryTreeNode parent, ASAtom atom, int parentPosition) {
            this.parent = parent;
            this.atom = atom;
            this.parentPosition = parentPosition;

            if (atom instanceof ASNode) {
                this.children = new QueryTreeNode[0];
            } else {
                ASLink link = (ASLink) atom;
                ASOutgoingList outgoingList = link.getOutgoingList();
                int n = outgoingList.getSize();
                this.children = new QueryTreeNode[n];

                for (int i = 0; i < n; i++) {
                    this.children[i] = new QueryTreeNode(this, outgoingList.getAtom(i), i);
                }
            }
        }
    }
}
