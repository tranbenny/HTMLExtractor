import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;


/**
 * Created by bennytran on 3/16/17.
 */
public class HTMLDocument {

    private String url;
    private Document doc;


    public HTMLDocument(String url) {
        this.url = url;
        this.setup();
    }

    public String getTitle() {
        return this.doc.title();
    }

    private void setup() {
        // this.getHTMLDocument();
        try {
            this.doc = Jsoup.connect(this.url).get();
        } catch (IOException e) {
            // TODO: Log to file
            e.printStackTrace();
        }
    }


    /**
     * Prints all elements in an HTML Document. Prints outermost tag and then all of its inner tags until all elements
     * are printed.
     */
    public void printAllTags() {
        Elements elements = this.doc.select("*");
        int counter = 0;
        for (Element elem: elements) {
            System.out.println(counter);
            System.out.println(elem);
            counter++;
        }
    }



}
