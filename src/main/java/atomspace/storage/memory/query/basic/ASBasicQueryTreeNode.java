package atomspace.storage.memory.query.basic;


import atomspace.storage.ASAtom;
import atomspace.storage.ASLink;
import atomspace.storage.ASNode;
import atomspace.storage.ASOutgoingList;

class ASBasicQueryTreeNode {

    final ASAtom atom;
    final int incomingSetPosition;
    final ASBasicQueryTreeNode parent;
    final ASBasicQueryTreeNode[] childreen;

    public ASBasicQueryTreeNode(ASBasicQueryTreeNode parent, ASAtom atom, int incomingSetPosition) {
        this.parent = parent;
        this.atom = atom;
        this.incomingSetPosition = incomingSetPosition;

        if (atom instanceof ASNode) {
            this.childreen = new ASBasicQueryTreeNode[0];
        } else {
            ASLink link = (ASLink) atom;
            ASOutgoingList outgoingList = link.getOutgoingList();
            int n = outgoingList.getSize();
            this.childreen = new ASBasicQueryTreeNode[n];

            for (int i = 0; i < n; i++) {
                this.childreen[i] = new ASBasicQueryTreeNode(this, outgoingList.getAtom(i), i);
            }
        }
    }
}
