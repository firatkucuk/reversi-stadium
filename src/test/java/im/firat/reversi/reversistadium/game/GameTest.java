
package im.firat.reversi.reversistadium.game;

import im.firat.reversi.reversistadium.beans.Path;
import im.firat.reversi.reversistadium.domain.Game;
import im.firat.reversi.reversistadium.exceptions.AlreadyStartedException;
import im.firat.reversi.reversistadium.exceptions.IllegalMoveException;
import im.firat.reversi.reversistadium.exceptions.NotStartedException;
import im.firat.reversi.reversistadium.exceptions.WrongOrderException;
import im.firat.reversi.reversistadium.repositories.GameRepository;
import im.firat.reversi.reversistadium.services.GameService;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import static im.firat.reversi.reversistadium.services.GameService.*;



public final class GameTest {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameTest() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Test
    public final void convertLocationToTextTest() throws NoSuchMethodException, InvocationTargetException,
        IllegalAccessException {

        final Method method = GameService.class.getDeclaredMethod(
            "convertLocationToText", // method name
            int.class,     // parameter - row
            int.class      // parameter - col
        );

        // Making private method accessible for test
        method.setAccessible(true);

        final GameService gameService = new GameService();

        String cell;

        cell = (String) method.invoke(gameService, 0, 0);
        Assert.assertEquals(cell, "a1");

        cell = (String) method.invoke(gameService, 0, 7);
        Assert.assertEquals(cell, "h1");

        cell = (String) method.invoke(gameService, 7, 0);
        Assert.assertEquals(cell, "a8");

        cell = (String) method.invoke(gameService, 7, 7);
        Assert.assertEquals(cell, "h8");

        cell = (String) method.invoke(gameService, 3, 3);
        Assert.assertEquals(cell, "d4");
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public final void findAvailablePathsTest() {

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

        List<Path> paths;

        paths = gameService.findAvailablePaths(game, 2, 0, BLACK_PLAYER);
        Assert.assertNull(paths); // Should be null if target location is not null

        paths = gameService.findAvailablePaths(game, 0, 5, BLACK_PLAYER);
        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        paths = gameService.findAvailablePaths(game, 1, 0, BLACK_PLAYER);
        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        paths = gameService.findAvailablePaths(game, 5, 1, BLACK_PLAYER);
        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        paths = gameService.findAvailablePaths(game, 5, 0, BLACK_PLAYER);
        Assert.assertEquals(paths.size(), 1);

        paths = gameService.findAvailablePaths(game, 6, 5, BLACK_PLAYER);
        Assert.assertEquals(paths.size(), 3);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public final void occupyPathTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException,
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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 5, 0, 0, 3);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 6, 1, 1, 4);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 4, 1, 2, 5);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 0, 1, 3, 3);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 0, 3, 4, 2);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 2, 5, 5, 4);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 0, 7, 6, 7);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 7, 7, 7, 6);

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

        game.setBoardState(boardState);
        gameService.occupyPath(game, 0, 0, 2, 100);

        Assert.assertEquals(boardStateToText(game.getBoardState()), boardStateToText(targetBoardState));
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public final void sampleGameScenarioTest() throws AlreadyStartedException, WrongOrderException,
        IllegalMoveException, NotStartedException {

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
