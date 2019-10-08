
The aim of the project is to try a simple Atomspace implementation with basic Pattern Matcher
with different backing storages.

* [Atomspace Storage](src/main/java/atomspace/storage)
* [Query Engine](src/main/java/atomspace/query)

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
