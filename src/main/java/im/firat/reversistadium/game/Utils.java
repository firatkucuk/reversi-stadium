
package im.firat.reversistadium.game;

import java.util.List;

import static im.firat.reversistadium.game.Constants.*;



public class Utils {



    //~ --- [CONSTRUCTORS] ---------------------------------------------------------------------------------------------

    public Utils() {

    }



    //~ --- [METHODS] --------------------------------------------------------------------------------------------------

    public static String getBoardStateRepresentation(List<List<Integer>> boardState) {

        String text = "  a b c d e f g h" + "\n";

        for (int i = 0; i < 8; i++) {

            text += (i + 1) + " ";

            for (int j = 0; j < 8; j++) {
                int currentPlaceValue = boardState.get(i).get(j);

                if (currentPlaceValue == EMPTY_PLACE) {
                    text += "- ";
                } else if (currentPlaceValue == WHITE_DISK) {
                    text += "O ";
                } else if (currentPlaceValue == BLACK_DISK) {
                    text += "X ";
                }
            }

            text += " " + (i + 1) + "\n";
        }

        text += "  a b c d e f g h" + "\n";

        return text;
    }
}
