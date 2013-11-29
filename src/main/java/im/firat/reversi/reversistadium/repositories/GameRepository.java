
package im.firat.reversi.reversistadium.repositories;


import im.firat.reversi.reversistadium.dao.MemoryGameDaoImpl;
import im.firat.reversi.reversistadium.domain.Game;
import im.firat.reversi.reversistadium.exceptions.AlreadyStartedException;
import im.firat.reversi.reversistadium.exceptions.IllegalMoveException;
import im.firat.reversi.reversistadium.exceptions.NotStartedException;
import im.firat.reversi.reversistadium.exceptions.WrongOrderException;
import im.firat.reversi.reversistadium.services.GameService;



/**
 * This class contains main reversi game logic.
 */
public final class GameRepository {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    // TODO: CDI needed
    private final MemoryGameDaoImpl gameDao = new MemoryGameDaoImpl();



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public GameRepository() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    /**
     * Cancels running status, saves board state and current player until next status start
     *
     * @throws  NotStartedException  If there is no running status
     */
    public void cancel() throws NotStartedException {

        final Game        game        = gameDao.getCurrentGame();
        final GameService gameService = new GameService();

        gameService.cancel(game);
        gameDao.persist(game);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Provides players to move their disks.
     *
     * @param   piece  desired target move in string format c4, a8
     *
     * @throws  NotStartedException   if status is not started
     * @throws  IllegalMoveException  if actual move is illegal
     */
    public void move(final String piece, final int player) throws NotStartedException, IllegalMoveException,
        WrongOrderException {

        final Game        game        = gameDao.getCurrentGame();
        final GameService gameService = new GameService();

        gameService.move(game, piece, player);
        gameDao.persist(game);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void persist(Game game) {

        gameDao.persist(game);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Creates a new Reversi status and initialize all internal variables
     *
     * @throws  AlreadyStartedException  if status is already started
     */
    public void start() throws AlreadyStartedException {

        final Game        game        = gameDao.getCurrentGame();
        final GameService gameService = new GameService();

        gameService.start(game);
        gameDao.persist(game);
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Returns current reversi game status
     *
     * @return  returns game status
     */
    public Game status() {

        return gameDao.getCurrentGame();
    }
}
