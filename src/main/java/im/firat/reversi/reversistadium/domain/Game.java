
package im.firat.reversi.reversistadium.domain;

import im.firat.reversi.reversistadium.beans.Path;
import im.firat.reversi.reversistadium.core.Utils;
import im.firat.reversi.reversistadium.services.GameService;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.codehaus.jackson.annotate.JsonIgnore;



public class Game implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private Set<String>         availableMoves;
    private List<List<Integer>> boardState;
    private boolean             cancelled;
    private int                 currentPlayer;
    private boolean             started;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Game() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public Set<String> getAvailableMoves() {

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

    public void setAvailableMoves(Set<String> availableMoves) {

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
