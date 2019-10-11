The main of the project is investigation of a simple AtomSpace implementation on top of different (including relational
and graph) backing storages.

# Description

Main modules:
* [Atomspace Storage](src/main/java/atomspace/storage)
* [Query Engine](src/main/java/atomspace/query)

# Current status

What is implemented:
* Atoms creation
* Atoms querying

What is not implemented:
* Atoms removing
* Atoms key-value properties
* Child atomspaces
* Querying typed atoms
* Querying atoms which contains only variable nodes

Supported backing storages:
* InMemory
  * [Memory](src/main/java/atomspace/storage/memory)
* Relational DB
  * [Derby](src/main/java/atomspace/storage/relationaldb)
* Property Graph
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
