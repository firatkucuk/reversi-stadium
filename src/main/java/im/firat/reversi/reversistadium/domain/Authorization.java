
package im.firat.reversi.reversistadium.domain;


import java.io.Serializable;



/**
 * This domain object contains details about game authorization.
 */
public class Authorization implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String cancellationCode;
    private String playerBlackAuthCode;
    private String playerWhiteAuthCode;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Authorization() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public String getCancellationCode() {

        return cancellationCode;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getPlayerBlackAuthCode() {

        return playerBlackAuthCode;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public String getPlayerWhiteAuthCode() {

        return playerWhiteAuthCode;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setCancellationCode(String cancellationCode) {

        this.cancellationCode = cancellationCode;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setPlayerBlackAuthCode(String playerBlackAuthCode) {

        this.playerBlackAuthCode = playerBlackAuthCode;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setPlayerWhiteAuthCode(String playerWhiteAuthCode) {

        this.playerWhiteAuthCode = playerWhiteAuthCode;
    }
}
