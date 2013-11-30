
package im.firat.reversi.core;

import im.firat.reversi.beans.Position;
import im.firat.reversi.services.GameService;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;



/**
 * Contains simple utility methods
 */
public final class Utils {



    //~ --- [STATIC FIELDS/INITIALIZERS] -------------------------------------------------------------------------------

    private static final String ALPHABET   = "abcdefghijklmnopqrstuvwxyz";
    private static final String ROW_HEADER = "  a b c d e f g h" + "\n";



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    private Utils() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    /**
     * Creates simple, readable, 8 chars length, random codes
     *
     * @return  created random authentication code
     */
    public static String generateAuthCode() {

        final StringBuilder authCode = new StringBuilder();
        final Random        random   = new Random();

        for (int i = 0; i < 4; i++) {
            authCode.append(ALPHABET.charAt(random.nextInt(26)));
        }

        for (int i = 0; i < 4; i++) {
            authCode.append(random.nextInt(10));
        }

        return authCode.toString();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    /**
     * Returns a beautified string representation of reversi game board state
     *
     * @param   boardState  reversi game board state
     *
     * @return  returns board state in matrix form string representation
     */
    public static String getBoardStateRepresentation(final List<List<Integer>> boardState) {

        if (boardState == null) {
            return null;
        }

        final StringBuilder text = new StringBuilder().append(ROW_HEADER);

        for (int row = 0, rowValue = 1; row < 8; row++, rowValue++) {

            text.append(rowValue).append(" ");

            for (int col = 0; col < 8; col++) {
                final int currentPlaceValue = boardState.get(row).get(col);

                if (currentPlaceValue == GameService.EMPTY_PLACE) {
                    text.append("- ");
                } else if (currentPlaceValue == GameService.WHITE_DISK) {
                    text.append("O ");
                } else if (currentPlaceValue == GameService.BLACK_DISK) {
                    text.append("X ");
                }
            }

            text.append(" ").append(rowValue).append("\n");
        }

        return text.append(ROW_HEADER).toString();
    }



    //~ ----------------------------------------------------------------------------------------------------------------

    public static List<String> positionSet2StringList(Set<Position> positionSet) {

        List<String> keyList = new ArrayList<String>();

        for (Position position : positionSet) {
            keyList.add(position.toString());
        }

        return keyList;
    }
}
