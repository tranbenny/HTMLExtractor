import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


/**
 * Created by bennytran on 3/16/17.
 */
public class HTMLDocument {

    private String url;
    private Document doc;
    private Element root;
    private ArrayList<String> links;
    private ArrayList<String> htmlTags;
    private ArrayList<String> sequences;
    private ArrayList<String> resultSequences;


    public HTMLDocument(String url) {
        this.url = url;
        this.links = new ArrayList<>();
        this.htmlTags = new ArrayList<>();
        this.sequences = new ArrayList<>();
        this.resultSequences = new ArrayList<>();
        this.setup();
        this.getRawHTMLTags();
        this.findSequences();
//        this.fetchLinks();
    }

    public HTMLDocument(File file) {
        try {
            this.doc = Jsoup.parse(file, "UTF-8", "");
            this.root = this.doc.select("html").get(0);
            this.links = new ArrayList<>();
            this.htmlTags = new ArrayList<>();
            this.sequences = new ArrayList<>();
            this.resultSequences = new ArrayList<>();
            // this.setup();
        } catch (IOException e) {
            // TODO: handle error
            e.printStackTrace();
        }
    }

    // TODO: add common constructor initialization here
    private HTMLDocument() {

    }

    public String getTitle() {
        return this.doc.title();
    }

    private void setup() {
        // this.getHTMLDocument();
        try {
            this.doc = Jsoup.connect(this.url).get();
            // TODO: this should always be 0, need to handle the invalid document test case
            // TODO: handle invalid requests
            this.root = this.doc.select("html").get(0);
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
        return this.resultSequences;
    }

    public void compareSequences() {
        int index = 0;
        IntStream.range(0, this.sequences.size()).forEach(x -> {
            System.out.println("ORIGINAL");
            System.out.println(this.sequences.get(x));
            System.out.println("RESULT");
            System.out.println(this.resultSequences.get(x));
        });
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

    // TODO: Iterating through each element tag like this has repeats. Needs to skip these
    private void findSequences() {
        // search through all text combinations inside html document
        Elements elements = this.doc.select("*");
        for (Element elem : elements) {
            String text = elem.text();
            this.sequences.add(text);
            ArrayList<String> validSequences = isValidSequence(text);
            this.resultSequences.addAll(validSequences);
        }
//        this.resultSequences = this.sequences.stream().map((x) -> {
//            return isValidSequence(x);
//        }).collect(Collectors.toCollection(ArrayList::new));


    }

    /**
     * Sequence Criteria: Two or more words that have the first letter in each word capitalized. Word is defined as sequence
     *  of characters greater than 2 seperated by whitespace
     * @param text: input String text value
     * @return String value for valid sequence, otherwise returns empty string
     */
    private ArrayList<String> isValidSequence(String text) {
        String[] fullWords = text.split(" ");
        if (fullWords.length < 2) {
            return new ArrayList<String>();
        }
        ArrayList<Character> words = Arrays.asList(text.split(" ")).stream().map(x -> {
            return x.charAt(0);
        }).collect(Collectors.toCollection(ArrayList::new));
        ArrayList<String> results = new ArrayList<String>();
        StringBuilder result = new StringBuilder();
        int index = 0;
        while (index < words.size()) {
            if (words.get(index) == Character.toUpperCase(words.get(index))) {
                result.append(fullWords[index] + " ");
            } else {
                if (result.toString().split(" ").length >= 2) {
                    results.add(result.toString().trim());
                }
                result.setLength(0);
            }
            index++;
        }
        return results;


    }

    public void printAllNodes() {
        this.printAllNodes(this.root);
    }

    // ISSUE: prints opening head and body twice
    private void printAllNodes(Element currentNode) {
        Elements children = currentNode.children();
        System.out.println("<" + currentNode.nodeName() + ">");
        if (children.size() == 0) {
            System.out.println("</" + currentNode.nodeName() + ">");
        } else {
            // issue here: causes tags to be printed twice
            for (Element child: children) {
                // System.out.println("<" + child.nodeName() + ">");
                printAllNodes(child);
            }
            System.out.println("</" + currentNode.nodeName() + ">");
        }
    }










}
