
The aim of the project is to try a simple Atomspace implementation with basic Query Engine
with different backing storages.

What is implemented:
* Atoms creation
* Atoms querying

What is not implemented:
* Atoms removing
* Child atomspaces
* Querying atoms which contains only variable nodes
* Querying typed atoms

# Description

Main modules:
* [Atomspace Storage](src/main/java/atomspace/storage)
* [Query Engine](src/main/java/atomspace/query)

Backing Storages:
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
