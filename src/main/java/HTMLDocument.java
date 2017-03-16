import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;


/**
 * Created by bennytran on 3/16/17.
 */
public class HTMLDocument {

    private String url;
    private Document doc;
    private ArrayList<String> links;
    private ArrayList<String> htmlTags;
    private ArrayList<String> sequences;


    public HTMLDocument(String url) {
        this.url = url;
        this.links = new ArrayList<String>();
        this.htmlTags = new ArrayList<String>();
        this.sequences = new ArrayList<String>();
        this.setup();
        this.getRawHTMLTags();
        this.findSequences();
//        this.fetchLinks();
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
//        int counter = 0;
        for (Element elem: elements) {
//            System.out.println(elem.tagName());
//            System.out.println(counter);
            System.out.println(elem);
//            counter++;
        }
    }

    /**
     *
     * @return list of links from html document
     */
    public ArrayList<String> getLinks() {
        return this.links;
    }

    /**
     *
     * @return list of links from html document
     */
    public ArrayList<String> getTags() {
        return this.htmlTags;
    }

    public ArrayList<String> getSequences() {
        return this.sequences;
    }

    /**
     * TODO: Write links to file
     * TODO: Get Links for all other types of attributes, f.e. script tags, css queries, etc.
     */
    private void fetchLinks() {
        Elements aTagLinks = doc.select("a[href]");
        for (Element link: aTagLinks) {
            String hrefText = link.attr("href");
            this.links.add(hrefText);
        }
    }

    /**
     * TODO: Needs to grab the opening and closing elements instead of just the tag name
     */
    private void getRawHTMLTags() {
        Elements elements = this.doc.select("*");
        for (Element elem : elements) {
            StringBuilder tagBuilder = new StringBuilder();
            String tag = elem.tagName();
            this.htmlTags.add(tag);
        }

    }

    private void findSequences() {
        // search through all text combinations inside html document
        Elements elements = this.doc.select("*");
        for (Element elem : elements) {
            String text = elem.text();
            this.sequences.add(text);
        }
    }



}
