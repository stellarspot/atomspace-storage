
The aim of the project is to try a simple Atomspace implementation with basic Pattern Matcher
with different backing storages.

# Atomspace Storage

* [ASAtom](src/main/java/atomspace/storage/ASAtom.java)
* [ASNode](src/main/java/atomspace/storage/ASNode.java)
* [ASLink](src/main/java/atomspace/storage/ASLink.java)
* [AtomspaceStorage](src/main/java/atomspace/storage/AtomspaceStorage.java)
* [AtomspaceStorageTransaction](src/main/java/atomspace/storage/AtomspaceStorageTransaction.java)

# Query Engine

* [ASQueryEngine](src/main/java/atomspace/query/ASQueryEngine.java)

# Backing Storages

* [Memory](src/main/java/atomspace/storage/memory)
* [Neo4j](src/main/java/atomspace/storage/neo4j)
* [JanusGraph](src/main/java/atomspace/storage/janusgraph)

# Building

Build project:
```bash
gradle build
```

Run tests:
```bash
gradle test
```
Run performance tests:
```bash
gradle jmh
```
