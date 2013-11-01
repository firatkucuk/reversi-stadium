
package im.firat.reversistadium.game;


import im.firat.reversistadium.exceptions.*;
import java.io.Serializable;
import java.util.Random;



/**
 * Game class provides authentication layer to ReversiGame class. All operations about ReversiGame use this class
 * directly. And also this class directs start, cancel, move operations to ReversiGame class
 */
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

    /**
     * Directs cancel operation to Reversi Game class instance and adds authenticated cancellation.
     *
     * @param   cancellationCode  required code for running game cancellation
     *
     * @throws  WrongCodeException   if cancellation code is wrong
     * @throws  NotStartedException  if game is not started yet
     */
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

    /**
     * Directs move operation to ReversiGame class instance and adds security layer.
     *
     * @param   authCode     authentication code of current player
     * @param   desiredMove  desired in move in string format c4, a8 etc.
     *
     * @throws  WrongCodeException    if player code is wrong
     * @throws  NotStartedException   if game is not started yet
     * @throws  IllegalMoveException  if move is illegal
     * @throws  WrongOrderException   if current order is other players order
     */
    public void move(String authCode, String desiredMove) throws WrongCodeException, NotStartedException,
        IllegalMoveException, WrongOrderException {

        if (authCode.equals(playerBlackAuthCode)) {
            reversiGame.move(desiredMove, Constants.BLACK_PLAYER);
        } else if (authCode.equals(playerWhiteAuthCode)) {
            reversiGame.move(desiredMove, Constants.WHITE_PLAYER);
        } else {
            throw new WrongCodeException();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Directs start operation to ReversiGame class instance
     *
     * @throws  AlreadyStartedException  if game is already started
     */
    public void start() throws AlreadyStartedException {

        playerBlackAuthCode = generateAuthCode();
        playerWhiteAuthCode = generateAuthCode();
        cancellationCode    = generateAuthCode() + generateAuthCode();

        reversiGame.start();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Creates simple, readable, 8 chars length, random codes
     *
     * @return  created random authentication code
     */
    private String generateAuthCode() {

        StringBuffer authCode = new StringBuffer();
        Random       random   = new Random();

        for (int i = 0; i < 4; i++) {
            authCode.append(ALPHABET.charAt(random.nextInt(26)));
        }

        for (int i = 0; i < 4; i++) {
            authCode.append(random.nextInt(10));
        }

        return authCode.toString();
    }
}
