
package im.firat.reversistadium.controllers;

import im.firat.reversistadium.exceptions.*;
import im.firat.reversistadium.game.Game;
import im.firat.reversistadium.game.ReversiGame;
import im.firat.reversistadium.game.SingletonGame;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



public class RestController {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(RestController.class);



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private final Game                game;
    private final ServletOutputStream out;
    private final HttpServletRequest  request;
    private final HttpServletResponse response;
    private final ReversiGame         reversiGame;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public RestController(HttpServletRequest request, HttpServletResponse response) throws IOException {

        this.game        = SingletonGame.getInstance();
        this.reversiGame = game.getReversiGame();
        this.request     = request;
        this.response    = response;
        this.out         = response.getOutputStream();
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public void cancelGame(String cancellationCode) throws IOException {

        try {
            game.cancel(cancellationCode);
            LOG.info("Game cancelled!");

            response.setContentType("application/json");
            out.print(new ObjectMapper().writeValueAsString(reversiGame));
        } catch (NotStartedException e) {
            LOG.warn("Cancellation aborted. No active game found!");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (WrongCodeException ex) {
            LOG.warn("Cancellation aborted. Wrong cancellation code.!");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void movePiece(String authCode, String piece) throws IOException {

        try {
            game.move(authCode, piece);
            LOG.info("Player(" + authCode + ") moved disk to location " + piece);
            LOG.debug("\n" + game.getReversiGame());

            response.setContentType("application/json");
            out.print(new ObjectMapper().writeValueAsString(reversiGame));
        } catch (NotStartedException e) {
            LOG.warn("Move aborted. No active game found!");
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (WrongCodeException ex) {
            LOG.warn("Move aborted. Wrong authentication code!");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (WrongOrderException e) {
            LOG.warn("Move aborted. Wrong authentication code!");
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (IllegalMoveException e) {
            LOG.warn("Move aborted. Requested illegal move!");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void showGameStatus() throws IOException {

        response.setContentType("application/json");
        out.print(new ObjectMapper().writeValueAsString(reversiGame));
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void startGame() throws IOException {

        try {
            game.start();
            LOG.info("New game started!");
            LOG.info("Cancellation Code: " + game.getCancellationCode());
            LOG.info("Player Black Auth Code: " + game.getPlayerBlackAuthCode());
            LOG.info("Player White Auth Code: " + game.getPlayerWhiteAuthCode());
            LOG.debug("\n" + game.getReversiGame());

            response.setContentType("application/json");
            out.print(new ObjectMapper().writeValueAsString(game));
        } catch (AlreadyStartedException ex) {
            LOG.warn("Game start aborted! There is already a running game.");
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
