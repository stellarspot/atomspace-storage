package atomspace.sample

import atomspace.runner.*
import atomspace.storage.ASAtom
import atomspace.storage.AtomspaceStorage
import atomspace.storage.memory.AtomspaceMemoryStorage

class SampleAtomspaceRunner(override val atomspace: AtomspaceStorage) : AtomspaceRunner(atomspace) {

    fun PersonNode(value: String): ASAtom =
            tx.get("PersonNode", value)

    fun ItemNode(value: String): ASAtom =
            tx.get("ItemNode", value)

    fun LikesLink(person: ASAtom, item: ASAtom): ASAtom =
            tx.get("LikesLink", person, item)

    fun DislikesLink(person: ASAtom, item: ASAtom): ASAtom =
            tx.get("DislikesLink", person, item)
}

fun main() {

    val runner = SampleAtomspaceRunner(AtomspaceMemoryStorage())

    run(runner) {

        LikesLink(
                PersonNode("Alice"),
                ItemNode("apple")
        )

        LikesLink(
                PersonNode("Alice"),
                ItemNode("orange")
        )

        DislikesLink(
                PersonNode("Alice"),
                ItemNode("pear")
        )

        LikesLink(
                PersonNode("Bob"),
                ItemNode("apple")
        )
    }

    val results = query(runner) {
        LikesLink(
                PersonNode("Alice"),
                VariableNode("WHAT")
        )
    }

    results.variables("WHAT").nodes().forEach {
        println("Alice likes ${it.value}")
    }
}
