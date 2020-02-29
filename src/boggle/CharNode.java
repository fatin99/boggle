package boggle;

public class CharNode {

    private char value;
    private TrieNode node;
    private CharNode next;

    public CharNode(char value) {
        this.value = value;
        this.node = new TrieNode();
    }

    public char getValue() {
        return this.value;
    }

    public TrieNode getNode() {
        return this.node;
    }

    public CharNode getNext() {
        return this.next;
    }

    public void setNext(CharNode next) {
        this.next = next;
    }
}
