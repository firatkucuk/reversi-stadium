
package im.firat.reversistadium.game;

import java.util.Random;



public class SingletonGame {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static Game game = new Game();



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    private SingletonGame() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public static Game getInstance() {

        return game;
    }
}
