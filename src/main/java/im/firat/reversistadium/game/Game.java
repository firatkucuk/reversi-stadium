
package im.firat.reversistadium.game;


import im.firat.reversistadium.exceptions.*;
import java.io.Serializable;
import java.util.Random;



public class Game implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final long   serialVersionUID = 1L;
    private static final String ALPHABET         = "abcdefghijklmnopqrstuvwxyz";



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private String      cancellationCode;
    private String      playerBlackAuthCode;
    private String      playerWhiteAuthCode;
    private ReversiGame reversiGame;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Game() {

        this.reversiGame = new ReversiGame();
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public void cancel(String cancellationCode) throws WrongCodeException, NotStartedException {

        if (cancellationCode.equals(this.cancellationCode)) {
            reversiGame.cancel();
        } else {
            throw new WrongCodeException();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

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

    public ReversiGame getReversiGame() {

        return reversiGame;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void move(String authCode, String piece) throws WrongCodeException, NotStartedException,
        IllegalMoveException, WrongOrderException {

        if (authCode.equals(playerBlackAuthCode)) {
            reversiGame.move(piece, Constants.BLACK_PLAYER);
        } else if (authCode.equals(playerWhiteAuthCode)) {
            reversiGame.move(piece, Constants.WHITE_PLAYER);
        } else {
            throw new WrongCodeException();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void start() throws AlreadyStartedException {

        playerBlackAuthCode = generateAuthCode();
        playerWhiteAuthCode = generateAuthCode();
        cancellationCode    = generateAuthCode() + generateAuthCode();

        reversiGame.start();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    private String generateAuthCode() {

        StringBuffer authCode = new StringBuffer();
        Random       random   = new Random();

        for (int i = 0; i < 4; i++) {
            authCode.append(ALPHABET.charAt(random.nextInt(26)));
        }

        authCode.append(random.nextInt(9999));

        return authCode.toString();
    }
}
