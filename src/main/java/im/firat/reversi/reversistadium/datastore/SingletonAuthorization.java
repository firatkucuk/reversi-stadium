
package im.firat.reversi.reversistadium.datastore;


import im.firat.reversi.domain.Authorization;



public final class SingletonAuthorization {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final Authorization authorization = new Authorization();



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    private SingletonAuthorization() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public static Authorization getInstance() {

        return authorization;
    }
}
