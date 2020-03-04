package boggle;

import static boggle.MyBoggle.ALPHABET;
import static boggle.MyBoggle.board;
import static boggle.MyBoggle.dict;
import static boggle.MyBoggle.gameDict;
import static boggle.MyBoggle.possibleWords;
import static boggle.MyBoggle.SIZE;
import static boggle.MyBoggle.used;

public class Search {

    //the arrays we'll use for the the offset (search all the surrounding cells)
    private static int[] xOffset = {-1, 0, 1, -1, 1, -1, 0, 1};
    private static int[] yOffset = {-1, -1, -1, 0, 0, 1, 1, 1};

    private static final int MIN_LENGTH = 3;

    public static void exhaustiveSearchWildCard(int x, int y, StringBuilder word) {
        used.setChar(x, y, '1'); //get the current character, add it to the word
        //these will function as our new coordinates
        int newX;
        int newY;
        //examine all the surrounding coordinates
        for (int i = 0; i < SIZE * 2; i++) {
            newX = x + xOffset[i];
            newY = y + yOffset[i];
            if (isUnusedLetter(newX, newY)) {
                char next = Character.toLowerCase(board.getChar(newX, newY));
                if (next != '*') {
                    word.append(next);
                    checkWord(newX, newY, word, true);
                    backTrack(newX, newY, word);
                } else { //if we are dealing with a wildcard
                    for (char c : ALPHABET) {
                        word.append(c);
                        checkWord(newX, newY, word, true);
                        backTrack(newX, newY, word);
                    }
                }
            }
        }
    }

    public static void exhaustiveSearch(int x, int y, StringBuilder word) {
        used.setChar(x, y, '1'); //get the current character, add it to the word
        //these will function as our new coordinates
        int newX;
        int newY;
        //examine all the surrounding coordinates
        for (int i = 0; i < SIZE * 2; i++) {
            newX = x + xOffset[i];
            newY = y + yOffset[i];
            if (isUnusedLetter(newX, newY)) {
                word.append(Character.toLowerCase(board.getChar(newX, newY)));
                checkWord(newX, newY, word, false);
                backTrack(newX, newY, word);
            }
        }
    }

    public static boolean isUnusedLetter(int newX, int newY) {
        return ((newX >= 0 && newX <= (SIZE - 1)) //make sure x is in bounds
                && (newY >= 0 && newY <= (SIZE - 1))) //make sure y is in bounds
                //make sure letter is unused
                && (used.getChar(newX, newY) == '0');
    }

    public static void backTrack(int newX, int newY, StringBuilder word) {
        //remove the character we added and reset the character as used
        word.deleteCharAt(word.length() - 1);
        used.setChar(newX, newY, '0');
    }

    public static void checkWord(int newX, int newY, StringBuilder word, boolean hasWildCard) {
        int status = dict.search((word));
        //check to see if adding the letter results in a word or prefix
        if (status > 0) { //it's a word or a prefix
            //minimum word length is 3
            if ((status >= 2) && (word.length() >= MIN_LENGTH)) {
                gameDict.add(word.toString()); //valid word, add to dictionary
                possibleWords.add(word.toString());
            }
            if (!(status % 2 == 0)) { //1 or 3 means that we have a valid prefix
                //mark the character as used before passing it down the stack
                used.setChar(newX, newY, '1');
                if (!hasWildCard) {
                    exhaustiveSearch(newX, newY, word);
                } else {
                    exhaustiveSearchWildCard(newX, newY, word);
                }
            }
        }
    }
}
