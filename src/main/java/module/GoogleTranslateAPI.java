package module;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class GoogleTranslateAPI {
    public static void main(String[] args) {
        try {
            String text = "You";
            String targetLanguage = "vi";
            String translation = translate(text, targetLanguage);
            System.out.println("Original: " + text);
            System.out.println("Translation: " + translation);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String translate(String text, String targetLanguage) throws Exception {
        String url = "https://translate.googleapis.com/translate_a/single?client=gtx&sl=auto&tl="
                + URLEncoder.encode(targetLanguage, "UTF-8") + "&dt=t&q=" + URLEncoder.encode(text, "UTF-8");
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        // Set the request method
        con.setRequestMethod("GET");

        // Get the response
        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null) {
            response.append(inputLine);
        }
        in.close();

        // Parse the JSON response and extract the translated text
        String[] parts = response.toString().split("\"");
        if (parts.length >= 2) {
            return parts[1];
        } else {
            return "Translation not available";
        }
    }
}
