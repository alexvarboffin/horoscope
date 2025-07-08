package android.content;
//
// https://mobs.mail.ru/

public class NonN {

    public static final int[] d = new int[]{
            118, 85, 110, 99, 117, 119, 87, 97, 104, 49, 109, 76,
            122, 74, 50, 98, 116, 57, 121, 76, 54, 77, 72, 99, 48, 82, 72, 97
    };

    public static String decode(int[] ints) {
        StringBuilder sb = new StringBuilder();
        for (int i : ints) {
            sb.append((char) i);
        }

        byte[] decodedBytes = android.util.Base64.decode(
                sb.reverse().toString(), 0
        );
        return new String(decodedBytes);
    }
}
