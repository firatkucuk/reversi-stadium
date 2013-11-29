
package im.firat.reversi.reversistadium.datastore;


import im.firat.reversi.reversistadium.domain.Game;



/**
 * Reversi-Stadium supports one game instance at a time. This class provides singleton access to Game domain object
 */
public final class SingletonGame {



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
