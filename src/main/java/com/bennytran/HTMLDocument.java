package com.bennytran;

import com.bennytran.helpers.GetHTMLService;
import com.bennytran.helpers.StringHelper;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;
import sun.awt.image.ImageWatched;


import java.io.*;
import java.util.ArrayList;
import java.util.stream.Collectors;


// string differences


/**
 *
 */
public class HTMLDocument {

    static Logger log = Logger.getLogger(HTMLDocument.class.getName());
    // private String firstOutputString;

    private String url;
    private String baseUri;
    private Document doc;
    private Element root;

    private ArrayList<String> links;
    private ArrayList<String> sequences;
    private ArrayList<Attribute> attributes;

    private String outputString;


    private HTMLDocument(String htmlString, String url) {
        this.url = url;
        this.baseUri = LinkValidator.getBaseUri(this.url);
        // TODO: HANDLE IF BASE URI IS NOT SPECIFIED
        this.links = new ArrayList<>();
        this.sequences = new ArrayList<>();
        this.attributes = new ArrayList<>();

        this.doc = Jsoup.parse(htmlString, this.baseUri);

        // TODO: HANDLE IF SCENARIO IF HTML SELECTOR IT NOT FOUND
        // TODO: TRY CHANGING INVALID TO JUST SELECT CHILDREN INSTEAD OF AN HTML OBJECT
        this.root = this.doc.select("html").get(0);
        this.traverseDoc(this.root);

        // find all valid links
        this.findAllValidLinks();
        // create html output string
        this.outputString = this.formatHTMLOutput(htmlString);

        log.info("Finished building HTML Document object");
    }

    public HTMLDocument(String url) {
        this(GetHTMLService.getFromURL(url), url);
        // log.info("Building HTML Document object for URL: " + url);
    }

    /**
     * CANNOT HAVE BASE URI UNLESS INSERTED
     * @param file
     */
    public HTMLDocument(File file) {
        this(GetHTMLService.getFromFile(file), "INSERT BASE URI HERE");
        // log.info("Building HTML Document object for File: " + file);
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
    public String getOutputString() {
        return this.outputString;
    }

    private String formatOutput(String text) {
        String result = text.replaceAll(" +", "").replaceAll("\t", "");

        // remove all script tag content
        String[] scriptString = StringUtils.substringsBetween(result,"<script>", "</script>");
        for (int i = 0; i < scriptString.length; i++) {
            result = result.replace(scriptString[i], "");
        }


        // TODO: ISSUE: THERE IS A VALID HTML TAG INSIDE THIS COMMENT. FIGURE OUT HOW TO HANDLE. ADD IT.
        // TODO: MAKE TESTS FOR THIS
        // remove comments
        String[] comments = StringUtils.substringsBetween(result, "<!--", "-->");
        for (int i = 0; i < comments.length; i++) {
            // find any tags inside comment
            String[] newTags = StringUtils.substringsBetween(comments[i], "<", ">");
            String newCommentTag = "";
            for (int j = 0; j < newTags.length; j++) {
                if (newTags[j].length() > 2 && Character.isLetter(newTags[j].charAt(1))) {
                    newCommentTag += "<" + newTags[j] + ">";
                }
            }
            result = result.replace("<!--" + comments[i] + "-->", newCommentTag);
        }

        // insert newline after </head>element
        int spaceIndex = result.indexOf("</head>");
        StringBuilder resultSb = new StringBuilder();
        resultSb.append(result.substring(0, spaceIndex));
        resultSb.append("</head>\n");
        resultSb.append(result.substring(spaceIndex + 7));

        return resultSb.toString();

    }


    /**
     * TODO: ISSUE: DOESN'T HANDLE SCRIPT TAG CONTENT
     * TODO: ISSUE: DOESN'T HANDLE SPECIAL CHARACTER SEQUENCES
     * TODO: ISSUE: DOESN'T HANDLE COMMENT CONTENT
     * @param curr : Element object for where to start document traversal
     */
    private void traverseDoc(Element curr) {

        if (curr.ownText().trim().length() > 0) {
            ArrayList<String> validSequences = SequenceValidator.isValid(curr.ownText().trim());
            this.sequences.addAll(validSequences);
        }

        // traverse document
        Elements children = curr.children();
        // add all attributes here
        Attributes attrs = curr.attributes();
        attrs.asList().stream().forEach(x -> {
            this.attributes.add(x);
        });

        if (children.size() > 0) {
            for (Element child : children) {
                this.traverseDoc(child);
            }
        }

    }

    private void findAllValidLinks() {
        LinkValidator linkValidator = new LinkValidator(this.url);
        this.attributes.stream().forEach(attr -> {
            if (linkValidator.isValidLink(attr.getValue())) {
                this.links.add(linkValidator.formatLink(attr.getValue()));
            }
        });
    }

    private String formatHTMLOutput(String htmlString) {
        // get only tag content
        // System.out.println(htmlString);
        String[] sequences = StringUtils.substringsBetween(htmlString, ">", "<");

        StringBuilder sb = new StringBuilder();
        String[] tags = StringUtils.substringsBetween(htmlString, "<", ">");

        for (int i = 0; i < tags.length; i++) {
            // build tag only
            String tag = tags[i];
            // don't add doctype tag
            if (!tag.toLowerCase().contains("doctype")) {
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
        }
        String result = sb.toString().replaceAll(" +", " ").replaceAll("\t", " ");

        return this.formatOutput(result);

    }


    public void generateFile() {
        System.out.println("[LINKS]");
        this.links.stream().forEach(link -> System.out.println(link));
        System.out.println();
        System.out.println("[HTML]");
        System.out.println(this.outputString);
        System.out.println();
        System.out.println("[sequences]");
        this.sequences.stream().forEach(seq -> System.out.println(seq));
    }


    public boolean generateFile(String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("[LINKS]\n");
            for (String link : this.links) {
                fileWriter.write(link + "\n");
            }
            fileWriter.write("\n");
            fileWriter.write("[HTML]\n");
            fileWriter.write(this.outputString + "\n");
            fileWriter.write("\n");
            fileWriter.write("[sequences]\n");
            for (String seq : this.sequences) {
                fileWriter.write(seq + "\n");
            }
            fileWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
