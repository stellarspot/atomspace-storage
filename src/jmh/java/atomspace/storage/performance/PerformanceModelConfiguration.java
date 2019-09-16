package atomspace.storage.performance;

public class PerformanceModelConfiguration {

    public final long randomSeed;
    public final int nodeTypes;
    public final int linkTypes;
    public final int valuesPerType;

    public PerformanceModelConfiguration(int nodeTypes, int linkTypes, int valuesPerType) {
        this(42, nodeTypes, linkTypes, valuesPerType);
    }

    public PerformanceModelConfiguration(long randomSeed, int nodeTypes, int linkTypes, int valuesPerType) {
        this.randomSeed = randomSeed;
        this.nodeTypes = nodeTypes;
        this.linkTypes = linkTypes;
        this.valuesPerType = valuesPerType;
    }
}
