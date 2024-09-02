
import java.util.*;

public class LempelZiv {
    
    private static final int WINDOW_SIZE = 100;
    private static final int LOOKAHEAD_BUFFER_SIZE = 8;
    /**
     * Take uncompressed input as a text string, compress it, and return it as a
     * text string.
     */
    public static String compress(String input) {
        // TODO fill this in.
        StringBuilder output = new StringBuilder();
        int cursor = 0;

        while (cursor < input.length()) {
            int matchLength = 0;
            int matchDistance = 0;

            int searchWindowStart = Math.max(0, cursor - WINDOW_SIZE);
            String searchWindow = input.substring(searchWindowStart, cursor);

            for (int length = 1; length <= LOOKAHEAD_BUFFER_SIZE && cursor + length <= input.length(); length++) {
                String substring = input.substring(cursor, cursor + length);
                int index = searchWindow.indexOf(substring);

                if (index != -1) {
                    matchDistance = cursor - (searchWindowStart + index);
                    matchLength = length;
                } else {
                    break;
                }
            }

            if (matchLength > 0) {
                output.append("[").append(matchDistance).append("|").append(matchLength).append("|");
                if (cursor + matchLength < input.length()) {
                    output.append(input.charAt(cursor + matchLength)).append("]");
                } else {
                    output.append("]");
                }
                cursor += matchLength + 1;
            } else {
                output.append("[0|0|").append(input.charAt(cursor)).append("]");
                cursor++;
            }
        }

        return output.toString();
    }

    /**
     * Take compressed input as a text string, decompress it, and return it as a
     * text string.
     */
    public static String decompress(String compressed) {
        // TODO fill this in.
        StringBuilder output = new StringBuilder();
        int cursor = 0;

        while (cursor < compressed.length()) {
            int start = compressed.indexOf('[', cursor);
            int end = compressed.indexOf(']', start);

            String[] tuple = compressed.substring(start + 1, end).split("\\|");

            int offset = Integer.parseInt(tuple[0]);
            int length = Integer.parseInt(tuple[1]);
            String nextChar = tuple.length > 2 ? tuple[2] : "";
            int startPosition = output.length() - offset;
            for (int i = 0; i < length; i++) {
                output.append(output.charAt(startPosition + i));
            }

            if (!nextChar.isEmpty()) {
                output.append(nextChar);
            }

            cursor = end + 1;
        }

        return output.toString();
    }
    


    /**
     * The getInformation method is here for your convenience, you don't need to
     * fill it in if you don't want to. It is called on every run and its return
     * value is displayed on-screen. You can use this to print out any relevant
     * information from your compression.
     */
    public String getInformation() {
        return "";
    }
}
