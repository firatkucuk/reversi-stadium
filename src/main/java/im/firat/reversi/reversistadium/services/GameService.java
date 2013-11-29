
package im.firat.reversi.reversistadium.services;


import im.firat.reversi.reversistadium.beans.Path;
import im.firat.reversi.reversistadium.domain.Game;
import im.firat.reversi.reversistadium.exceptions.*;
import java.util.*;



public final class GameService {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    public static final int END_OF_DIRECTION = -1;

    // Board square states
    public static final int EMPTY_PLACE = 0;
    public static final int BLACK_DISK  = 1;
    public static final int WHITE_DISK  = 2;

    // Players
    public static final int NO_PLAYER    = 0;
    public static final int BLACK_PLAYER = 1;
    public static final int WHITE_PLAYER = 2;

    // In class global constants
    private static final String BOARD_LETTERS = "abcdefgh";
    private static final String BOARD_NUMBERS = "12345678";
    private static final char[] BOARD_CHARS   = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameService() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public void cancel(final Game game) throws NotStartedException {

        if (!game.isStarted()) {
            throw new NotStartedException();
        }

        game.setStarted(false);
        game.setCancelled(true);
        game.setAvailablePaths(null);
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
    public List<Path> findAvailablePaths(final Game game, final int targetRow, final int targetCol, final int player) {

        final List<List<Integer>> boardState = game.getBoardState();

        if (boardState.get(targetRow).get(targetCol) != EMPTY_PLACE) {
            return null;
        }

        List<Path> result = new ArrayList<Path>();

        final int DIFFERENT_COLOR = player == WHITE_PLAYER ? BLACK_DISK : WHITE_DISK;
        final int SAME_COLOR      = player; // Redundant constant for code readability

        for (int direction = 0; direction < 8; direction++) {

            int value = getPositionValue(game, targetRow, targetCol, direction, 1);

            if (value == DIFFERENT_COLOR) {

                for (int step = 2; step < 8; step++) {

                    value = getPositionValue(game, targetRow, targetCol, direction, step);

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
    public int getPositionValue(final Game game, final int row, final int col, final int direction, final int step) {

        try {
            final int[]               position      = getTranslatedPosition(row, col, direction, step);
            final int                 translatedRow = position[0];
            final int                 translatedCol = position[1];
            final List<List<Integer>> boardState    = game.getBoardState();

            return boardState.get(translatedRow).get(translatedCol);
        } catch (OutOfBoundsException ex) {
            return END_OF_DIRECTION;
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Checks if there are legal moves for player
     *
     * @param   player  1 for black, 2 for white
     *
     * @return  returns true if there is at lease one legal move
     */
    public boolean haveLegalMovesForPlayer(final Game game, final int player) {

        final Map<Integer, Map<String, List<Path>>> availablePaths = game.getAvailablePaths();
        final Map<String, List<Path>>               playerPaths    = availablePaths.get(player);

        return !playerPaths.isEmpty();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void move(final Game game, final String piece, final int player) throws NotStartedException,
        WrongOrderException, IllegalMoveException {

        if (!game.isStarted()) {
            throw new NotStartedException();
        }

        final int currentPlayer = game.getCurrentPlayer();

        if (player != currentPlayer) {
            throw new WrongOrderException();
        }

        final char[] chars = piece.toCharArray();
        final int    col   = BOARD_LETTERS.indexOf(chars[0]);
        final int    row   = BOARD_NUMBERS.indexOf(chars[1]);

        occupyPaths(game, row, col);
        updateAvailablePaths(game);

        final int otherPlayer = currentPlayer == BLACK_PLAYER ? WHITE_PLAYER : BLACK_PLAYER;

        if (haveLegalMovesForPlayer(game, otherPlayer)) {
            game.setCurrentPlayer(otherPlayer);
        } else if (!haveLegalMovesForPlayer(game, currentPlayer)) {
            game.setStarted(false);
            game.setCurrentPlayer(NO_PLAYER);
            game.setAvailablePaths(null);
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
     * @param  path  found path
     */
    public void occupyPath(final Game game, final Path path) {

        occupyPath(game, path.getTargetRow(), path.getTargetCol(), path.getDirection(), path.getStep());
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
    public void occupyPath(final Game game, final int targetRow, final int targetCol, final int direction,
            final int step) {

        final List<List<Integer>> boardState    = game.getBoardState();
        final int                 currentPlayer = game.getCurrentPlayer();

        for (int i = 0; i < step; i++) {

            try {
                final int[] position = getTranslatedPosition(targetRow, targetCol, direction, i);
                final int   row      = position[0];
                final int   col      = position[1];

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
    public void occupyPaths(final Game game, final int targetRow, final int targetCol) throws IllegalMoveException {

        final Map<Integer, Map<String, List<Path>>> availablePaths = game.getAvailablePaths();
        final int                                   currentPlayer  = game.getCurrentPlayer();

        if (availablePaths != null && !availablePaths.isEmpty()) {

            final Map<String, List<Path>> playerPaths = availablePaths.get(currentPlayer);
            final List<Path>              foundPaths  = playerPaths.get(convertLocationToText(targetRow, targetCol));

            if (foundPaths != null && !foundPaths.isEmpty()) {

                for (Path foundPath : foundPaths) {
                    occupyPath(game, foundPath);
                }

            } else {
                throw new IllegalMoveException();
            }
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    public void start(Game game) throws AlreadyStartedException {

        if (game.isStarted()) {
            throw new AlreadyStartedException();
        }

        game.setStarted(true);
        game.setCancelled(false);
        game.setCurrentPlayer(BLACK_PLAYER);
        game.setBoardState(Arrays.<List<Integer>>asList(

                //                     0  1  2  3  4  5  6  7
                //                     a  b  c  d  e  f  g  h
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0), // 1 0
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0), // 2 1
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0), // 3 2
                Arrays.<Integer>asList(0, 0, 0, 2, 1, 0, 0, 0), // 4 3
                Arrays.<Integer>asList(0, 0, 0, 1, 2, 0, 0, 0), // 5 4
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0), // 6 5
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0), // 7 6
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)  // 8 7
                ));

        updateAvailablePaths(game);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * scans all available paths for all players than updates availablePaths filed.
     */
    public void updateAvailablePaths(final Game game) {

        final HashMap<Integer, Map<String, List<Path>>> availablePaths;
        availablePaths = new HashMap<Integer, Map<String, List<Path>>>(2);

        for (int player = 1; player <= 2; player++) {

            final Map<String, List<Path>> playerPaths = new HashMap<String, List<Path>>();
            availablePaths.put(player, playerPaths);

            for (int row = 0; row < 8; row++) {

                for (int col = 0; col < 8; col++) {
                    final List<Path> paths = findAvailablePaths(game, row, col, player);

                    if (paths != null && paths.size() > 0) {
                        playerPaths.put(convertLocationToText(row, col), paths);
                    }
                }
            }
        }

        game.setAvailablePaths(availablePaths);
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
    private String convertLocationToText(final int row, final int col) {

        final StringBuilder text = new StringBuilder();

        text.append(BOARD_CHARS[col]);
        text.append(row + 1);

        return text.toString();
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
    private int[] getTranslatedPosition(final int targetRow, final int targetCol, final int direction, final int step)
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
}
