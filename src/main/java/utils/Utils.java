package utils;

import dictionary.Word;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public class Utils {
    public String generateHtmlForWord(Word word) {
        String wordName = word.getWordFound();
        String html = "<h1>" + wordName + "</h1>" +
                "<h3><i>//</i></h3><ul><li>" + word.getWordExplaination() + "</li></ul>";
        return html;
    }
}
