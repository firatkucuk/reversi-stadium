
package im.firat.reversi.domain;

import im.firat.reversi.core.Utils;
import java.io.Serializable;
import java.util.List;



public class Game implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private List<String>        availableMoves;
    private List<List<Integer>> boardState;
    private boolean             cancelled;
    private int                 currentPlayer;
    private boolean             started;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Game() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public List<String> getAvailableMoves() {

        return availableMoves;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public List<List<Integer>> getBoardState() {

        return boardState;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int getCurrentPlayer() {

        return currentPlayer;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public boolean isCancelled() {

        return cancelled;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public boolean isStarted() {

        return started;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setAvailableMoves(List<String> availableMoves) {

        this.availableMoves = availableMoves;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setBoardState(List<List<Integer>> boardState) {

        this.boardState = boardState;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setCancelled(boolean cancelled) {

        this.cancelled = cancelled;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setCurrentPlayer(int currentPlayer) {

        this.currentPlayer = currentPlayer;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public void setStarted(boolean started) {

        this.started = started;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return Utils.getBoardStateRepresentation(boardState);
    }
}
