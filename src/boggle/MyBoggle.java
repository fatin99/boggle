package boggle;

import java.io.IOException;
import java.util.Scanner;
import java.util.TreeSet;

import static boggle.Search.exhaustiveSearch;
import static boggle.Search.exhaustiveSearchWildCard;

public class MyBoggle {

    private static final String DICTIONARY_URI = "data/dictionary.txt";
    static final int SIZE = 4;
    static final char[] ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z'};

    static TreeSet<String> possibleWords = new TreeSet<>();
    static Board board;
    static Board used = new Board(); //this will keep track of whether we have used a letter or not
    static Dictionary dict = new Dictionary(DICTIONARY_URI);
    //this is the main dictionary into which we will load words from the txt file
    static Dictionary gameDict = new Dictionary();
    //this is the game dictionary; when we search for words on the board they go here.
    private static Scanner sc;

    //scoring variables
    private static int correct = 0;
    private static int incorrect = 0;
    private static int possible = 0;
    private static long start = 0;
    private static long end = 0;
    private static long curr = 0;
    private static long limit = 0;

    public static void main(String[] args) {
        System.out.println("ENTER LOCATION OF BOARD FILE OR 'random' TO GENERATE A RANDOM BOARD.");
        sc = new Scanner(System.in);
        String boardUri = sc.nextLine();
        System.out.println("ENTER THE DURATION OF THE GAME IN MILLI SECONDS.");
        limit =  sc.nextInt();

        //load board
        try {
            board = new Board(boardUri);
            board.printBoard();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //exhaustively search board for words
        search();
        //prompt the user to enter as many words as they can find in that board
        System.out.println("Enter as many words are you can find in the board.");
        System.out.println("Type the word and hit enter. Enter 0 when finished.");
        start = System.currentTimeMillis();
        end = start + limit;
        play(sc);
        end();
        end = System.currentTimeMillis();
        System.out.println("Total time spent = " + (end - start) + " milli seconds.");
    }

    public static void play(Scanner sc) {
        TreeSet<String> correctWords = new TreeSet<>();
        sc.nextLine();
        StringBuilder input = new StringBuilder(sc.nextLine().toLowerCase());

        while (!input.toString().equals("0")) {
            if (gameDict.search(input) >= 2) {
                if (correctWords.contains(input.toString())) {
                    //already guessed
                    printTime("THIS WORD HAS ALREADY BEEN GUESSED");
                } else {
                    //correct word
                    printTime("VALID WORD");
                    correct++;
                    correctWords.add(input.toString());
                }
            } else {
                //incorrect word
                printTime("INVALID WORD");
                incorrect++;
            }
            if ((end - curr) <= 0) {
                System.out.println("TIME IS UP.");
                break;
            }
            input = new StringBuilder(sc.nextLine().toLowerCase());
        }
        sc.close();
    }

    public static void end() {
        //after user indicates they have finished entering words
        System.out.println("Possible words");
        for (String s : possibleWords) {
            System.out.println(s);
            possible++;
        }
        System.out.printf("# of correct words: %d\n# of incorrect words: %d\n"
                + "# of possible words: %d\n", correct, incorrect, possible);
    }

    public static void search() {
        StringBuilder word = new StringBuilder();
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                if(!(board.isHasWildcard())) {
                    searchWildcard(word, i, j, false);
                }
                else {
                    if(board.getChar(i,j) != '*'){
                        searchWildcard(word, i, j, true);
                    }
                    else{
                       for(char c : ALPHABET){
                           board.setChar(i,j,c);
                           searchWildcard(word, i, j, true);
                       }
                        board.setChar(i,j,'*');
                    }
                }
            }
        }
    }

    public static void searchWildcard(StringBuilder word, int i, int j, boolean hasWildcard) {
        used.clearBoard();
        word.append(Character.toLowerCase(board.getChar(i, j)));
        if (hasWildcard) {
            exhaustiveSearchWildCard(i, j, word);
        } else {
            exhaustiveSearch(i, j, word);
        }
        word.deleteCharAt(word.length() - 1);
    }

    public static void printTime(String message) {
        curr = System.currentTimeMillis();
        System.out.println(message + "\nTIME ELAPSED: " + (curr - start)
                + " milli seconds.\nTIME REMAINING: " + (end - curr)
                + " milli seconds.");
    }
}
