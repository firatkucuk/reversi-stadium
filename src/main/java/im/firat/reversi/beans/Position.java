
package im.firat.reversi.beans;


import java.io.Serializable;



public class Position implements Serializable {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    /** Use serialVersionUID for interoperability. */
    private static final long serialVersionUID = 1L;

    private static final String BOARD_LETTERS = "abcdefgh";
    private static final String BOARD_NUMBERS = "12345678";
    private static final char[] BOARD_CHARS   = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h' };



    //~ --- [INSTANCE FIELDS] ------------------------------------------------------------------------------------------

    private final int col;
    private final int row;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Position(final String piece) {

        final char[] chars = piece.toCharArray();

        this.col = BOARD_LETTERS.indexOf(chars[0]);
        this.row = BOARD_NUMBERS.indexOf(chars[1]);
    }



    public Position(final int row, final int col) {

        this.row = row;
        this.col = col;
    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public int getCol() {

        return col;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public int getRow() {

        return row;
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        final StringBuilder text = new StringBuilder();

        text.append(BOARD_CHARS[col]);
        text.append(row + 1);

        return text.toString();
    }
}
