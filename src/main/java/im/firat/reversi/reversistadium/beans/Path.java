
package im.firat.reversi.reversistadium.beans;


import java.io.Serializable;



/**
 * This is POJO for occupiable paths in the board
 */
public final class Path implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private int direction;
    private int step;
    private int targetCol;
    private int targetRow;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Path(int targetRow, int targetCol, int direction, int step) {

        this.targetRow = targetRow;
        this.targetCol = targetCol;
        this.direction = direction;
        this.step      = step;
    }



    private Path() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public int getDirection() {

        return direction;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int getStep() {

        return step;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int getTargetCol() {

        return targetCol;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int getTargetRow() {

        return targetRow;
    }
}
