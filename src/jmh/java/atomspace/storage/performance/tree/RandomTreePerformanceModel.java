package atomspace.storage.performance.tree;

import atomspace.query.ASQueryEngine;
import atomspace.query.basic.ASBasicQueryEngine;
import atomspace.storage.ASAtom;
import atomspace.storage.AtomspaceStorage;
import atomspace.storage.AtomspaceStorageTransaction;
import atomspace.storage.memory.AtomspaceMemoryStorage;
import atomspace.storage.performance.PerformanceModelConfiguration;
import atomspace.storage.performance.PerformanceModel;
import atomspace.storage.performance.PerformanceModelParameters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class RandomTreePerformanceModel implements PerformanceModel {

    private static final String VARIABLE_TYPE = ASBasicQueryEngine.TYPE_NODE_VARIABLE;

    public final PerformanceModelConfiguration config;
    public final PerformanceModelParameters params;
    public final int averageWidth;
    public final int averageDepth;
    public final int averageVariables;
    public final Random random;

    private final List<RandomNode> statements = new ArrayList<>();
    private final List<RandomNode> queries = new ArrayList<>();

    public RandomTreePerformanceModel(PerformanceModelConfiguration config,
                                      PerformanceModelParameters params,
                                      int averageWidth,
                                      int averageDepth,
                                      int averageVariables) {

        this.config = config;
        this.params = params;
        this.averageWidth = averageWidth;
        this.averageDepth = averageDepth;
        this.averageVariables = averageVariables;
        this.random = new Random(config.randomSeed + 2);

        this.init();
    }

    @Override
    public void createAtoms(AtomspaceStorage atomspace) throws IOException {

        try (AtomspaceStorageTransaction tx = atomspace.getTx()) {
            for (RandomNode statement : statements) {
                createAtom(tx, statement);
            }
        }
    }

    private ASAtom createAtom(AtomspaceStorageTransaction tx, RandomNode node) {

        int n = node.children.length;

        if (n == 0) {
            return tx.get(node.type, node.value);
        }

        ASAtom[] atoms = new ASAtom[node.children.length];
        for (int i = 0; i < n; i++) {
            atoms[i] = createAtom(tx, node.children[i]);
        }

        return tx.get(node.type, atoms);
    }

    @Override
    public void queryAtoms(AtomspaceStorage atomspace, ASQueryEngine queryEngine) throws Exception {

        try (AtomspaceStorageTransaction tx = atomspace.getTx()) {
            for (RandomNode queryNode : queries) {
                ASAtom query = createAtom(tx, queryNode);
                Iterator iter = queryEngine.match(query);
            }
        }
    }

    private final void init() {
        for (int i = 0; i < params.statements; i++) {
            statements.add(getNode(averageWidth, averageDepth - 1));
        }

        for (int i = 0; i < params.queries; i++) {

            RandomNode node = statements.get(random.nextInt(statements.size()));
            QueryItem queryItem = getQuery(node, averageVariables);
            queries.add(queryItem.node);
        }
    }

    private RandomNode getNode(int width, int depth) {

        if (depth == 0) {
            return new RandomNode(getNodeType(), getValue());
        }

        int currentWidth = random.nextInt(width) + 1;
        int currentDepth = random.nextInt(depth) + 1;


        RandomNode[] children = new RandomNode[currentWidth];

        for (int i = 0; i < currentWidth; i++) {
            children[i] = getNode(currentWidth, currentDepth - 1);
        }

        return new RandomNode(getLinkType(), children);
    }

    private QueryItem getQuery(RandomNode node, int variables) {
        int n = node.children.length;

        if (n == 0) {

            if (convertNodeToVariable(variables)) {
                String variableName = String.format("$%s", node.value.toUpperCase());
                return new QueryItem(new RandomNode(VARIABLE_TYPE, variableName), variables - 1);
            } else {
                return new QueryItem(node, variables);
            }
        }

        RandomNode[] children = new RandomNode[n];
        int vars = variables;
        for (int i = 0; i < n; i++) {
            QueryItem queryItem = getQuery(node.children[i], vars);
            children[i] = queryItem.node;
            vars = queryItem.variables;
        }

        return new QueryItem(new RandomNode(node.type, children), vars);
    }

    private String getNodeType() {
        return String.format("Node%d", random.nextInt(config.nodeTypes));
    }

    private String getLinkType() {
        return String.format("Link%d", random.nextInt(config.nodeTypes));
    }

    private String getValue() {
        return String.format("Value%d", random.nextInt(config.valuesPerType));
    }

    private boolean convertNodeToVariable(int variables) {
        return variables > 0 && random.nextInt(5) >= 2;
    }

    public void dump() {
        System.out.printf("--- dump ---%n");
        System.out.printf("statements: %d%n", statements.size());
        for (RandomNode statement : statements) {
            System.out.printf("%s%n", statement);
        }
        System.out.printf("queries: %d%n", queries.size());
        for (RandomNode query : queries) {
            System.out.printf("%s%n", query);
        }
        System.out.printf("--- ---- ---%n");
    }

    private static class QueryItem {
        final RandomNode node;
        final int variables;

        public QueryItem(RandomNode node, int variables) {
            this.node = node;
            this.variables = variables;
        }
    }

    private static class RandomNode {
        final String type;
        final String value;
        final RandomNode[] children;

        final static RandomNode[] EMPTY = {};

        public RandomNode(String type, String value) {
            this(type, value, EMPTY);
        }

        public RandomNode(String type, RandomNode... children) {
            this(type, "", children);
        }

        public RandomNode(String type, String value, RandomNode[] children) {
            this.type = type;
            this.value = value;
            this.children = children;
        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            toString(builder, "");
            builder.append('\n');
            return builder.toString();
        }

        public void toString(StringBuilder builder, String indent) {
            builder
                    .append("\n")
                    .append(indent)
                    .append(type)
                    .append("[")
                    .append(children.length)
                    .append("]")
                    .append("(");
            if (children.length == 0) {
                builder
                        .append("'")
                        .append(value)
                        .append("'");
            } else {
                String nextIndent = indent + " ";
                for (RandomNode node : children) {
                    node.toString(builder, nextIndent);
                }
            }
            builder.append(")");
        }
    }

    public static void main(String[] args) throws Exception {

        PerformanceModelConfiguration config = new PerformanceModelConfiguration(3, 3, 3);
        PerformanceModelParameters params = new PerformanceModelParameters(5, 5);
        RandomTreePerformanceModel model = new RandomTreePerformanceModel(config, params, 3, 3, 2);
        model.dump();

        try (AtomspaceStorage atomspace = new AtomspaceMemoryStorage();
             AtomspaceStorageTransaction tx = atomspace.getTx()) {

            ASQueryEngine queryEngine = new ASBasicQueryEngine();
            model.createAtoms(atomspace);
            model.queryAtoms(atomspace, queryEngine);
        }
    }
}
