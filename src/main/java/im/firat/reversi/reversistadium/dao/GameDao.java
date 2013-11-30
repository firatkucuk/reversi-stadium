
package im.firat.reversi.reversistadium.dao;

import im.firat.reversi.domain.Game;



public interface GameDao {



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public Game getCurrentGame();



    //~ ----------------------------------------------------------------------------------------------------------------

    public void persist(final Game game);
}
