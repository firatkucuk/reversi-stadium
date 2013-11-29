
package im.firat.reversi.reversistadium.dao;


import im.firat.reversi.reversistadium.datastore.SingletonAuthorization;
import im.firat.reversi.reversistadium.domain.Authorization;



public final class MemoryAuthorizationDaoImpl implements AuthorizationDao {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public MemoryAuthorizationDaoImpl() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @Override
    public final Authorization getCurrentGameAuthorization() {

        return SingletonAuthorization.getInstance();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public void persist(final Authorization authorization) {

        // Nothing to do already persisted to RAM
    }
}
