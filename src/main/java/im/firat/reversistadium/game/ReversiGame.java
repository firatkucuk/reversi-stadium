
package im.firat.reversistadium.game;

import im.firat.reversistadium.exceptions.AlreadyStartedException;
import im.firat.reversistadium.exceptions.IllegalMoveException;
import im.firat.reversistadium.exceptions.NotStartedException;
import im.firat.reversistadium.exceptions.WrongOrderException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static im.firat.reversistadium.game.Constants.*;



public class ReversiGame implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private List<List<Integer>> boardState;
    private boolean             cancelled;
    private int                 currentPlayer;
    private boolean             started;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public ReversiGame() {

        started       = false;
        cancelled     = false;
        currentPlayer = NO_PLAYER;
        boardState    = Arrays.asList(

            //            a  b  c  d  e  f  g  h
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3
            Arrays.asList(0, 0, 0, 2, 1, 0, 0, 0),     // 4
            Arrays.asList(0, 0, 0, 1, 2, 0, 0, 0),     // 5
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8
            );
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public void cancel() throws NotStartedException {

        if (!started) {
            throw new NotStartedException();
        }

        started   = false;
        cancelled = true;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public List<List<Integer>> getBoardState() {

        return boardState;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int getCurrentPlayer() {

        return currentPlayer;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public boolean isCancelled() {

        return cancelled;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public boolean isStarted() {

        return started;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public synchronized void move(String piece, int player) throws NotStartedException, IllegalMoveException,
        WrongOrderException {

        if (!started) {
            throw new NotStartedException();
        }

        if (player != currentPlayer) {
            throw new WrongOrderException();
        }

        char[] chars = piece.toCharArray();
        int    col   = "abcdefgh".indexOf(chars[0]);
        int    row   = "12345678".indexOf(chars[1]);

        occupyDisks(row, col);

        int otherPlayer = currentPlayer == BLACK_PLAYER ? WHITE_PLAYER : BLACK_PLAYER;

        if (haveLegalMovesForPlayer(otherPlayer)) {
            currentPlayer = otherPlayer;
        } else if (haveLegalMovesForPlayer(currentPlayer)) {
            // Nothing to do
        } else {
            started       = false;
            currentPlayer = NO_PLAYER;
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void start() throws AlreadyStartedException {

        if (started) {
            throw new AlreadyStartedException();
        }

        started       = true;
        cancelled     = false;
        currentPlayer = BLACK_PLAYER;
        boardState    = Arrays.asList(

            //            a  b  c  d  e  f  g  h
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3
            Arrays.asList(0, 0, 0, 2, 1, 0, 0, 0),     // 4
            Arrays.asList(0, 0, 0, 1, 2, 0, 0, 0),     // 5
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7
            Arrays.asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8
            );
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private int getPositionValue(int startRow, int startCol, int direction, int step) {

        int[] position = getTranslatedPosition(startRow, startCol, direction, step);
        int   row      = position[0];
        int   col      = position[1];

        if (row < 0 || col < 0 || row > 7 || col > 7) {
            return END_OF_DIRECTION;
        }

        return boardState.get(row).get(col);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private int[] getTranslatedPosition(int startRow, int startCol, int direction, int step) {

        int row = startRow, col = startCol;

        if (direction == 0) {
            row = startRow - step;
        }

        if (direction == 1) {
            row = startRow - step;
            col = startCol + step;
        }

        if (direction == 2) {
            col = startCol + step;
        }

        if (direction == 3) {
            row = startRow + step;
            col = startCol + step;
        }

        if (direction == 4) {
            row = startRow + step;
        }

        if (direction == 5) {
            row = startRow + step;
            col = startCol - step;
        }

        if (direction == 6) {
            col = startCol - step;
        }

        if (direction == 7) {
            row = startRow - step;
            col = startCol - step;
        }

        return new int[] { row, col };
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private boolean haveLegalMovesForPlayer(int player) throws IllegalMoveException {

        for (int i = 0; i < 8; i++) {

            for (int j = 0; j < 8; j++) {

                if (isLegalMove(i, j, player)) {
                    return true;
                }
            }
        }

        return false;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private boolean isLegalMove(int row, int col, int player) throws IllegalMoveException {

        if (boardState.get(row).get(col) != EMPTY_PLACE) {
            throw new IllegalMoveException();
        }

        // Directions
        //    7  0  1
        //     \ | /
        //      \|/
        //    6--+--2
        //      /|\
        //     / | \
        //    5  4  3

        int[] directionStates = {
            DS_START, DS_START,
            DS_START, DS_START,
            DS_START, DS_START,
            DS_START, DS_START
        };

        final int DIFFERENT_COLOR = player == WHITE_PLAYER ? BLACK_DISK : WHITE_DISK;

        for (int step = 1; step < 8; step++) {

            for (int direction = 0; direction < 8; direction++) {

                int directionState = directionStates[direction];

                if (directionState == DS_NO_VALID_PATH) {
                    // Nothing to do
                } else {
                    int directionValue = getPositionValue(row, col, direction, step);

                    if (directionState == DS_START) {

                        if (directionValue == DIFFERENT_COLOR) {
                            directionStates[direction] = DS_WAITING_FOR_SAME_COLOR;
                        } else {
                            directionStates[direction] = DS_NO_VALID_PATH;
                        }
                    } else if (directionState == DS_WAITING_FOR_SAME_COLOR) {

                        if (directionValue == END_OF_DIRECTION || directionValue == EMPTY_PLACE) {
                            directionStates[direction] = DS_NO_VALID_PATH;
                        } else if (directionValue == DIFFERENT_COLOR) {
                            // Nothing to do
                        } else {
                            return true;
                        }
                    }
                }
            } // end for
        }     // end for

        return false;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void occupyDisks(int row, int col) throws IllegalMoveException {

        if (boardState.get(row).get(col) != EMPTY_PLACE) {
            throw new IllegalMoveException();
        }

        // Directions
        //    7  0  1
        //     \ | /
        //      \|/
        //    6--+--2
        //      /|\
        //     / | \
        //    5  4  3

        boolean occupied        = false;
        int[]   directionStates = {
            DS_START, DS_START,
            DS_START, DS_START,
            DS_START, DS_START,
            DS_START, DS_START
        };

        final int DIFFERENT_COLOR = currentPlayer == WHITE_PLAYER ? BLACK_DISK : WHITE_DISK;

        for (int step = 1; step < 8; step++) {

            for (int direction = 0; direction < 8; direction++) {

                int directionState = directionStates[direction];

                if (directionState == DS_NO_VALID_PATH || directionState == DS_OCCUPIED) {
                    // Nothing to do
                } else {
                    int directionValue = getPositionValue(row, col, direction, step);

                    if (directionState == DS_START) {

                        if (directionValue == DIFFERENT_COLOR) {
                            directionStates[direction] = DS_WAITING_FOR_SAME_COLOR;
                        } else {
                            directionStates[direction] = DS_NO_VALID_PATH;
                        }
                    } else if (directionState == DS_WAITING_FOR_SAME_COLOR) {

                        if (directionValue == END_OF_DIRECTION || directionValue == EMPTY_PLACE) {
                            directionStates[direction] = DS_NO_VALID_PATH;
                        } else if (directionValue == DIFFERENT_COLOR) {
                            // Nothing to do
                        } else {
                            occupyPath(row, col, step, direction);

                            directionStates[direction] = DS_OCCUPIED;
                            occupied                   = true;
                        }
                    }
                } // end if-else
            }     // end for
        }         // end for

        if (!occupied) {
            throw new IllegalMoveException();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void occupyPath(int row, int col, int step, int direction) {

        for (int i = 0; i < step; i++) {
            setPositionValue(row, col, direction, i, currentPlayer);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private void setPositionValue(int startRow, int startCol, int direction, int step, int value) {

        int[] position = getTranslatedPosition(startRow, startCol, direction, step);
        int   row      = position[0];
        int   col      = position[1];

        if (row < 0 || col < 0 || row > 7 || col > 7) {
            // Nothing to do
        } else {
            boardState.get(row).set(col, value);
        }
    }
}
