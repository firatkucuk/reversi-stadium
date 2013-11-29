
package im.firat.reversi.reversistadium.dao;

import im.firat.reversi.reversistadium.domain.Authorization;



/**
 * Created with IntelliJ IDEA. User: firat Date: 11/29/13 Time: 10:49 AM To change this template use File | Settings |
 * File Templates.
 */
public interface AuthorizationDao {



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public Authorization getCurrentGameAuthorization();



    //~ ----------------------------------------------------------------------------------------------------------------

    public void persist(final Authorization authorization);
}
