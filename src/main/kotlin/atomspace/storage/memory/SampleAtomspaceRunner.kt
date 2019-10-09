package atomspace.storage.memory

import atomspace.storage.ASAtom
import atomspace.storage.AtomspaceStorage
import atomspace.storage.ASTransaction

class SampleAtomSpaceRunner(val atomspace: AtomspaceStorage) {

    var tx: ASTransaction? = null

    fun run(block: SampleAtomSpaceRunner.() -> Unit) {
        tx = atomspace.tx
        this.block()
        tx?.commit()
        tx?.close()
    }

    fun PersonNode(value: String): ASAtom =
            tx?.get("PersonNode", value)!!

    fun ItemNode(value: String): ASAtom =
            tx?.get("ItemNode", value)!!

    fun LikesLink(person: ASAtom, item: ASAtom): ASAtom =
            tx?.get("LikesLink", person, item)!!

    fun dump() {
        for (atom in tx?.atoms!!) {
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
