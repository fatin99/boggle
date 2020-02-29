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

    public static void exhaustiveSearchWildCard(int x, int y, StringBuilder word) {
        used.setChar(x,y,'1'); //get the current character, add it to the word
        //these will function as our new coordinates
        int newx;
        int newy;
        //examine all the surrounding coordinates
        for (int i = 0; i < SIZE * 2; i++) {
            newx = x + xOffset[i];
            newy = y + yOffset[i];
            if (isUnusedLetter(newx, newy)) {
                char next = Character.toLowerCase(board.getChar(newx, newy));
                if (next != '*') {
                    word.append(next);
                    checkWord(newx, newy, word, true);
                    backTrack(newx, newy, word);
                } else { //if we are dealing with a wildcard
                    for (char c : ALPHABET) {
                        word.append(c);
                        checkWord(newx, newy, word, true);
                        backTrack(newx, newy, word);
                    }
                }
            }
        }
    }

    public static void exhaustiveSearch(int x, int y, StringBuilder word) {
        used.setChar(x,y,'1'); //get the current character, add it to the word
        //these will function as our new coordinates
        int newx;
        int newy;
        //examine all the surrounding coordinates
        for (int i = 0; i < SIZE * 2; i++) {
            newx = x + xOffset[i];
            newy = y + yOffset[i];
            if(isUnusedLetter(newx, newy)){
                word.append(Character.toLowerCase(board.getChar(newx,newy)));
                checkWord(newx, newy, word, false);
                backTrack(newx, newy, word);
            }
        }
    }

    public static boolean isUnusedLetter(int newX, int newY) {
        return ((newX >= 0 && newX <= 3) //make sure x is in bounds
                && (newY >= 0 && newY <= 3)) //make sure y is in bounds
                && (used.getChar(newX, newY) == '0'); //make sure letter is unused
    }

    public static void backTrack(int newx, int newy, StringBuilder word) {
        //to backtrack, remove the character we added and reset the character as used
        word.deleteCharAt(word.length() - 1);
        used.setChar(newx, newy, '0');
    }

    public static void checkWord(int newx, int newy, StringBuilder word, boolean hasWildCard) {
        int status = dict.search((word));
        //check to see if adding the letter results in a word or prefix
        if (status > 0){ //it's a word or a prefix
            if ((status >= 2)&& (word.length()>=3)){ //minimum word length is 3
                gameDict.add(word.toString()); //valid word, add to dictionary
                possibleWords.add(word.toString());
            }
            //1 or 3 means that we have a valid prefix
            if (!(status % 2 ==0)){
                used.setChar(newx,newy,'1'); //mark the character as used before passing it down the stack
                if (!hasWildCard) {
                    exhaustiveSearch(newx, newy, word);
                } else {
                    exhaustiveSearchWildCard(newx, newy, word);
                }
            }
        }
    }
}