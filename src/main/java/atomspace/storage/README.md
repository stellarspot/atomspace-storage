# Atomspace Storage

Atomspace storage contains implementation for atoms which can be stored and queried by backing storage.
The main idea is that atoms stored in backing storage must be unique.

Each atom can be a node which has type and value or link which has type and list of outgoing atoms.

There are two main types of atoms: RawAtom and ASAtom.

`RawAtom` is an atom in memory which need not to be unique. It represents a user data which should be
stored in backing storage.

For example:
```java
        RawLink rawLink = new RawLink("Link1",
                new RawLink("Link2",
                        new RawNode("Node1", "value1"),
                        new RawNode("Node2", "value2")),
                new RawLink("Link3",
                        new RawNode("Node1", "value1")));
```
Note that both `Link2` and `Link3` contain the same `Node1` node with `value1` value which are represented
as two different nodes in memory.

`ASAtom` represents the atom in backing storage and the same atoms are unique in the backing storage.

For example:
```java
            RawLink rawLink = new RawLink("Link1",
                    new RawLink("Link2",
                            new RawNode("Node1", "value1"),
                            new RawNode("Node2", "value2")),
                    new RawLink("Link3",
                            new RawNode("Node1", "value1")));

            ASLink link = tx.get(rawLink);
```

The `ASLink` can be represented in storage like : `Link[2304]: Link1([3072, 4096])`
where the link type is `Link1`, its id in the backing storage is `2304` and its children ids are `[3072, 4096]`.

The dump of a backing storage can look like:
```text
Node[3328]: Node1(value1)
Node[3584]: Node2(value2)
Link[3072]: Link2([3328, 3584])
Link[4096]: Link3([3328])
Link[2304]: Link1([3072, 4096])
```

And indeed there is only one `Node1` with `value1` with id `3328` and both `Link2` and `Link3` point to the same
node with id `3328`.

## Atoms

Atom
* `id`
* `type`
* IncomingSet
  * `getIncomingSetArity(id, type, arity, position): arity`
  * `getIncomingSet(id, type, arity, position): [id]`

Node extends Atom
* `value`

Link extends Atom
* OutgoingSet
  * `arity`
  * `childrenIds`

## Storage

## Storage Transaction

Atoms retrieving

Get or create node:
* `get(type, value): id`

Get or create link:
* `get(type, [id]): id`

Get Incoming Set:
* `getIncomingSetArity(id, type, arity, pos): arity`
* `getIncomingSet(id, type, arity, pos): [id]`
