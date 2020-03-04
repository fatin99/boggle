package boggle;

public class CharList {

    private CharNode first;
    private int size;

    public TrieNode add(char newChar) {
        CharNode newNode = new CharNode(newChar);

        if (first == null) {
            //case 1: first is null
            first = newNode;
            size++;

        } else if (newChar < first.getValue()) {
            //case 2: newNode is before first
            newNode.setNext(first);
            first = newNode;
            size++;

        } else if (newChar > first.getValue()) {
            //case 3: newNode is after first
            CharNode current = first;
            boolean inserted = false;

            while (!inserted) {
                if (current.getNext() == null) {
                    //case 4: there is no next node
                    current.setNext(newNode);
                    inserted = true;
                    size++;

                } else if (current.getValue() > newChar) {
                    //case 5: newNode goes after current and before next
                    newNode.setNext(current.getNext());
                    current.setNext(newNode);
                    inserted = true;
                    size++;

                } else if (current.getValue() == newChar) {
                    //case 6: newNode is next
                    inserted = true;

                } else {
                    //case 7: newNode is after next
                    inserted = false;
                    current = current.getNext();
                }
            }
        }
        return newNode.getNode();
    }

    public TrieNode get(char val) {
        TrieNode result = null;
        CharNode current = first;

        while (current != null) {
            if (current.getValue() == val) {
                result = current.getNode();
                break;
            } else {
                current = current.getNext();
            }
        }
        return result;
    }

    public int getSize() {
        return this.size;
    }
}
