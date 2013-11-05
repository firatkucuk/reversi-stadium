
package im.firat.reversistadium.game;

import im.firat.reversistadium.exceptions.*;
import java.io.Serializable;
import java.util.*;

import static im.firat.reversistadium.game.Constants.*;



/**
 * Simple Reversi Game implementation.
 */
public class ReversiGame implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final long serialVersionUID = 1L;

    private static final String BOARD_LETTERS = "abcdefgh";
    private static final String BOARD_NUMBERS = "12345678";
    private static final char[] BOARD_CHARS   = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    /**
     * <pre>
        {
           1: { // Found paths for black player
              'e1': { // Path Object
                'targetRow': 0,
                'targetCol': 4,
                'direction': 0,
                'step'     : 2
              }
           },
           2: { // Found paths for white player
              'a5': { // Path Object
                'targetRow': 4,
                'targetCol': 0,
                'direction': 6,
                'step'     : 2
              }
           }
        }
     * </pre>
     */
    private Map<Integer, Map<String, List<Path>>> availablePaths;
    private List<List<Integer>>                   boardState;
    private boolean                               cancelled;
    private int                                   currentPlayer;
    private boolean                               started;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public ReversiGame() {

        started       = false;
        cancelled     = false;
        currentPlayer = NO_PLAYER;
        boardState    = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 2, 1, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 1, 2, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    /**
     * Cancels running game, saves board state and current player until next game start
     *
     * @throws  NotStartedException  If there is no running game
     */
    public void cancel() throws NotStartedException {

        if (!started) {
            throw new NotStartedException();
        }

        started        = false;
        cancelled      = true;
        availablePaths = null;
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
        int    col   = BOARD_LETTERS.indexOf(chars[0]);
        int    row   = BOARD_NUMBERS.indexOf(chars[1]);

        occupyPaths(row, col);
        updateAvailablePaths();

        int otherPlayer = currentPlayer == BLACK_PLAYER ? WHITE_PLAYER : BLACK_PLAYER;

        if (haveLegalMovesForPlayer(otherPlayer)) {
            currentPlayer = otherPlayer;
        } else if (!haveLegalMovesForPlayer(currentPlayer)) {
            started        = false;
            currentPlayer  = NO_PLAYER;
            availablePaths = null;
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

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 2, 1, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 1, 2, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );

        updateAvailablePaths();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return Utils.getBoardStateRepresentation(boardState);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Converts given location (row, col) to human readable board text
     *
     * @param   row  row value of given location
     * @param   col  column value of given location
     *
     * @return  returns human readable location text. Converts (1, 7) to h2
     */
    private String convertLocationToText(int row, int col) {

        StringBuilder text = new StringBuilder();
        text.append(BOARD_CHARS[col]);
        text.append(row + 1);

        return text.toString();
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
     * @param   targetRow  row value of target location
     * @param   targetCol  col value of target location
     * @param   player     player value for pattern validation
     *
     * @return  Returns found path if valid pattern found.
     */
    private List<Path> findAvailablePaths(int targetRow, int targetCol, int player) {

        if (boardState.get(targetRow).get(targetCol) != EMPTY_PLACE) {
            return null;
        }

        List<Path> result = new ArrayList<Path>();

        final int DIFFERENT_COLOR = player == WHITE_PLAYER ? BLACK_DISK : WHITE_DISK;
        final int SAME_COLOR      = player;

        for (int direction = 0; direction < 8; direction++) {

            int value = getPositionValue(targetRow, targetCol, direction, 1);

            if (value == DIFFERENT_COLOR) {

                for (int step = 2; step < 8; step++) {

                    value = getPositionValue(targetRow, targetCol, direction, step);

                    if (value == END_OF_DIRECTION || value == EMPTY_PLACE) {
                        break;
                    } else if (value == SAME_COLOR) {
                        result.add(new Path(targetRow, targetCol, direction, step));

                        break;
                    }
                } // end for
            }
        }         // end for

        return result;
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
     * @param   row        start position row. 3 for f4 location
     * @param   col        start position col. 5 for f4 location
     * @param   direction  direction number. Range 0 - 7
     * @param   step       translation distance
     *
     * @return  -1 for out of bound locations, 0 for empty locations, 1 for black disk, 2 for white disk
     */
    private int getPositionValue(int row, int col, int direction, int step) {

        try {
            int[] position      = getTranslatedPosition(row, col, direction, step);
            int   translatedRow = position[0];
            int   translatedCol = position[1];

            return boardState.get(translatedRow).get(translatedCol);
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
     * @param   targetRow  start position row. 3 for f4 location
     * @param   targetCol  start position col. 5 for f4 location
     * @param   direction  direction number. Range 0 - 7
     * @param   step       translation distance
     *
     * @return  returns translated position index 0 is row, index 1 is col
     *
     * @throws  OutOfBoundsException
     */
    private int[] getTranslatedPosition(int targetRow, int targetCol, int direction, int step)
        throws OutOfBoundsException {

        int row = targetRow, col = targetCol;

        if (direction == 0) {
            row = targetRow - step;
        } else if (direction == 1) {
            row = targetRow - step;
            col = targetCol + step;
        } else if (direction == 2) {
            col = targetCol + step;
        } else if (direction == 3) {
            row = targetRow + step;
            col = targetCol + step;
        } else if (direction == 4) {
            row = targetRow + step;
        } else if (direction == 5) {
            row = targetRow + step;
            col = targetCol - step;
        } else if (direction == 6) {
            col = targetCol - step;
        } else if (direction == 7) {
            row = targetRow - step;
            col = targetCol - step;
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

        Map<String, List<Path>> playerPaths = availablePaths.get(player);

        if (playerPaths.size() > 0) {
            return true;
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
     * @param   targetRow  start position row. 3 for f4 location
     * @param   targetCol  start position col. 5 for f4 location
     * @param   player     player value for legal move search
     *
     * @return  returns if move is legal or illegal
     */
    private boolean isLegalMove(int targetRow, int targetCol, int player) {

        if (availablePaths != null && !availablePaths.isEmpty()) {

            Map<String, List<Path>> playerPaths = availablePaths.get(player);

            if (playerPaths.containsKey(convertLocationToText(targetRow, targetCol))) {
                return true;
            }
        }

        return false;
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
     * @param  path  found path
     */
    private void occupyPath(Path path) {

        occupyPath(path.getTargetRow(), path.getTargetCol(), path.getDirection(), path.getStep());
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
     * @param  targetRow  start position row. 3 for f4 location
     * @param  targetCol  start position col. 5 for f4 location
     * @param  direction  direction number. Range 0 - 7
     * @param  step       translation distance
     */
    private void occupyPath(int targetRow, int targetCol, int direction, int step) {

        for (int i = 0; i < step; i++) {

            try {
                int[] position = getTranslatedPosition(targetRow, targetCol, direction, i);
                int   row      = position[0];
                int   col      = position[1];

                // player constants and disk constants are equivalent. So can be used "currentPlayer" for
                // current player's disk color.
                boardState.get(row).set(col, currentPlayer);
            } catch (OutOfBoundsException ex) {
                break;
            }
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Occupies all available paths that current player has for target location
     *
     * @param   targetRow  start position row. 3 for f4 location
     * @param   targetCol  start position col. 5 for f4 location
     *
     * @throws  IllegalMoveException  if move is illegal
     */
    private void occupyPaths(int targetRow, int targetCol) throws IllegalMoveException {

        if (availablePaths != null && !availablePaths.isEmpty()) {

            Map<String, List<Path>> playerPaths = availablePaths.get(currentPlayer);
            List<Path>              foundPaths  = playerPaths.get(convertLocationToText(targetRow, targetCol));

            if (foundPaths != null && !foundPaths.isEmpty()) {

                for (Path foundPath : foundPaths) {
                    occupyPath(foundPath);
                }

            } else {
                throw new IllegalMoveException();
            }
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * scans all available paths for all players than updates availablePaths filed.
     */
    private void updateAvailablePaths() {

        availablePaths = new HashMap<Integer, Map<String, List<Path>>>(2);

        for (int player = 1; player <= 2; player++) {

            Map<String, List<Path>> playerPaths = new HashMap<String, List<Path>>();
            availablePaths.put(player, playerPaths);

            for (int i = 0; i < 8; i++) {

                for (int j = 0; j < 8; j++) {
                    List<Path> paths = findAvailablePaths(i, j, player);

                    if (paths != null && paths.size() > 0) {
                        playerPaths.put(convertLocationToText(i, j), paths);
                    }
                }
            }
        }
    }



    //~ --- [INNER CLASSES] --------------------------------------------------------------------------------------------

    private class Path {



        //~ --- [INSTANCE FIELDS] --------------------------------------------------------------------------------------

        private int direction;
        private int step;
        private int targetCol;
        private int targetRow;



        //~ --- [CONSTRUCTORS] -----------------------------------------------------------------------------------------

        private Path(int targetRow, int targetCol, int direction, int step) {

            this.targetRow = targetRow;
            this.targetCol = targetCol;
            this.direction = direction;
            this.step      = step;
        }



        //~ --- [METHODS] ----------------------------------------------------------------------------------------------

        private int getDirection() {

            return direction;
        }



        //~ ------------------------------------------------------------------------------------------------------------

        private int getStep() {

            return step;
        }



        //~ ------------------------------------------------------------------------------------------------------------

        private int getTargetCol() {

            return targetCol;
        }



        //~ ------------------------------------------------------------------------------------------------------------

        private int getTargetRow() {

            return targetRow;
        }
    }
}
