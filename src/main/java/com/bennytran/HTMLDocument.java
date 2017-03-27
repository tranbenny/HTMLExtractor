package com.bennytran;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Properties;


import static com.bennytran.helpers.GetHTMLService.getFromFile;
import static com.bennytran.helpers.GetHTMLService.getFromURL;


/**
 *
 */
public class HTMLDocument implements HTMLDocumentInterface {

    // static Logger log = Logger.getLogger(HTMLDocument.class.getName());

    private final String propertyFile = "/app.properties";

    private String url;
    private String baseUri;
    private Document doc;

    private ArrayList<String> links = new ArrayList<>();
    private ArrayList<String> sequences = new ArrayList<>();
    private ArrayList<Attribute> attributes = new ArrayList<>();
    private String outputString;

    private LinkValidator linkValidator;


    /**
     *
     * @param url
     */
    public HTMLDocument(String url) throws MalformedURLException {
        linkValidator = new LinkValidator(url);
        this.url = url;
        // add some validation/link fixing
        this.setup(getFromURL(linkValidator.createFullLink(url)));
    }

    /**
     * TODO: REMOVE. USED FOR TESTING PURPOSES
     * @param file
     */
    public HTMLDocument(File file) {
        try {
            this.setup(getFromFile(file));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param htmlString
     */
    private void setup(String htmlString) throws MalformedURLException {
        this.links.clear();
        this.sequences.clear();
        this.attributes.clear();

        if (this.url != null) {
            this.linkValidator = new LinkValidator(this.url);
            this.baseUri = linkValidator.getBaseUri();
        }

        this.doc = Jsoup.parse(htmlString, baseUri);
        Element root = this.doc.child(0);
        if (!root.nodeName().equals("html") || this.doc.children().size() > 1) {
            throw new MalformedURLException("NonValid HTML Document Found");
        }
        this.traverseDoc(root);
        this.findAllValidLinks();
        this.outputString = this.formatHTMLOutput(htmlString);
    }

    // PUBLIC METHODS

    /**
     * @param url
     * @return
     */
    public boolean setUrl(String url) throws MalformedURLException {
        this.url = url;
        this.linkValidator.setUrl(this.url);
        this.setup(getFromURL(linkValidator.createFullLink(url)));
        return true;
    }


    public String getUrl() { return this.url; }
    public ArrayList<String> getLinks() {
        return this.links;
    }
    public ArrayList<String> getSequences() {
        return this.sequences;
    }
    public String getOutputString() {
        return this.outputString;
    }


/*===========================================================
PRIVATE METHODS
=============================================================*/


    /**
     *
      * @param text
     * @return
     */
    private String formatOutput(String text) {
        String result = text.replaceAll(" +", "").replaceAll("\t", "");

        // remove all script tag content
        String[] scriptString = StringUtils.substringsBetween(result,"<script>", "</script>");
        if (scriptString != null) {
            for (int i = 0; i < scriptString.length; i++) {
                result = result.replace(scriptString[i], "");
            }
        }

        // remove comments
        String[] comments = StringUtils.substringsBetween(result, "<!--", "-->");
        if (comments != null) {
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
     * @param curr : Element object for where to start document traversal
     */
    private void traverseDoc(Element curr) {
        if (curr != null) {
            if (curr.ownText().trim().length() > 0) {
                ArrayList<String> validSequences = SequenceValidator.getValidSequences(curr.ownText().trim());
                this.sequences.addAll(validSequences);
            }

            Elements children = curr.children();
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

    }

    /**
     *
     */
    private void findAllValidLinks() {
        LinkValidator linkValidator = null;
        try {
            linkValidator = new LinkValidator(this.url);
            for (Attribute attr : this.attributes) {
                if (linkValidator.isValidLink(attr.getValue())) {
                    this.links.add(linkValidator.formatRelativeLink(attr.getValue()));
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            // TODO: HANDLE ERROR LOGGING HERE
        }
    }

    /**
     *
     * @param htmlString
     * @return
     */
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
        System.out.println("[LINKS]");
        this.links.stream().forEach(x -> System.out.println(x));
        System.out.println();
        System.out.println("[HTML]");
        System.out.println(this.outputString);
        System.out.println();
        System.out.println("[SEQUENCES]");
        this.sequences.stream().forEach(x -> System.out.println(x));
    }


    public boolean generateFile(String fileName) {
        try {
            FileWriter fileWriter = new FileWriter(fileName);
            fileWriter.write("[links]\n");
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
