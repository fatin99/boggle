package boggle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {

    private TrieNode root;

    private static final int NOT_PREFIX_NOT_WORD = 0;
    private static final int PREFIX_NOT_WORD = 1;
    private static final int WORD_NOT_PREFIX = 2;
    private static final int PREFIX_AND_WORD = 3;

    public Dictionary() {
        root = new TrieNode();
    }

    public Dictionary(String dictionaryUri) {
        root = new TrieNode();
        try {
            BufferedReader reader = new BufferedReader(
                    new FileReader(dictionaryUri));
            String word = reader.readLine();
            while (word != null) {
                add(word);
                word = reader.readLine();
            }
            reader.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void add(String word) {
        TrieNode current = root;
        TrieNode next = null;
        for (char t: word.toCharArray()) {
            next = current.getCharList().get(t);
            if (next == null) {
                next = current.getCharList().add(t);
            }
            current = next;
        }
        next.setValue(1);
    }

    public int search(StringBuilder s) {
        TrieNode current = root;
        boolean word = false;
        boolean prefix = false;

        for (char t: s.toString().toCharArray()) {
            TrieNode next = current.getCharList().get(t);
            if (next == null) { //dead end
                return NOT_PREFIX_NOT_WORD;
            } else {
                current = next;
            }
        }

        if (current.getValue() > 0) {
            word = true;
        }
        if (current.getCharList().getSize() > 0) {
            prefix = true;
        }

        return (word ? (prefix ? PREFIX_AND_WORD : WORD_NOT_PREFIX)
                : PREFIX_NOT_WORD);
    }
}
