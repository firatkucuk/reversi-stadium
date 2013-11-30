
package im.firat.reversi.beans;


import java.io.Serializable;



/**
 * This is POJO for occupiable paths in the board
 */
public final class Path implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private final int      direction;
    private final Position position;
    private final int      step;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Path(final Position position, final int direction, final int step) {

        this.position  = position;
        this.direction = direction;
        this.step      = step;
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public int getDirection() {

        return direction;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public Position getPosition() {

        return position;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int getStep() {

        return step;
    }
}
