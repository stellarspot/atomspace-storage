package atomspace.storage.memory

import atomspace.storage.ASAtom
import atomspace.storage.AtomspaceStorage

class SampleAtomSpaceRunner(val atomspace: AtomspaceStorage) {

    fun run(block: SampleAtomSpaceRunner.() -> Unit) {
        this.block()
    }

    fun PersonNode(value: String): ASAtom =
            atomspace.get("PersonNode", value)

    fun ItemNode(value: String): ASAtom =
            atomspace.get("ItemNode", value)

    fun LikesLink(person: ASAtom, item: ASAtom): ASAtom =
            atomspace.get("LikesLink", person, item)

    fun dump() {
        for (atom in atomspace.atoms) {
            println(atom)
        }
    }
}

fun main() {

    val atomspaceRunner = SampleAtomSpaceRunner(AtomspaceMemoryStorage())

    atomspaceRunner.run {
        LikesLink(
                PersonNode("Alice"),
                ItemNode("ice-cream")
        )
    }

    atomspaceRunner.dump()
}
