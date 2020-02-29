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
        //these will function as our new coordinates
        int newx;
        int newy;
        int status;
        char next;

        //get the current character, add it to the word
        used.setChar(x,y,'1');
        //char current = board.getChar(x,y);
        //examine all the surrounding coordinates
        for (int i = 0; i < SIZE * 2; i++) {
            newx = x + xOffset[i];
            newy = y + yOffset[i];
            //three part check:
            //make sure x is in bounds
            //make sure y is in bounds
            //make sure we haven't used this letter before
            if (isInBounds(newx, newy)) {
                //if it's a valid coordinate, check to see if adding the letter results in a word or prefix
                next = Character.toLowerCase(board.getChar(newx, newy));
                if (next != '*') {
                    word.append(next);
                    //it's a word or a prefix
                    status = dict.search((word));
                    if (status > 0) {
                        //valid word, add to dictionary
                        //minimum length is 3
                        if ((status >= 2) && (word.length() >= 3)) {
                            gameDict.add(word.toString());
                            possibleWords.add(word.toString());
                        }
                        //1 or 3 means that we have a valid prefix
                        if (!(status % 2 == 0)) {
                            used.setChar(newx, newy, '1');    //mark the character as used before passing it down the stack
                            exhaustiveSearchWildCard(newx, newy, word);
                        }
                    }
                    //to backtrack, remove the character we added and reset the character as used
                    word.deleteCharAt(word.length() - 1);
                    used.setChar(newx, newy, '0');
                }
                //if we are dealing with an asterisk
                else {
                    for (char c : ALPHABET) {
                        word.append(c);
                        //it's a word or a prefix
                        status = dict.search((word));
                        if (status > 0) {
                            //valid word, add to dictionary
                            //minimum length is 3
                            if ((status >= 2) && (word.length() >= 3)) {
                                gameDict.add(word.toString());
                                possibleWords.add(word.toString());
                            }
                            //1 or 3 means that we have a valid prefix
                            if (!(status % 2 == 0)) {
                                used.setChar(newx, newy, '1');    //mark the character as used before passing it down the stack
                                exhaustiveSearchWildCard(newx, newy, word);
                            }
                        }
                        //to backtrack, remove the character we added and reset the character as used
                        word.deleteCharAt(word.length() - 1);
                        used.setChar(newx, newy, '0');
                    }
                }
            }
        }
    }

    public static void exhaustiveSearch(int x, int y, StringBuilder word) {
        //get the current character, add it to the word
        used.setChar(x,y,'1');
        //char current = board.getChar(x,y);
        char next;
        //these will function as our new coordinates
        int newx;
        int newy;
        int status;
        //examine all the surrounding coordinates
        for (int i = 0; i < SIZE * 2; i++) {
            newx = x + xOffset[i];
            newy = y + yOffset[i];
            //three part check:
            //make sure x is in bounds
            //make sure y is in bounds
            //make sure we haven't used this letter befpre
            if(isInBounds(newx, newy)){
                //if it's a valid coordinate, check to see if adding the letter results in a word or prefix
                next = Character.toLowerCase(board.getChar(newx,newy));
                word.append(next);
                //it's a word or a prefix
                status = dict.search((word));
                if (status > 0){
                    //valid word, add to dictionary
                    //minimum length is 3
                    if ((status >= 2)&& (word.length()>=3)){
                        gameDict.add(word.toString());
                        possibleWords.add(word.toString());
                    }
                    //1 or 3 means that we have a valid prefix
                    if (!(status % 2 ==0)){
                        used.setChar(newx,newy,'1');    //mark the character as used before passing it down the stack
                        exhaustiveSearch(newx,newy,word);
                    }
                }
                //to backtrack, remove the character we added and reset the character as used
                word.deleteCharAt(word.length()-1);
                used.setChar(newx,newy,'0');
            }
        }
    }

    public static boolean isInBounds(int newX, int newY) {
        return ((newX >= 0 && newX <= 3) //make sure x is in bounds
                && (newY >= 0 && newY <= 3)) //make sure y is in bounds
                && (used.getChar(newX, newY) == '0');
    }
}