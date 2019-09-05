package atomspace.storage.memory.query.basic;


import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.ASOutgoingList;

class ASBasicQueryTreeNode {

    final ASAtom atom;
    final ASBasicQueryTreeNode parent;
    boolean parentVisited = false;
    final boolean[] childrenVisited;
    final ASBasicQueryTreeNode[] childreen;

    public ASBasicQueryTreeNode(ASAtom atom, ASBasicQueryTreeNode parent) {
        this.atom = atom;
        this.parent = parent;

        if (atom instanceof ASNode) {
            this.childrenVisited = new boolean[0];
            this.childreen = new ASBasicQueryTreeNode[0];
        } else {
            ASLink link = (ASLink) atom;
            ASOutgoingList outgoingList = link.getOutgoingList();
            int n = outgoingList.getSize();
            this.childrenVisited = new boolean[n];
            this.childreen = new ASBasicQueryTreeNode[n];

            for (int i = 0; i < n; i++) {
                this.childreen[i] = new ASBasicQueryTreeNode(outgoingList.getAtom(i), this);
            }
        }
    }
}
