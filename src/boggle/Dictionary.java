package boggle;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class Dictionary {

    private TrieNode root;

    public Dictionary() {
        root = new TrieNode();
    }

    public Dictionary(String dictionaryUri) {
        root = new TrieNode();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dictionaryUri));
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
            if (next == null) { //dead end, not a word or prefix
                return 0;
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

        //1 => prefix not word
        //2 => word not prefix
        //3 => both
        return (word ? (prefix ? 3 : 2) : 1);
    }
}
