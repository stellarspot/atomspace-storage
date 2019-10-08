# Atomspace Storage

## Atoms

Atom
* id
* type
* IncomingSet
  * getIncomingSetArity(id, type, arity, position): arity
  * getIncomingSet(id, type, arity, position): [id]

Node extends Atom
* value

Link extends Atom
* OutgoingSet
  * arity
  * childrenIds

## Storage

## Storage Transaction

Atoms retrieving

Get node:
* get(type, value): id

Get link:
* get(type, [id]): id

Get Incoming Set:
* getIncomingSetArity(id, type, arity, pos): arity
* getIncomingSet(id, type, arity, pos): [id]
