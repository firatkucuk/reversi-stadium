
package im.firat.reversistadium.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import im.firat.reversistadium.exceptions.*;
import im.firat.reversistadium.game.Game;
import im.firat.reversistadium.game.ReversiGame;
import im.firat.reversistadium.game.SingletonGame;
import java.io.IOException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class RestController {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private Game                game;
    private ServletOutputStream out;
    private HttpServletRequest  request;
    private HttpServletResponse response;
    private ReversiGame         reversiGame;



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

            response.setContentType("application/json");
            out.print(new GsonBuilder().create().toJson(reversiGame));
        } catch (NotStartedException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (WrongCodeException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void movePiece(String authCode, String piece) throws IOException {

        try {
            game.move(authCode, piece);

            response.setContentType("application/json");
            out.print(new GsonBuilder().create().toJson(reversiGame));
        } catch (NotStartedException e) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
        } catch (WrongCodeException ex) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (WrongOrderException e) {
            response.sendError(HttpServletResponse.SC_FORBIDDEN);
        } catch (IllegalMoveException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void showGameStatus() throws IOException {

        response.setContentType("application/json");
        out.print(new GsonBuilder().create().toJson(reversiGame));
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void startGame() throws IOException {

        try {
            game.start();

            response.setContentType("application/json");
            out.print(new GsonBuilder().create().toJson(game));
        } catch (AlreadyStartedException ex) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
