
package im.firat.reversi.reversistadium.controllers;


import im.firat.reversi.domain.Authorization;
import im.firat.reversi.domain.Game;
import im.firat.reversi.exceptions.*;
import im.firat.reversi.reversistadium.repositories.AuthorizationRepository;
import im.firat.reversi.reversistadium.repositories.GameRepository;
import javax.ws.rs.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static javax.ws.rs.core.Response.Status;



/**
 * This is main service class. This can provide restful web service for external applications.
 */
@Path("/")
public final class GameController {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final Logger LOG = LoggerFactory.getLogger(GameController.class);



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    // TODO: CDI needed
    private final AuthorizationRepository authorizationRepository = new AuthorizationRepository();
    private final GameRepository          gameRepository          = new GameRepository();



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameController() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    @DELETE
    @Path("/cancel/{cancellationCode}")
    @Produces("application/json")
    public Game cancel(@PathParam("cancellationCode") final String cancellationCode) {

        try {

            authorizationRepository.cancel(cancellationCode);
            gameRepository.cancel();

            LOG.info("Game cancelled!");

            return gameRepository.status();
        } catch (WrongCodeException e) {
            LOG.warn("Cancellation aborted. Wrong cancellation code.!");
            throw new WebApplicationException(e, Status.FORBIDDEN);
        } catch (NotStartedException e) {
            LOG.warn("Cancellation aborted. No active status found!");
            throw new WebApplicationException(e, Status.NOT_FOUND);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @PUT
    @Path("/move/{authCode}/{piece}")
    @Produces("application/json")
    public Game move(@PathParam("authCode") final String authCode,
            @PathParam("piece") final String piece) {

        try {

            final int playerColor = authorizationRepository.move(authCode);
            gameRepository.move(piece, playerColor);

            final Game game = gameRepository.status();

            LOG.info("Player(" + authCode + ") moved disk to location " + piece);
            LOG.debug("\n" + game);

            return game;
        } catch (NotStartedException e) {
            LOG.warn("Move aborted. No active status found!");
            throw new WebApplicationException(e, Status.NOT_FOUND);
        } catch (WrongCodeException e) {
            LOG.warn("Move aborted. Wrong authentication code!");
            throw new WebApplicationException(e, Status.FORBIDDEN);
        } catch (WrongOrderException e) {
            LOG.warn("Move aborted. Wrong authentication code!");
            throw new WebApplicationException(e, Status.FORBIDDEN);
        } catch (IllegalMoveException e) {
            LOG.warn("Move aborted. Requested illegal move!");
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @POST
    @Path("/start")
    @Produces("application/json")
    public Authorization start() {

        try {
            authorizationRepository.start();
            gameRepository.start();

            final Authorization authorizationInfo = authorizationRepository.get();
            Game                game              = gameRepository.status();

            LOG.info("New game started!");
            LOG.info("Player Black Auth Code: " + authorizationInfo.getPlayerBlackAuthCode());
            LOG.info("Player White Auth Code: " + authorizationInfo.getPlayerWhiteAuthCode());
            LOG.info("Cancellation Code: " + authorizationInfo.getCancellationCode());

            LOG.debug("\n" + game);

            return authorizationInfo;
        } catch (AlreadyStartedException e) {
            LOG.warn("Game start aborted! There is already a running game.");
            throw new WebApplicationException(e, Status.BAD_REQUEST);
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @GET
    @Path("/status")
    @Produces("application/json")
    public Game status() {

        return gameRepository.status();
    }
}
