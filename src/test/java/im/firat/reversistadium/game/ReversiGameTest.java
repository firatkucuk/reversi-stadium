
package im.firat.reversistadium.game;

import im.firat.reversistadium.exceptions.AlreadyStartedException;
import im.firat.reversistadium.exceptions.IllegalMoveException;
import im.firat.reversistadium.exceptions.NotStartedException;
import im.firat.reversistadium.exceptions.WrongOrderException;
import org.junit.Test;

import static im.firat.reversistadium.game.Constants.*;



public class ReversiGameTest {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public ReversiGameTest() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Test
    public void sampleGameScenario() throws AlreadyStartedException, WrongOrderException, IllegalMoveException,
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

        if (!reversiGame.isStarted()) {
            assert true;

            return;
        }

        assert false;
    }
}
