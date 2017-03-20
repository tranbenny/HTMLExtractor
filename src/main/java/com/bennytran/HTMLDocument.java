package com.bennytran;

import com.bennytran.helpers.GetHTMLService;
import com.bennytran.helpers.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;


import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;


// string differences



/**
 *
 */
public class HTMLDocument {

    static Logger log = Logger.getLogger(HTMLDocument.class.getName());

    private String baseUri;
    private Document doc;
    private Element root;

    private ArrayList<String> links;
    private ArrayList<String> sequences;
    private ArrayList<Attribute> attributes;

    private String outputString;


    private HTMLDocument(String htmlString, String baseUri) {
        this.baseUri = baseUri;
        // TODO: HANDLE IF BASE URI IS NOT SPECIFIED
        this.links = new ArrayList<>();
        this.sequences = new ArrayList<>();
        this.attributes = new ArrayList<>();

        // this.outputString = StringHelper.removeSpaces(htmlString);
        this.outputString = htmlString.replaceAll(" +", " ").replaceAll("\t", " ");
        this.doc = Jsoup.parse(htmlString);
        // TODO: HANDLE IF HTML SELECTOR IT NOT FOUND
        this.root = this.doc.select("html").get(0);
        this.traverseDoc(this.root);
        /**
         * this.root.outerHTML doesn't work b/c it auto corrects self-closing tags, and auto-cleans the tags
         */
    }


    public HTMLDocument(String url) {
        this(GetHTMLService.getFromURL(url), "INSERT BASE URI HERE");
        log.info("Building HTML Document object for URL: " + url);
    }

    public HTMLDocument(File file) {
        this(GetHTMLService.getFromFile(file), "INSERT BASE URI HERE");
        log.info("Building HTML Document object for File: " + file);
    }


    public ArrayList<String> getLinks() {
        return this.links;
    }
    public ArrayList<String> getSequences() {
        return this.sequences;
    }

    public ArrayList<Attribute> getAttributes() {
        return this.attributes;
    }

    public String getHTMLString() {
        this.formatOutput();
        return StringHelper.removeSpaces(this.outputString);
        // return this.outputString;
    }

    private void formatOutput() {

        this.outputString = this.outputString.replaceAll(" +", "").replaceAll("\t", "");
        // remove script content
        // TODO: add script content to search for links
        String[] scriptString = StringUtils.substringsBetween(this.outputString,"<script>", "</script>");
        for (int i = 0; i < scriptString.length; i++) {
            //System.out.println(scriptString[i]);
            this.outputString = this.outputString.replace(scriptString[i], "");
        }

        // did not work
        String[] comments = StringUtils.substringsBetween(this.outputString, "<!--", "-->");
        for (int i = 0; i < comments.length; i++) {
            // System.out.println(comments[i]);
            // TODO: ISSUE NEED TO ESCAPE QUOTES HERE
//            <!--[ifltIE10]><linkhref="/css/ie.css?uq=rcvnUrNU"/><![endif]-->
            this.outputString = this.outputString.replace("<!--" + scriptString[i] + "-->", "");
        }


        //System.out.println(this.outputString.replaceAll(" +", "").replaceAll("\t", ""));


         System.out.println(this.outputString);

//        Elements scriptElements = this.doc.getElementsByTag("script");
//        for (Element scriptBlock : scriptElements) {
//            System.out.println(scriptBlock.outerHtml());
//        }




    }


    private void addLink(Element node) {
        Attributes attr = node.attributes();
        // TODO: add a URL validation/check here
        this.links.addAll(attr.asList().stream().map(
                x -> x.toString())
                .collect(Collectors.toCollection(ArrayList::new)));
    }

    private void addContent(Element node) {
        String text = node.ownText();
        // run sequence validation
         ArrayList<String> validSequences = SequenceValidator.isValid(text);
        if (validSequences.size() > 0) {
            this.sequences.addAll(validSequences);
        }
    }

    /**
     * TODO: ISSUE: DOESN'T HANDLE REMOVING SCRIPT TAG CONTENT
     * TODO: ISSUE: DOESN'T HANDLE SPECIAL CHARACTER SEQUENCES
     * TODO: ISSUE: DOESN'T HANDLE COMMENTS
     * TODO: ISSUE: DOESN'T HANDLE nested text inside elements with children
     * @param curr
     */
    private void traverseDoc(Element curr) {

        if (curr.hasText()) {
//            System.out.println("NEW NODE");
//            System.out.println(curr.ownText());
            this.outputString = this.outputString.replace(">" + curr.ownText() + "<", "><");
        }

        // traverse document
        Elements children = curr.children();
        // remove all attributes here
        Attributes attrs = curr.attributes();
        attrs.asList().stream().forEach(x -> {
            // TODO: refactor these combinations by removing all quotes. Then remove sequence
            String possibleCombination = x.getKey() + "=" + x.getValue(); // no quotes
            String singleQuoteCombination = x.getKey() + "='" + x.getValue() + "'"; // single quotes
            this.outputString = this.outputString.replace(possibleCombination, "");
            this.outputString = this.outputString.replace(singleQuoteCombination, "");
            this.outputString = this.outputString.replace(x.toString(), "");
            this.attributes.add(x);
            if (LinkValidator.isValidLink(x.getValue())) {
                this.links.add(x.getValue());
            }
        });

        if (children.size() == 0) {
            // TODO: grab all the text inside and add it to sequences
            if (curr.hasText()) {
                // System.out.println(curr.text());
                addContent(curr);
                this.sequences.add(curr.text());
//                this.outputString = this.outputString.replace(curr.text(), "");
            }
            curr.text("");
            // curr.html("");
        } else {
            for (Element child : children) {
                this.traverseDoc(child);
            }
        }

    }

}
