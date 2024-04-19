package com.example.kriptoprojekat;

import java.util.*;

public class Algorithms {

    public static String railFence(String text, int rails) {
        char[][] fence = new char[rails][text.length()];
        for (int i = 0; i < rails; i++) {
            for (int j = 0; j < text.length(); j++) {
                fence[i][j] = '\n';
            }
        }

        boolean down = false;
        int row = 0, col = 0;
        for (int i = 0; i < text.length(); i++) {
            if (row == 0 || row == rails - 1) {
                down = !down;
            }
            fence[row][col++] = text.charAt(i);
            row += down ? 1 : -1;
        }

        StringBuilder sifrat = new StringBuilder();
        for (int i = 0; i < rails; i++) {
            for (int j = 0; j < text.length(); j++) {
                if (fence[i][j] != '\n') {
                    sifrat.append(fence[i][j]);
                }
            }
        }
        return sifrat.toString();
    }

    public static String myszkowski(String key, String text) {
        return processEncryption(key, text);
    }

    private static String processEncryption(String key, String text) {
        int[] positions = processKey(key.toLowerCase());
        char[][] box = createTextBox(positions, text.toLowerCase().replaceAll(" ", ""));
        HashMap<Integer, CharCount> info = findCountsAndPositions(positions);
        StringBuilder cipher = new StringBuilder();

        for (int i = 0; i < info.size(); i++) {
            for (int row = 0; row < box.length; row++) {
                for (int col : info.get(i).positions) {
                    if (box[row][col] != '\0') {
                        cipher.append(box[row][col]);
                    }
                }
            }
        }

        return cipher.toString();
    }

    private static int[] processKey(String key) {
        String alphabet = "abcdefghijklmnopqrstuvwxyz";
        int[] positions = new int[key.length()];
        int num = 0;

        for (char c : alphabet.toCharArray()) {
            int control = 0;
            for (int i = 0; i < key.length(); i++) {
                if (key.charAt(i) == c) {
                    control = 1;
                    positions[i] = num;
                }
            }
            if (control == 1) {
                num++;
            }
        }

        return positions;
    }

    private static HashMap<Integer, CharCount> findCountsAndPositions(int[] positions) {
        HashMap<Integer, CharCount> organized = new HashMap<>();

        for (int i = 0; i < positions.length; i++) {
            int pos = positions[i];
            if (!organized.containsKey(pos)) {
                organized.put(pos, new CharCount());
            }
            organized.get(pos).count++;
            organized.get(pos).positions.add(i);
        }

        return organized;
    }

    private static char[][] createTextBox(int[] positions, String text) {
        int rows = (text.length() + positions.length - 1) / positions.length;
        char[][] box = new char[rows][positions.length];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < positions.length; j++) {
                if (text.length() > 0) {
                    box[i][j] = text.charAt(0);
                    text = text.substring(1);
                }
            }
        }

        return box;
    }

    private static class CharCount {
        int count;
        ArrayList<Integer> positions;

        CharCount() {
            count = 0;
            positions = new ArrayList<>();
        }
    }

    public static String playFair(String key, String plainText) {
        // convert all the characters to lowercase
        key = key.toLowerCase();
        plainText = plainText.toLowerCase().replaceAll("\\s", "");

        if(plainText.length() % 2 != 0)
            plainText += "x";

        // function to remove duplicate characters from the key
        key = cleanPlayFairKey(key);

        // function to generate playfair cipher key table
        char[][] matrix = generateCipherKey(key);

        // function to preprocess plaintext
        plainText = formatPlainText(plainText);

        // function to group every two characters
        String[] msgPairs = formPairs(plainText);

        // function to get position of character in key table and encrypt message
        return encryptMessage(matrix, msgPairs);
    }

    private static String cleanPlayFairKey(String key) {
        LinkedHashSet<Character> set = new LinkedHashSet<>();

        for (int i = 0; i < key.length(); i++)
            set.add(key.charAt(i));

        StringBuilder newKey = new StringBuilder();
        Iterator<Character> it = set.iterator();

        while (it.hasNext())
            newKey.append(it.next());

        return newKey.toString();
    }

    private static char[][] generateCipherKey(String key) {
        Set<Character> set = new HashSet<>();

        for (int i = 0; i < key.length(); i++) {
            if (key.charAt(i) == 'j')
                continue;
            set.add(key.charAt(i));
        }

        // remove repeated characters from the cipher key
        String tempKey = key;

        for (int i = 0; i < 26; i++) {
            char ch = (char) (i + 97);
            if (ch == 'j')
                continue;

            if (!set.contains(ch))
                tempKey += ch;
        }

        // create cipher key table
        char[][] matrix = new char[5][5];
        for (int i = 0, idx = 0; i < 5; i++)
            for (int j = 0; j < 5; j++)
                matrix[i][j] = tempKey.charAt(idx++);

        return matrix;
    }

    private static String formatPlainText(String plainText) {
        StringBuilder message = new StringBuilder();
        int len = plainText.length();

        for (int i = 0; i < len; i++) {
            // if plaintext contains the character 'j', replace it with 'i'
            if (plainText.charAt(i) == 'j')
                message.append('i');
            else
                message.append(plainText.charAt(i));
        }

        // if two consecutive characters are same, then insert character 'x' in between them
        for (int i = 0; i < message.length(); i += 2) {
            if (message.charAt(i) == message.charAt(i + 1))
                message.insert(i + 1, 'x');
        }

        // make the plaintext of even length
        if (len % 2 == 1)
            message.append('x'); // dummy character

        return message.toString();
    }

    private static String[] formPairs(String message) {
        int len = message.length();
        String[] pairs = new String[len / 2];

        for (int i = 0, cnt = 0; i < len / 2; i++)
            pairs[i] = message.substring(cnt, cnt += 2);

        return pairs;
    }

    private static String encryptMessage(char[][] matrix, String[] msgPairs) {
        StringBuilder encText = new StringBuilder();

        for (String pair : msgPairs) {
            char ch1 = pair.charAt(0);
            char ch2 = pair.charAt(1);
            int[] ch1Pos = getCharPos(ch1, matrix);
            int[] ch2Pos = getCharPos(ch2, matrix);

            // if both the characters are in the same row
            if (ch1Pos[0] == ch2Pos[0]) {
                ch1Pos[1] = (ch1Pos[1] + 1) % 5;
                ch2Pos[1] = (ch2Pos[1] + 1) % 5;
            }

            // if both the characters are in the same column
            else if (ch1Pos[1] == ch2Pos[1]) {
                ch1Pos[0] = (ch1Pos[0] + 1) % 5;
                ch2Pos[0] = (ch2Pos[0] + 1) % 5;
            }

            // if both the characters are in different rows and columns
            else {
                int temp = ch1Pos[1];
                ch1Pos[1] = ch2Pos[1];
                ch2Pos[1] = temp;
            }

            // get the corresponding cipher characters from the key matrix
            encText.append(matrix[ch1Pos[0]][ch1Pos[1]]).append(matrix[ch2Pos[0]][ch2Pos[1]]);
        }

        return encText.toString();
    }

    private static int[] getCharPos(char ch, char[][] matrix) {
        int[] keyPos = new int[2];

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (matrix[i][j] == ch) {
                    keyPos[0] = i;
                    keyPos[1] = j;
                    return keyPos;
                }
            }
        }
        return keyPos;
    }
}

