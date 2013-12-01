
package im.firat.reversi.services;


import im.firat.reversi.beans.Path;
import im.firat.reversi.beans.Position;
import im.firat.reversi.core.Utils;
import im.firat.reversi.domain.Game;
import im.firat.reversi.exceptions.*;
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



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameService() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

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
     * @param   position   start position
     * @param   direction  direction number. Range 0 - 7
     * @param   step       translation distance
     *
     * @return  returns translated position index 0 is row, index 1 is col
     *
     * @throws  OutOfBoundsException
     */
    public static Position getTranslatedPosition(final Position position, final int direction, final int step)
        throws OutOfBoundsException {

        int row = position.getRow(), col = position.getCol();

        if (direction == 0) {
            row = position.getRow() - step;
        } else if (direction == 1) {
            row = position.getRow() - step;
            col = position.getCol() + step;
        } else if (direction == 2) {
            col = position.getCol() + step;
        } else if (direction == 3) {
            row = position.getRow() + step;
            col = position.getCol() + step;
        } else if (direction == 4) {
            row = position.getRow() + step;
        } else if (direction == 5) {
            row = position.getRow() + step;
            col = position.getCol() - step;
        } else if (direction == 6) {
            col = position.getCol() - step;
        } else if (direction == 7) {
            row = position.getRow() - step;
            col = position.getCol() - step;
        }

        if (row < 0 || col < 0 || row > 7 || col > 7) {
            throw new OutOfBoundsException();
        }

        return new Position(row, col);
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public void cancel(final Game game) throws NotStartedException {

        if (!game.isStarted()) {
            throw new NotStartedException();
        }

        game.setStarted(false);
        game.setCancelled(true);
        game.setAvailableMoves(null);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public Map<Position, List<Path>> findAllAvailablePaths(final Game game, final int player) {

        final Map<Position, List<Path>> availablePaths = new HashMap<Position, List<Path>>();

        for (int row = 0; row < 8; row++) {

            for (int col = 0; col < 8; col++) {
                final Position                  position      = new Position(row, col);
                final Map<Position, List<Path>> positionPaths = findAvailablePaths(game, position, player);

                if (positionPaths != null && !positionPaths.isEmpty()) {
                    availablePaths.putAll(positionPaths);
                }
            }
        }

        return availablePaths;
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
     * @param   game      current game
     * @param   position  target position
     * @param   player    player value for pattern validation
     *
     * @return  Returns found path if valid pattern found.
     */
    public Map<Position, List<Path>> findAvailablePaths(final Game game, final Position position, final int player) {

        final List<List<Integer>> boardState = game.getBoardState();

        if (boardState.get(position.getRow()).get(position.getCol()) != EMPTY_PLACE) {
            return null;
        }

        final Map<Position, List<Path>> playerPaths     = new HashMap<Position, List<Path>>();
        final int                       DIFFERENT_COLOR = player == WHITE_PLAYER ? BLACK_DISK : WHITE_DISK;
        final int                       SAME_COLOR      = player; // Redundant constant for code readability

        for (int direction = 0; direction < 8; direction++) {

            int value = getPositionValue(game, position, direction, 1);

            if (value == DIFFERENT_COLOR) {

                for (int step = 2; step < 8; step++) {

                    value = getPositionValue(game, position, direction, step);

                    if (value == END_OF_DIRECTION || value == EMPTY_PLACE) {
                        break;
                    } else if (value == SAME_COLOR) {
                        List<Path> paths = playerPaths.get(position);

                        if (paths == null) {
                            paths = new ArrayList<Path>();
                        }

                        paths.add(new Path(position, direction, step));
                        playerPaths.put(position, paths);

                        break;
                    }
                } // end for
            }
        }         // end for

        return playerPaths;
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
     * @param   position   start position
     * @param   direction  direction number. Range 0 - 7
     * @param   step       translation distance
     *
     * @return  -1 for out of bound locations, 0 for empty locations, 1 for black disk, 2 for white disk
     */
    public int getPositionValue(final Game game, final Position position, final int direction, final int step) {

        try {
            final Position            translatedPosition = getTranslatedPosition(position, direction, step);
            final List<List<Integer>> boardState         = game.getBoardState();

            return boardState.get(translatedPosition.getRow()).get(translatedPosition.getCol());
        } catch (OutOfBoundsException ex) {
            return END_OF_DIRECTION;
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public synchronized void move(final Game game, final String piece, final int player) throws NotStartedException,
        WrongOrderException, IllegalMoveException {

        if (!game.isStarted()) {
            throw new NotStartedException();
        }

        final int currentPlayer = game.getCurrentPlayer();

        if (player != currentPlayer) {
            throw new WrongOrderException();
        }

        Position                  position    = new Position(piece);
        Map<Position, List<Path>> playerPaths = findAvailablePaths(game, position, currentPlayer);

        if (playerPaths == null || playerPaths.isEmpty()) {
            throw new IllegalMoveException();
        }

        occupyPaths(game, playerPaths, position);

        final int otherPlayer = currentPlayer == BLACK_PLAYER ? WHITE_PLAYER : BLACK_PLAYER;

        if (!(playerPaths = findAllAvailablePaths(game, otherPlayer)).isEmpty()) {

            // if have legal moves for other player
            game.setCurrentPlayer(otherPlayer);
            game.setAvailableMoves(Utils.positionSet2StringList(playerPaths.keySet()));
        } else if (!(playerPaths = findAllAvailablePaths(game, currentPlayer)).isEmpty()) {

            // if have legal moves for current player
            game.setAvailableMoves(Utils.positionSet2StringList(playerPaths.keySet()));
        } else {
            game.setStarted(false);
            game.setCurrentPlayer(NO_PLAYER);
            game.setAvailableMoves(null);
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

        occupyPath(game, path.getPosition(), path.getDirection(), path.getStep());
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
     * @param  position   start position
     * @param  direction  direction number. Range 0 - 7
     * @param  step       translation distance
     */
    public void occupyPath(final Game game, final Position position, final int direction, final int step) {

        final List<List<Integer>> boardState    = game.getBoardState();
        final int                 currentPlayer = game.getCurrentPlayer();

        for (int i = 0; i < step; i++) {

            try {
                final Position translatedPosition = getTranslatedPosition(position, direction, i);

                // player constants and disk constants are equivalent. So can be used "currentPlayer" for
                // current player's disk color.
                boardState.get(translatedPosition.getRow()).set(translatedPosition.getCol(), currentPlayer);
            } catch (OutOfBoundsException ex) {
                break;
            }
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void occupyPaths(final Game game, final Map<Position, List<Path>> playerPaths, final Position position)
        throws IllegalMoveException {

        if (playerPaths != null && !playerPaths.isEmpty()) {

            final List<Path> foundPaths = playerPaths.get(position);

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

    public void start(Game game) throws AlreadyStartedException {

        if (game.isStarted()) {
            throw new AlreadyStartedException();
        }

        game.setStarted(true);
        game.setCancelled(false);
        game.setCurrentPlayer(BLACK_PLAYER);
        game.setBoardState(Arrays.asList(

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
        game.setAvailableMoves(Arrays.asList("c4", "d3", "e6", "f5"));
    }
}
