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
    private String firstOutputString;

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
        this.firstOutputString = this.testFormat(htmlString);
        this.formatOutput(this.firstOutputString);


        this.outputString = htmlString.replaceAll(" +", " ").replaceAll("\t", " ");
        this.doc = Jsoup.parse(htmlString);
        // TODO: HANDLE IF HTML SELECTOR IT NOT FOUND
        this.root = this.doc.select("html").get(0);
        this.traverseDoc(this.root);


        // this.formatOutput(this.firstOutputString);
        /**
         * this.root.outerHTML doesn't work b/c it auto corrects self-closing tags, and auto-cleans the tags
         */
    }

    private String testFormat(String htmlString) {
        // get only tag content
        // System.out.println(htmlString);
        String[] sequences = StringUtils.substringsBetween(htmlString, ">", "<");

        StringBuilder sb = new StringBuilder();
        String[] tags = StringUtils.substringsBetween(htmlString, "<", ">");

        for (int i = 0; i < tags.length; i++) {
            // build tag only
            String tag = tags[i];
            int spaceIndex = tags[i].indexOf(' ');
            if (spaceIndex != -1) {
                // remove all attributes
                tag = tag.substring(0, spaceIndex);
            }
            // handle self-closing tags
            if (tags[i].endsWith("/")) {
                tag = tag + "/";
            }
            sb.append("<" + tag + ">");
        }
        String result = sb.toString().replaceAll(" +", " ").replaceAll("\t", " ");



        return result;

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

    private void formatOutput(String text) {
        String text2 = text.replaceAll(" +", "").replaceAll("\t", "");

        // removes all script tag content
        String[] scriptString = StringUtils.substringsBetween(text2,"<script>", "</script>");
        for (int i = 0; i < scriptString.length; i++) {
            //System.out.println(scriptString[i]);
            text2 = text2.replace(scriptString[i], "");
        }

        // remove comments
        // TODO: ISSUE: THERE IS A VALID HTML TAG INSIDE THIS COMMENT. FIGURE OUT HOW TO HANDLE. ADD IT.
        // TODO: MAKE TESTS FOR THIS
        String[] comments = StringUtils.substringsBetween(text2, "<!--", "-->");
        for (int i = 0; i < comments.length; i++) {
//            System.out.println(comments[i]);
            // find any tags inside comment
            String[] newTags = StringUtils.substringsBetween(comments[i], "<", ">");
            String newCommentTag = "";
            for (int j = 0; j < newTags.length; j++) {
                if (newTags[j].length() > 2 && Character.isLetter(newTags[j].charAt(1))) {
                    newCommentTag += "<" + newTags[j] + ">";
                }
            }

            // TODO: TEST IF IS WORKING FOR IF COMMENT INCLUDES VALID LINK, SECOND PARAMETER IN REPLACE SHOULD BE THE TAGS
            text2 = text2.replace("<!--" + comments[i] + "-->", newCommentTag);
        }

        System.out.println(text2);

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

        String[] inbetweenText = StringUtils.substringsBetween(this.outputString, ">", "<");
        for (int i = 0; i < inbetweenText.length; i++) {
            // System.out.println(inbetweenText[i]);
            this.outputString = this.outputString.replace(inbetweenText[i], "");
        }




        // System.out.println(this.outputString.replaceAll(" +", "").replaceAll("\t", ""));



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

        if (curr.ownText().trim().length() > 0) {
//            this.sequences.add(curr.ownText().trim());
            ArrayList<String> validSequences = SequenceValidator.isValid(curr.ownText().trim());
            this.sequences.addAll(validSequences);
//            if (validSequences.size() > 0) {
//                System.out.println();
//                System.out.println(curr.ownText().trim());
//                System.out.println(validSequences);
//            }
        }

        // traverse document
        Elements children = curr.children();
        // add all attributes here
        Attributes attrs = curr.attributes();
        attrs.asList().stream().forEach(x -> {
            this.attributes.add(x);
            System.out.println();
            System.out.println(x.getValue());
            if (LinkValidator.isValidLink(x.getValue())) {
                System.out.println("VALID LINK");
                this.links.add(x.getValue());
            } else {
                System.out.println("INVALID LINK");
            }
        });

        if (children.size() > 0) {
            for (Element child : children) {
                this.traverseDoc(child);
            }
        }

    }

}
