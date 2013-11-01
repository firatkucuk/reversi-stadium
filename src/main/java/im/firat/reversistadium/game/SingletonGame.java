
package im.firat.reversistadium.game;


/**
 * Reversi-Stadium supports one game instance at one time. This class adds singleton access to Game and Reversi Game
 * classes.
 */
public class SingletonGame {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final Game game = new Game();



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    private SingletonGame() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public static Game getInstance() {

        return game;
    }
}
