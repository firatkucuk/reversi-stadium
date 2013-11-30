
package im.firat.reversi.reversistadium.dao;


import im.firat.reversi.domain.Game;
import im.firat.reversi.reversistadium.datastore.SingletonGame;



public final class MemoryGameDaoImpl implements GameDao {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public MemoryGameDaoImpl() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public Game getCurrentGame() {

        return SingletonGame.getInstance();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public void persist(final Game game) {

        // Nothing to do already persisted to RAM
    }
}
