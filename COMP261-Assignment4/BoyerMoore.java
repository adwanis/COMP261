import java.util.*;

public class BoyerMoore{
    
     private static int[] preprocessBadCharacter(String pattern) {
        int[] badCharTable = new int[256];
        for (int i = 0; i < 256; i++) {
            badCharTable[i] = -1; 
        }
        for (int i = 0; i < pattern.length(); i++) {
            badCharTable[pattern.charAt(i)] = i; 
        }
        return badCharTable;
    }

    /**
     * Preprocess the pattern and create the good suffix shift table.
     */
    private static int[] preprocessGoodSuffix(String pattern) {
        int m = pattern.length();
        int[] goodSuffixTable = new int[m + 1];
        int[] borderPos = new int[m + 1];

        int i = m;
        int j = m + 1;
        borderPos[i] = j;

        while (i > 0) {
            while (j <= m && (i - 1 < 0 || j - 1 < 0 || pattern.charAt(i - 1) != pattern.charAt(j - 1))) {
                if (goodSuffixTable[j] == 0) {
                    goodSuffixTable[j] = j - i;
                }
                j = borderPos[j];
            }
            i--;
            j--;
            borderPos[i] = j;
        }

        j = borderPos[0];
        for (i = 0; i <= m; i++) {
            if (goodSuffixTable[i] == 0) {
                goodSuffixTable[i] = j;
            }
            if (i == j) {
                j = borderPos[j];
            }
        }

        return goodSuffixTable;
    }

    /**
     * Search the pattern in the given text using the Boyer-Moore algorithm.
     */
    public static int search(String pattern, String text) {
        int[] badCharTable = preprocessBadCharacter(pattern);
        int[] goodSuffixTable = preprocessGoodSuffix(pattern);

        int n = text.length();
        int m = pattern.length();
        int s = 0; 
        while (s <= (n - m)) {
            int j = m - 1;

            while (j >= 0 && pattern.charAt(j) == text.charAt(s + j)) {
                j--;
            }

            if (j < 0) {
                return s;
            } else {
                s += Math.max(goodSuffixTable[j + 1], j - badCharTable[text.charAt(s + j)]);
            }
        }

        return -1; 
    }

}
