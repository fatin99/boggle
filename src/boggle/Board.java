package boggle;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import static boggle.MyBoggle.SIZE;

public class Board {

    private char[][] boardArray;
    private boolean hasWildcard;
    private final char[] ALPHABET = {'a', 'b', 'c', 'd', 'e', 'f', 'g',
            'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't',
            'u', 'v', 'w', 'x', 'y', 'z', '*'};

    public Board() {
        this.boardArray = new char[SIZE][SIZE];
    }

    public Board(String uri) throws IOException {
        this.boardArray = new char[SIZE][SIZE];
        if (uri.equals("random")) {
            Random rnd = new Random();
            for (int j = 0; j < SIZE; j++) {
                for (int k = 0; k < SIZE; k++) {
                    boardArray[j][k] = ALPHABET[rnd.nextInt(27)];
                }
            }
        } else {
            File boardFile = new File(uri);
            FileReader reader = new FileReader(new File(uri));
            char[] letters = new char[(int) boardFile.length()];
            reader.read(letters);
            loadBoard(letters);
            reader.close();
        }
    }

    public void clearBoard() {
        for (int i = 0; i < SIZE; i++) {
            for (int j = 0; j < SIZE; j++) {
                boardArray[i][j] = '0'; //boolean false
            }
        }
    }

    public char getChar(int x, int y) {
        return boardArray[x][y];
    }

    public void setChar(int x, int y, char input) {
        boardArray[x][y] = input;
    }

    public void printBoard() {
        System.out.println("_________________");
        for (int i = 0; i < SIZE; i++) {
            System.out.print("| ");
            for (int j = 0; j < SIZE; j++) {
                System.out.print(boardArray[i][j]);
                System.out.print(" | ");
            }
            System.out.print("\n");
        }
        System.out.println("_________________");
    }

    public void loadBoard(char[] inputBoard) {
        ArrayList<Character> cleanBoard = new ArrayList<>();
        for (char c : inputBoard) {
            if (c != ',' && c != ' ') {
                cleanBoard.add(c);
            }
        }

        hasWildcard = false;
        int i = 0;
        System.out.println();
        for (int j = 0; j < SIZE; j++) {
            for (int k = 0; k < SIZE; k++) {
                if (cleanBoard.get(i) == '*') {
                    hasWildcard = true;
                }
                boardArray[j][k] = cleanBoard.get(i);
                i++;
            }
        }
    }

    public boolean isHasWildcard() {
        return hasWildcard;
    }
}
