import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tests {

    public static boolean isRTLMessage(String message) {
        if(message == null) return false;

        Pattern pattern =  Pattern.compile("\\p{IsArabic}|\\p{IsHebrew}");
        Matcher matcher = pattern.matcher(message);

        return matcher.find();
    }

    public static boolean isRTLChar(char character) {
        Pattern pattern =  Pattern.compile("\\p{IsArabic}|\\p{IsHebrew}");
        Matcher matcher = pattern.matcher(character + "");

        return matcher.find();
    }

    public static String fixRTLMessage(String[] words) {
        StringBuilder fixedMessage = new StringBuilder();

        for(int i = words.length - 1; i >= 0; i--) {
            if(isRTLMessage(words[i])) {
                fixRTLMessage(fixedMessage, words[i]);
            } else {
                fixedMessage.insert(0, words[i]);
            }

            fixedMessage.insert(0, " ");
        }

        return fixedMessage.toString();
    }

    private static String fixRTLMessage(StringBuilder builder, String message) {
        int rtlCounter = 0;

        for(int j = message.length() - 1; j >= 0; j--) {
            char c = message.charAt(j);
            if(isRTLChar(c)) {
                builder.insert(rtlCounter, c);
            } else {
                if(j == 0)
                    builder.insert(0, c);
                else
                    builder.insert(rtlCounter, c);
            }
            rtlCounter++;
        }

        return builder.toString();
    }

    public static void main(String[] args) {
        System.out.println(12_000);
    }
}
