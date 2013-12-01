
package im.firat.reversi.reversistadium.game;

import im.firat.reversi.beans.Path;
import im.firat.reversi.beans.Position;
import im.firat.reversi.domain.Game;
import im.firat.reversi.exceptions.AlreadyStartedException;
import im.firat.reversi.exceptions.IllegalMoveException;
import im.firat.reversi.exceptions.NotStartedException;
import im.firat.reversi.exceptions.WrongOrderException;
import im.firat.reversi.services.GameService;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.Assert;
import org.junit.Test;

import static im.firat.reversi.services.GameService.*;



public final class GameTest {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameTest() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public void findAvailablePathsTest() {

        /*
          7  0  1
           \ | /
            \|/
          6--+--2
            /|\
           / | \
          5  4  3
        */

        final GameService gameService = new GameService();
        final Game        game        = new Game();

        game.setCurrentPlayer(BLACK_PLAYER);
        game.setBoardState(Arrays.asList(

                //                     0  1  2  3  4  5  6  7
                //                     a  b  c  d  e  f  g  h
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0), // 1 0
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0), // 2 1
                Arrays.<Integer>asList(1, 1, 0, 0, 0, 0, 0, 0), // 3 2
                Arrays.<Integer>asList(2, 1, 0, 0, 0, 0, 0, 0), // 4 3
                Arrays.<Integer>asList(2, 1, 0, 1, 0, 1, 0, 0), // 5 4
                Arrays.<Integer>asList(0, 0, 0, 0, 2, 2, 0, 0), // 6 5
                Arrays.<Integer>asList(0, 0, 1, 2, 2, 0, 2, 2), // 7 6
                Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)  // 8 7
                ));

        Map<Position, List<Path>> paths;
        Position                  position;

        position = new Position(2, 0);
        paths    = gameService.findAvailablePaths(game, position, BLACK_PLAYER);

        Assert.assertNull(paths); // Should be null if target location is not null

        position = new Position(0, 5);
        paths    = gameService.findAvailablePaths(game, position, BLACK_PLAYER);

        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        position = new Position(1, 0);
        paths    = gameService.findAvailablePaths(game, position, BLACK_PLAYER);

        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        position = new Position(5, 1);
        paths    = gameService.findAvailablePaths(game, position, BLACK_PLAYER);

        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        position = new Position(5, 0);
        paths    = gameService.findAvailablePaths(game, position, BLACK_PLAYER);

        Assert.assertEquals(paths.get(position).size(), 1);

        position = new Position(6, 5);
        paths    = gameService.findAvailablePaths(game, position, BLACK_PLAYER);

        Assert.assertEquals(paths.get(position).size(), 3);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public void illegalMoveScenarioTest() throws AlreadyStartedException, WrongOrderException, NotStartedException {

        try {
            final GameService gameService = new GameService();
            final Game        game        = new Game();

            gameService.start(game);

            gameService.move(game, "d3", BLACK_PLAYER);
            gameService.move(game, "c5", WHITE_PLAYER);
            gameService.move(game, "f6", BLACK_PLAYER);
            gameService.move(game, "f5", WHITE_PLAYER);
            gameService.move(game, "c6", BLACK_PLAYER);
            gameService.move(game, "e3", WHITE_PLAYER);
            gameService.move(game, "f3", BLACK_PLAYER);
            gameService.move(game, "f4", WHITE_PLAYER);
            gameService.move(game, "g6", BLACK_PLAYER);
            gameService.move(game, "e2", WHITE_PLAYER);

            gameService.move(game, "d1", BLACK_PLAYER);
            gameService.move(game, "c2", WHITE_PLAYER);
            gameService.move(game, "b3", BLACK_PLAYER);
            gameService.move(game, "f1", WHITE_PLAYER);
            gameService.move(game, "b5", BLACK_PLAYER);
            gameService.move(game, "f2", WHITE_PLAYER);
            gameService.move(game, "g1", BLACK_PLAYER);
            gameService.move(game, "h1", WHITE_PLAYER);
            gameService.move(game, "g3", BLACK_PLAYER);
            gameService.move(game, "d2", WHITE_PLAYER);

            gameService.move(game, "d6", BLACK_PLAYER);
            gameService.move(game, "d6", WHITE_PLAYER);
        } catch (IllegalMoveException e) {
            assert true;

            return;
        }

        assert false;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public void occupyPathTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException,
        InvocationTargetException {

        /*
          Directions:

          7  0  1
           \ | /
            \|/
          6--+--2
            /|\
           / | \
          5  4  3
        */

        final GameService gameService = new GameService();
        final Game        game        = new Game();

        game.setCurrentPlayer(BLACK_PLAYER);

        List<List<Integer>> boardState;
        List<List<Integer>> targetBoardState;
        Position            targetPosition;
        int                 direction;
        int                 step;

        // Direction 0 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(1, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(2, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(2, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(1, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(1, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(1, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(1, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(5, 0);
        direction        = 0;
        step             = 3;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 1 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 1, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 2, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 2, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 1, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 1, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 1, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 1, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(6, 1);
        direction        = 1;
        step             = 4;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 2 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 2, 2, 2, 2, 1, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 1, 1, 1, 1, 1, 1, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(4, 1);
        direction        = 2;
        step             = 5;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 3 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 2, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 1, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 1, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 1, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 1, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(0, 1);
        direction        = 3;
        step             = 3;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 4 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(0, 3);
        direction        = 4;
        step             = 2;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 5 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 2, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 2, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 1, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 1, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 1, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 1, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 1, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(2, 5);
        direction        = 5;
        step             = 4;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 6 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(1, 2, 2, 2, 2, 2, 2, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(1, 1, 1, 1, 1, 1, 1, 1),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(0, 7);
        direction        = 6;
        step             = 7;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 7 Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 1, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 2, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 2, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 2, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 2, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 2, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 1, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 1, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 1, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 1, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 1, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 1, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 1)      // 8 7
            );
        targetPosition   = new Position(7, 7);
        direction        = 7;
        step             = 6;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));

        // Direction 7 Over Range Step Test

        boardState       = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetBoardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(1, 1, 1, 1, 1, 1, 1, 1),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );
        targetPosition   = new Position(0, 0);
        direction        = 2;
        step             = 100;

        game.setBoardState(boardState);
        gameService.occupyPath(game, targetPosition, direction, step);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Test
    public void positionToStringTest() throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {

        Position position;

        position = new Position(0, 0);
        Assert.assertEquals(position.toString(), "a1");

        position = new Position(0, 7);
        Assert.assertEquals(position.toString(), "h1");

        position = new Position(7, 0);
        Assert.assertEquals(position.toString(), "a8");

        position = new Position(7, 7);
        Assert.assertEquals(position.toString(), "h8");

        position = new Position(3, 3);
        Assert.assertEquals(position.toString(), "d4");
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public void sampleGameScenarioTest() throws AlreadyStartedException, WrongOrderException, IllegalMoveException,
        NotStartedException {

        final GameService gameService = new GameService();
        final Game        game        = new Game();

        gameService.start(game);

        gameService.move(game, "f5", BLACK_PLAYER);
        gameService.move(game, "f6", WHITE_PLAYER);
        gameService.move(game, "e6", BLACK_PLAYER);
        gameService.move(game, "f4", WHITE_PLAYER);
        gameService.move(game, "g3", BLACK_PLAYER);
        gameService.move(game, "d6", WHITE_PLAYER);
        gameService.move(game, "g7", BLACK_PLAYER);
        gameService.move(game, "h8", WHITE_PLAYER);
        gameService.move(game, "c7", BLACK_PLAYER);
        gameService.move(game, "d7", WHITE_PLAYER);

        gameService.move(game, "f7", BLACK_PLAYER);
        gameService.move(game, "h2", WHITE_PLAYER);
        gameService.move(game, "f3", BLACK_PLAYER);
        gameService.move(game, "g6", WHITE_PLAYER);
        gameService.move(game, "e7", BLACK_PLAYER);
        gameService.move(game, "b7", WHITE_PLAYER);
        gameService.move(game, "b8", BLACK_PLAYER);
        gameService.move(game, "c6", WHITE_PLAYER);
        gameService.move(game, "a8", BLACK_PLAYER);
        gameService.move(game, "c5", WHITE_PLAYER);

        gameService.move(game, "h6", BLACK_PLAYER);
        gameService.move(game, "h7", WHITE_PLAYER);
        gameService.move(game, "c3", BLACK_PLAYER);
        gameService.move(game, "b6", WHITE_PLAYER);
        gameService.move(game, "a6", BLACK_PLAYER);
        gameService.move(game, "a7", WHITE_PLAYER);
        gameService.move(game, "b5", BLACK_PLAYER);
        gameService.move(game, "a5", WHITE_PLAYER);
        gameService.move(game, "f8", BLACK_PLAYER);
        gameService.move(game, "g8", WHITE_PLAYER);

        gameService.move(game, "e8", BLACK_PLAYER);
        gameService.move(game, "c8", WHITE_PLAYER);
        gameService.move(game, "d8", BLACK_PLAYER);
        gameService.move(game, "e3", WHITE_PLAYER);
        gameService.move(game, "c4", BLACK_PLAYER);
        gameService.move(game, "d3", WHITE_PLAYER);
        gameService.move(game, "h3", BLACK_PLAYER);
        gameService.move(game, "b2", WHITE_PLAYER);
        gameService.move(game, "b4", BLACK_PLAYER);
        gameService.move(game, "h5", WHITE_PLAYER);

        gameService.move(game, "g5", BLACK_PLAYER);
        gameService.move(game, "h4", WHITE_PLAYER);
        gameService.move(game, "a4", BLACK_PLAYER);
        gameService.move(game, "f2", WHITE_PLAYER);
        gameService.move(game, "a1", BLACK_PLAYER);
        gameService.move(game, "g4", WHITE_PLAYER);
        gameService.move(game, "g1", BLACK_PLAYER);
        gameService.move(game, "e1", WHITE_PLAYER);
        gameService.move(game, "f1", BLACK_PLAYER);
        gameService.move(game, "e2", WHITE_PLAYER);

        gameService.move(game, "g2", BLACK_PLAYER);
        gameService.move(game, "h1", WHITE_PLAYER);
        gameService.move(game, "d2", BLACK_PLAYER);
        gameService.move(game, "c2", WHITE_PLAYER);
        gameService.move(game, "c1", BLACK_PLAYER);
        gameService.move(game, "d1", WHITE_PLAYER);
        gameService.move(game, "a2", WHITE_PLAYER);
        gameService.move(game, "a3", BLACK_PLAYER);
        gameService.move(game, "b1", WHITE_PLAYER);
        gameService.move(game, "b3", WHITE_PLAYER);

        Assert.assertFalse(game.isStarted());
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private String boardStateToText(final List<List<Integer>> boardState) {

        final StringBuilder text = new StringBuilder(64);

        for (final List<Integer> row : boardState) {

            for (Integer cell : row) {
                text.append(cell);
            }
        }

        return text.toString();
    }
}
