package com.bennytran;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.ArrayList;
import java.util.Properties;


import static com.bennytran.helpers.GetHTMLService.getFromFile;
import static com.bennytran.helpers.GetHTMLService.getFromURL;
import static com.bennytran.helpers.LinkValidator.getBaseUri;
import com.bennytran.helpers.LinkValidator;
import com.bennytran.helpers.SequenceValidator;


/**
 *
 */
public class HTMLDocument implements HTMLDocumentInterface {

    // static Logger log = Logger.getLogger(HTMLDocument.class.getName());

    private static final String propertyFile = "/app.properties";

    private String url;
    private String baseUri;
    private Document doc;

    private ArrayList<String> links = new ArrayList<>();
    private ArrayList<String> sequences = new ArrayList<>();
    private ArrayList<Attribute> attributes = new ArrayList<>();
    private String outputString;


    /**
     *
     * @param url
     */
    public HTMLDocument(String url) {
        this.url = url;
        this.setup(getFromURL(url));
    }

    /**
     * TODO: REMOVE. USED FOR TESTING PURPOSES
     * @param file
     */
    public HTMLDocument(File file) {
        this.setup(getFromFile(file));
    }
    
    /**
     *
     * @param htmlString
     */
    private void setup(String htmlString) {
        this.links.clear();
        this.sequences.clear();
        this.attributes.clear();

        if (this.url != null) {
            this.baseUri = getBaseUri(this.url);
        }

        this.doc = Jsoup.parse(htmlString, baseUri);
        // TODO: HANDLE IF SCENARIO IF HTML SELECTOR IT NOT FOUND
        // TODO: TRY CHANGING INVALID TO JUST SELECT CHILDREN INSTEAD OF AN HTML OBJECT
        Element root = this.doc.select("html").get(0);
        this.traverseDoc(root);
        this.findAllValidLinks();
        this.outputString = this.formatHTMLOutput(htmlString);
    }

    // PUBLIC METHODS

    /**
     * @param url
     * @return
     */
    public boolean setUrl(String url) {
        this.url = url;
        // if invalid, return false
        this.baseUri = getBaseUri(url);
        this.setup(getFromURL(url));
        return true;
    }


    public String getUrl() { return this.url; }
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


    /*
    PRIVATE METHODS
     */


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

    /**
     * TODO: DELETE THIS METHOD LATER
     */
    public void printValues() {
        System.out.println("LINKS");
        this.links.stream().forEach(x -> System.out.println(x));
        System.out.println();
        System.out.println("HTML");
        System.out.println(this.outputString);
        System.out.println();
        System.out.println("SEQUENCES");
        this.sequences.stream().forEach(x -> System.out.println(x));
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

    private void loadProperties() {
        Properties prop = new Properties();
        InputStream is = this.getClass().getResourceAsStream(propertyFile);
        try {
            prop.load(is);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
