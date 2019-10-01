# Atomspace Storage

## Atoms Hierarchy

Atom
* id
* type
* IncomingSet

Node <- Atom
* value

Link <- Atom
* OutgoingSet
  * arity
  * childrenIds

## Storage Operations

Atoms retrieving:
* getNode(type, value)
* getLink(type, [id])

Atoms querying:
* getIncomingSetArity(id, type, arity, position) -> arity
* getIncomingSet(id, type, arity, position) -> [id]
