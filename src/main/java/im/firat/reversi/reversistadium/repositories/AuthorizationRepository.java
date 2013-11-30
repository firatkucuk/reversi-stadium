package im.firat.reversi.reversistadium.repositories;


import im.firat.reversi.core.Utils;
import im.firat.reversi.domain.Authorization;
import im.firat.reversi.exceptions.WrongCodeException;
import im.firat.reversi.reversistadium.dao.AuthorizationDao;
import im.firat.reversi.reversistadium.dao.MemoryAuthorizationDaoImpl;
import im.firat.reversi.services.GameService;



/**
 * This stateless service class is responsable for authorization mechanism of reversi game
 */
public final class AuthorizationRepository {



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    // TODO: CDI needed
    private final AuthorizationDao authorizationInfoDao = new MemoryAuthorizationDaoImpl();



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public AuthorizationRepository() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public void cancel(final String cancellationCode) throws WrongCodeException {

        final Authorization authorizationInfo = authorizationInfoDao.getCurrentGameAuthorization();

        if (!cancellationCode.equals(authorizationInfo.getCancellationCode())) {
            throw new WrongCodeException();
        }
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public Authorization get() {

        return authorizationInfoDao.getCurrentGameAuthorization();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int move(final String authCode) throws WrongCodeException {

        final Authorization authorizationInfo = authorizationInfoDao.getCurrentGameAuthorization();

        if (authCode.equals(authorizationInfo.getPlayerBlackAuthCode())) {
            return GameService.BLACK_PLAYER;
        } else if (authCode.equals(authorizationInfo.getPlayerWhiteAuthCode())) {
            return GameService.WHITE_PLAYER;
        }

        throw new WrongCodeException();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void start() {

        final Authorization authorizationInfo = authorizationInfoDao.getCurrentGameAuthorization();

        authorizationInfo.setPlayerBlackAuthCode(Utils.generateAuthCode());
        authorizationInfo.setPlayerWhiteAuthCode(Utils.generateAuthCode());
        authorizationInfo.setCancellationCode(Utils.generateAuthCode() + Utils.generateAuthCode());

        authorizationInfoDao.persist(authorizationInfo);
    }
}
