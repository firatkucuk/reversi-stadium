
package im.firat.reversistadium.game;

import im.firat.reversistadium.exceptions.*;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

import static im.firat.reversistadium.game.Constants.*;



/**
 * Main reversi game implementation.
 */
public class ReversiGame implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    // Direction grow scan execution modes
    private static final int DGSEM_LEGAL_MOVE_SEARCH = 1;
    private static final int DGSEM_OCCUPY_SCAN       = 2;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private List<List<Integer>> boardState;
    private boolean             cancelled;
    private int                 currentPlayer;
    private boolean             started;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public ReversiGame() {

        started       = false;
        cancelled     = false;
        currentPlayer = NO_PLAYER;
        boardState    = Arrays.asList(

            //            a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3
            Arrays.<Integer>asList(0, 0, 0, 2, 1, 0, 0, 0),     // 4
            Arrays.<Integer>asList(0, 0, 0, 1, 2, 0, 0, 0),     // 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8
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

    /**
     * Provides players to move their disks.
     *
     * @param   desiredMove  desired target move in string format c4, a8
     *
     * @throws  NotStartedException   if game is not started
     * @throws  IllegalMoveException  if actual move is illegal
     */
    public synchronized void move(String desiredMove) throws NotStartedException, IllegalMoveException {

        if (!started) {
            throw new NotStartedException();
        }

        char[] chars = desiredMove.toCharArray();
        int    col   = "abcdefgh".indexOf(chars[0]);
        int    row   = "12345678".indexOf(chars[1]);

        occupyDisks(row, col);

        int otherPlayer = currentPlayer == BLACK_PLAYER ? WHITE_PLAYER : BLACK_PLAYER;

        if (haveLegalMovesForPlayer(otherPlayer)) {
            currentPlayer = otherPlayer;
        } else if (!haveLegalMovesForPlayer(currentPlayer)) {
            started       = false;
            currentPlayer = NO_PLAYER;
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Validates player order and move desired piece to target location. This method generally used by applications that
     * players (BLACK, WHITE) can simultaneously access move method.
     *
     * @param   desiredMove  desired target move in string format c4, a8
     * @param   player       current player (1 for BLACK, 2 for WHITE)
     *
     * @throws  NotStartedException   if game is not started yet
     * @throws  IllegalMoveException  if move is illegal
     * @throws  WrongOrderException   if current order is other players order
     */
    public void move(String desiredMove, int player) throws NotStartedException, IllegalMoveException,
        WrongOrderException {

        if (!started) {
            throw new NotStartedException();
        }

        if (player != currentPlayer) {
            throw new WrongOrderException();
        }

        move(desiredMove);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new Reversi game and initialize all internal variables
     *
     * @throws  AlreadyStartedException  if game is already started
     */
    @SuppressWarnings("unchecked")
    public void start() throws AlreadyStartedException {

        if (started) {
            throw new AlreadyStartedException();
        }

        started       = true;
        cancelled     = false;
        currentPlayer = BLACK_PLAYER;
        boardState    = Arrays.asList(

            //            a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3
            Arrays.<Integer>asList(0, 0, 0, 2, 1, 0, 0, 0),     // 4
            Arrays.<Integer>asList(0, 0, 0, 1, 2, 0, 0, 0),     // 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8
            );
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return Utils.getBoardStateRepresentation(boardState);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Scans all directions for valid pattern. Valid pattern:
     *
     * <pre>
        [SAME COLOR] [DIFF. COLOR] ... [DIFF. COLOR] [TARGET]
     * </pre>
     *
     * pattern rules:
     *
     * <ol>
     * <li>Target location cannot be an empty place.</li>
     * <li>Location before target location should be in different color according to current player</li>
     * <li>Pattern should start with same color according to current player</li>
     * </ol>
     *
     * @param   targetRow      row value of target location
     * @param   targetCol      col value of target location
     * @param   player         player value for pattern validation
     * @param   executionMode  1 for legal move search, 2 form occupy scan
     *
     * @return  Returns true if valid pattern found.
     */
    private boolean directionScan(int targetRow, int targetCol, int player, int executionMode) {

        if (boardState.get(targetRow).get(targetCol) != EMPTY_PLACE) {
            return false;
        }

        boolean pathFound = false;

        final int DIFFERENT_COLOR = player == WHITE_PLAYER ? BLACK_DISK : WHITE_DISK;
        final int SAME_COLOR      = player;

        for (int direction = 0; direction < 8; direction++) {

            int value = getPositionValue(targetRow, targetCol, direction, 1);

            if (value == DIFFERENT_COLOR) {

                for (int step = 2; step < 8; step++) {

                    value = getPositionValue(targetRow, targetCol, direction, step);

                    if (value == END_OF_DIRECTION || value == EMPTY_PLACE) {
                        break;
                    } else if (value == SAME_COLOR && executionMode == DGSEM_LEGAL_MOVE_SEARCH) {
                        return true;
                    } else if (value == SAME_COLOR && executionMode == DGSEM_OCCUPY_SCAN) {
                        occupyPath(targetRow, targetCol, direction, step);
                        pathFound = true;

                        break;
                    }
                } // end for
            }
        }         // end for


        return pathFound;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * There are 8 legal directions for player move. This method returns desired position value for given step and
     * directions and also returns -1 for out of bound locations.
     *
     * <pre>
             c   d   e   f
         4  [2] [0] [0] [1]  >>> direction 2
     * </pre>
     *
     * If start position is c4 and step is 3 then target location is f4 and the value is 1.
     *
     * @param   startRow   start position row. 3 for f4 location
     * @param   startCol   start position col. 5 for f4 location
     * @param   direction  direction number. Range 0 - 7
     * @param   step       translation distance
     *
     * @return  -1 for out of bound locations, 0 for empty locations, 1 for black disk, 2 for white disk
     */
    private int getPositionValue(int startRow, int startCol, int direction, int step) {

        try {
            int[] position = getTranslatedPosition(startRow, startCol, direction, step);
            int   row      = position[0];
            int   col      = position[1];

            return boardState.get(row).get(col);
        } catch (OutOfBoundsException ex) {
            return END_OF_DIRECTION;
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Returns translated position for required direction. If direction is other than range 0 - 7 then returns base
     * location.
     *
     * <pre>
             c   d   e   f
         4  [2] [0] [0] [1]  >>> direction 2
     * </pre>
     *
     * If start position is c4 and step is 3 then target location is f4. Returns [3, 5]
     *
     * @param   startRow   start position row. 3 for f4 location
     * @param   startCol   start position col. 5 for f4 location
     * @param   direction  direction number. Range 0 - 7
     * @param   step       translation distance
     *
     * @return  returns translated position index 0 is row, index 1 is col
     *
     * @throws  OutOfBoundsException
     */
    private int[] getTranslatedPosition(int startRow, int startCol, int direction, int step)
        throws OutOfBoundsException {

        int row = startRow, col = startCol;

        if (direction == 0) {
            row = startRow - step;
        } else if (direction == 1) {
            row = startRow - step;
            col = startCol + step;
        } else if (direction == 2) {
            col = startCol + step;
        } else if (direction == 3) {
            row = startRow + step;
            col = startCol + step;
        } else if (direction == 4) {
            row = startRow + step;
        } else if (direction == 5) {
            row = startRow + step;
            col = startCol - step;
        } else if (direction == 6) {
            col = startCol - step;
        } else if (direction == 7) {
            row = startRow - step;
            col = startCol - step;
        }

        if (row < 0 || col < 0 || row > 7 || col > 7) {
            throw new OutOfBoundsException();
        }

        return new int[] { row, col };
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Checks if there are legal moves for player
     *
     * @param   player  1 for black, 2 for white
     *
     * @return  returns true if there is at lease one legal move
     */
    private boolean haveLegalMovesForPlayer(int player) {

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

    /**
     * Directions
     *
     * <pre>
        7  0  1
         \ | /
          \|/
        6--+--2
          /|\
         / | \
        5  4  3
     </pre>
     *
     * @param   startRow  start position row. 3 for f4 location
     * @param   startCol  start position col. 5 for f4 location
     * @param   player    player value for legal move search
     *
     * @return  returns if move is legal or illegal
     */
    private boolean isLegalMove(int startRow, int startCol, int player) {

        return directionScan(startRow, startCol, player, DGSEM_LEGAL_MOVE_SEARCH);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Searches all directions and occupies found paths
     *
     * @param   startRow  start position row. 3 for f4 location
     * @param   startCol  start position col. 5 for f4 location
     *
     * @throws  IllegalMoveException  if move is illegal
     */
    private void occupyDisks(int startRow, int startCol) throws IllegalMoveException {

        if (!directionScan(startRow, startCol, currentPlayer, DGSEM_OCCUPY_SCAN)) {
            throw new IllegalMoveException();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Occupies path for current player
     *
     * <pre>
              c   d   e   f   g
          4  [2] [1] [1] [0] [0] >>> direction 2
     * </pre>
     *
     * After occupation: (Direction: 2, Step: 3)
     *
     * <pre>
              c   d   e   f   g
          4  [2] [2] [2] [2] [0]  >>> direction 2
     * </pre>
     *
     * @param  startRow   start position row. 3 for f4 location
     * @param  startCol   start position col. 5 for f4 location
     * @param  direction  direction number. Range 0 - 7
     * @param  step       translation distance
     */
    private void occupyPath(int startRow, int startCol, int direction, int step) {

        for (int i = 0; i < step; i++) {

            try {
                int[] position = getTranslatedPosition(startRow, startCol, direction, i);
                int   row      = position[0];
                int   col      = position[1];

                // player constants and disk constants are equivalent. So can be used "currentPlayer" for
                // current player's disk color.
                boardState.get(row).set(col, currentPlayer);
            } catch (OutOfBoundsException ex) {
                // Nothing to do
            }
        }
    }
}
