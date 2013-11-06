
package im.firat.reversistadium.game;

import im.firat.reversistadium.exceptions.AlreadyStartedException;
import im.firat.reversistadium.exceptions.IllegalMoveException;
import im.firat.reversistadium.exceptions.NotStartedException;
import im.firat.reversistadium.exceptions.WrongOrderException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

import static im.firat.reversistadium.game.Constants.*;



public class ReversiGameTest {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public ReversiGameTest() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Test
    public void convertLocationToTextTest() throws NoSuchMethodException, InvocationTargetException,
        IllegalAccessException {

        Method      method      = ReversiGame.class.getDeclaredMethod("convertLocationToText", int.class, int.class);
        ReversiGame reversiGame = new ReversiGame();
        String      cell;

        method.setAccessible(true);

        cell = (String) method.invoke(reversiGame, 0, 0);
        Assert.assertEquals(cell, "a1");

        cell = (String) method.invoke(reversiGame, 0, 7);
        Assert.assertEquals(cell, "h1");

        cell = (String) method.invoke(reversiGame, 7, 0);
        Assert.assertEquals(cell, "a8");

        cell = (String) method.invoke(reversiGame, 7, 7);
        Assert.assertEquals(cell, "h8");

        cell = (String) method.invoke(reversiGame, 3, 3);
        Assert.assertEquals(cell, "d4");
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @SuppressWarnings("unchecked")
    @Test
    public void findAvailablePathsTest() throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException,
        InvocationTargetException {

        /*
          7  0  1
           \ | /
            \|/
          6--+--2
            /|\
           / | \
          5  4  3
        */

        Class                  clazz              = ReversiGame.class;
        Field                  boardStateField    = clazz.getDeclaredField("boardState");
        Field                  currentPlayerField = clazz.getDeclaredField("currentPlayer");
        ReversiGame            reversiGame        = new ReversiGame();
        List<List<Integer>>    boardState;
        List<ReversiGame.Path> paths;

        // private List<Path> findAvailablePaths(int targetRow, int targetCol, int player);
        Method method = clazz.getDeclaredMethod("findAvailablePaths", int.class, int.class, int.class);

        boardStateField.setAccessible(true);
        currentPlayerField.setAccessible(true);
        method.setAccessible(true);
        currentPlayerField.set(reversiGame, Constants.BLACK_PLAYER);

        boardState = Arrays.asList(

            //                     0  1  2  3  4  5  6  7
            //                     a  b  c  d  e  f  g  h
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 1 0
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0),     // 2 1
            Arrays.<Integer>asList(1, 1, 0, 0, 0, 0, 0, 0),     // 3 2
            Arrays.<Integer>asList(2, 1, 0, 0, 0, 0, 0, 0),     // 4 3
            Arrays.<Integer>asList(2, 1, 0, 1, 0, 1, 0, 0),     // 5 4
            Arrays.<Integer>asList(0, 0, 0, 0, 2, 2, 0, 0),     // 6 5
            Arrays.<Integer>asList(0, 0, 1, 2, 2, 0, 2, 2),     // 7 6
            Arrays.<Integer>asList(0, 0, 0, 0, 0, 0, 0, 0)      // 8 7
            );

        boardStateField.set(reversiGame, boardState);


        paths = (List<ReversiGame.Path>) method.invoke(reversiGame, 2, 0, Constants.BLACK_PLAYER);
        Assert.assertNull(paths); // Should be null if target location is not null

        paths = (List<ReversiGame.Path>) method.invoke(reversiGame, 0, 5, Constants.BLACK_PLAYER);
        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        paths = (List<ReversiGame.Path>) method.invoke(reversiGame, 1, 0, Constants.BLACK_PLAYER);
        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        paths = (List<ReversiGame.Path>) method.invoke(reversiGame, 5, 1, Constants.BLACK_PLAYER);
        Assert.assertTrue(paths.isEmpty()); // Should be empty if there's no available path

        paths = (List<ReversiGame.Path>) method.invoke(reversiGame, 5, 0, Constants.BLACK_PLAYER);
        Assert.assertEquals(paths.size(), 1);

        paths = (List<ReversiGame.Path>) method.invoke(reversiGame, 6, 5, Constants.BLACK_PLAYER);
        Assert.assertEquals(paths.size(), 3);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Test
    public void occupyPathTest() throws NoSuchFieldException, IllegalAccessException, NoSuchMethodException,
        InvocationTargetException {

        /*
          7  0  1
           \ | /
            \|/
          6--+--2
            /|\
           / | \
          5  4  3
        */

        Field               boardStateField    = ReversiGame.class.getDeclaredField("boardState");
        Field               currentPlayerField = ReversiGame.class.getDeclaredField("currentPlayer");
        ReversiGame         reversiGame        = new ReversiGame();
        List<List<Integer>> boardState;
        List<List<Integer>> targetBoardState;

        // private void occupyPath(int targetRow, int targetCol, int direction, int step);
        Method method = ReversiGame.class.getDeclaredMethod("occupyPath", int.class, int.class, int.class, int.class);

        boardStateField.setAccessible(true);
        currentPlayerField.setAccessible(true);
        method.setAccessible(true);
        currentPlayerField.set(reversiGame, Constants.BLACK_PLAYER);

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 5, 0, 0, 3);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 6, 1, 1, 4);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 4, 1, 2, 5);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 0, 1, 3, 3);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 0, 3, 4, 2);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 2, 5, 5, 4);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 0, 7, 6, 7);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 7, 7, 7, 6);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));

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

        boardStateField.set(reversiGame, boardState);
        method.invoke(reversiGame, 0, 0, 2, 100);

        Assert.assertEquals(boardStateToText(reversiGame.getBoardState()), boardStateToText(targetBoardState));
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Test
    public void sampleGameScenarioTest() throws AlreadyStartedException, WrongOrderException, IllegalMoveException,
        NotStartedException {

        ReversiGame reversiGame = new ReversiGame();

        reversiGame.start();

        reversiGame.move("f5", BLACK_PLAYER);
        reversiGame.move("f6", WHITE_PLAYER);
        reversiGame.move("e6", BLACK_PLAYER);
        reversiGame.move("f4", WHITE_PLAYER);
        reversiGame.move("g3", BLACK_PLAYER);
        reversiGame.move("d6", WHITE_PLAYER);
        reversiGame.move("g7", BLACK_PLAYER);
        reversiGame.move("h8", WHITE_PLAYER);
        reversiGame.move("c7", BLACK_PLAYER);
        reversiGame.move("d7", WHITE_PLAYER);

        reversiGame.move("f7", BLACK_PLAYER);
        reversiGame.move("h2", WHITE_PLAYER);
        reversiGame.move("f3", BLACK_PLAYER);
        reversiGame.move("g6", WHITE_PLAYER);
        reversiGame.move("e7", BLACK_PLAYER);
        reversiGame.move("b7", WHITE_PLAYER);
        reversiGame.move("b8", BLACK_PLAYER);
        reversiGame.move("c6", WHITE_PLAYER);
        reversiGame.move("a8", BLACK_PLAYER);
        reversiGame.move("c5", WHITE_PLAYER);

        reversiGame.move("h6", BLACK_PLAYER);
        reversiGame.move("h7", WHITE_PLAYER);
        reversiGame.move("c3", BLACK_PLAYER);
        reversiGame.move("b6", WHITE_PLAYER);
        reversiGame.move("a6", BLACK_PLAYER);
        reversiGame.move("a7", WHITE_PLAYER);
        reversiGame.move("b5", BLACK_PLAYER);
        reversiGame.move("a5", WHITE_PLAYER);
        reversiGame.move("f8", BLACK_PLAYER);
        reversiGame.move("g8", WHITE_PLAYER);

        reversiGame.move("e8", BLACK_PLAYER);
        reversiGame.move("c8", WHITE_PLAYER);
        reversiGame.move("d8", BLACK_PLAYER);
        reversiGame.move("e3", WHITE_PLAYER);
        reversiGame.move("c4", BLACK_PLAYER);
        reversiGame.move("d3", WHITE_PLAYER);
        reversiGame.move("h3", BLACK_PLAYER);
        reversiGame.move("b2", WHITE_PLAYER);
        reversiGame.move("b4", BLACK_PLAYER);
        reversiGame.move("h5", WHITE_PLAYER);

        reversiGame.move("g5", BLACK_PLAYER);
        reversiGame.move("h4", WHITE_PLAYER);
        reversiGame.move("a4", BLACK_PLAYER);
        reversiGame.move("f2", WHITE_PLAYER);
        reversiGame.move("a1", BLACK_PLAYER);
        reversiGame.move("g4", WHITE_PLAYER);
        reversiGame.move("g1", BLACK_PLAYER);
        reversiGame.move("e1", WHITE_PLAYER);
        reversiGame.move("f1", BLACK_PLAYER);
        reversiGame.move("e2", WHITE_PLAYER);

        reversiGame.move("g2", BLACK_PLAYER);
        reversiGame.move("h1", WHITE_PLAYER);
        reversiGame.move("d2", BLACK_PLAYER);
        reversiGame.move("c2", WHITE_PLAYER);
        reversiGame.move("c1", BLACK_PLAYER);
        reversiGame.move("d1", WHITE_PLAYER);
        reversiGame.move("a2", WHITE_PLAYER);
        reversiGame.move("a3", BLACK_PLAYER);
        reversiGame.move("b1", WHITE_PLAYER);
        reversiGame.move("b3", WHITE_PLAYER);

        Assert.assertFalse(reversiGame.isStarted());
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private String boardStateToText(List<List<Integer>> boardState) {

        StringBuilder text = new StringBuilder(64);

        for (List<Integer> boardRow : boardState) {

            for (Integer cell : boardRow) {
                text.append(cell);
            }
        }

        return text.toString();
    }
}
