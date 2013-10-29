
package im.firat.reversistadium.game;


public class Constants {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    public static final int END_OF_DIRECTION = -1;

    // Board square states
    public static final int EMPTY_PLACE = 0;
    public static final int BLACK_DISK  = 1;
    public static final int WHITE_DISK  = 2;

    // Players
    public static final int NO_PLAYER    = 0;
    public static final int BLACK_PLAYER = 1;
    public static final int WHITE_PLAYER = 2;

    // Direction states
    public static final int DS_NO_VALID_PATH          = -1;
    public static final int DS_START                  = 0;
    public static final int DS_WAITING_FOR_SAME_COLOR = 1;
    public static final int DS_OCCUPIED               = 2;



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    private Constants() {

    }
}
