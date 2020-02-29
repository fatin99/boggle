package boggle;

public class TrieNode {

    private int value;
    private CharList charList;

    public TrieNode() {
        value = 0;
        charList = new CharList();
    }

    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public CharList getCharList() {
        return this.charList;
    }
}