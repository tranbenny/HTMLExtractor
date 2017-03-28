package com.bennytran;


import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.*;
import java.net.MalformedURLException;
import java.util.ArrayList;


import static com.bennytran.helpers.GetHTMLService.getFromFile;
import static com.bennytran.helpers.GetHTMLService.getFromURL;


/**
 * HTMLDocument Object for saving instance of passed urls
 * Creates and holds htmloutput string, sequences, links, and other elements of html page
 */
public class HTMLDocument implements HTMLDocumentInterface {

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
     * @param url: String value of url link to build values from
     * @throws MalformedURLException for invalid urls
     */
    public HTMLDocument(String url) throws MalformedURLException {
        linkValidator = new LinkValidator(url);
        this.url = url;
        // add some validation/link fixing
        this.setup(getFromURL(linkValidator.createFullLink(url)));
    }

    /**
     * Constructor for building values from input file. Used with testing specific testcases
     * @param file: input file
     * @param baseURI: base uri value for formatting relative links found in file
     */
    public HTMLDocument(File file, String baseURI) {
        try {
            this.url = baseURI;
            this.setup(getFromFile(file));
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Adds all htmloutput strings, sequences, and links to document fields
     * @param htmlString: string value of html content
     * @throws MalformedURLException if base uri or link validation is performed on
     *  an invalid link
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

    /**
     * setter for url. Reupdates field values to hold generated information from new url
     * @param url: url link string
     * @return: boolean value on successful setting of new url
     * @throws MalformedURLException if input url is invalid
     */
    public boolean setUrl(String url) throws MalformedURLException {
        this.url = url;
        this.linkValidator.setUrl(this.url);
        this.setup(getFromURL(linkValidator.createFullLink(url)));
        return true;
    }

    /**
     * Getter function for url
     * @return String url
     */
    public String getUrl() { return this.url; }

    /**
     * Getter function for all links found
     * @return list of link String values
     */
    public ArrayList<String> getLinks() {
        return this.links;
    }

    /**
     * Getter function for all sequences found
     * @return list of sequence string values
     */
    public ArrayList<String> getSequences() {
        return this.sequences;
    }

    /**
     * Getter function for html output string formatted
     * @return String htmloutput value
     */
    public String getOutputString() {
        return this.outputString;
    }


    /**
     * Formatted html output string removes all content from in between tags, including content
     *  between script and comment tags. Output also has a new line character after the closing head
     *  head tag or before opening body tag
     * @param text : htmldocument with only tags
     * @return String value of html document stripped of all script and comment tags
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
                if (newTags != null) {
                    String newCommentTag = "";
                    for (int j = 0; j < newTags.length; j++) {
                        if (newTags[j].length() > 2 && Character.isLetter(newTags[j].charAt(1))) {
                            newCommentTag += "<" + newTags[j] + ">";
                        }
                    }
                    result = result.replace("<!--" + comments[i] + "-->", newCommentTag);
                }
            }
        }

        // insert newline after </head>element
        int spaceIndex = result.indexOf("</head>");
        if (spaceIndex != -1) {
            StringBuilder resultSb = new StringBuilder();
            resultSb.append(result.substring(0, spaceIndex));
            resultSb.append("</head>\n");
            resultSb.append(result.substring(spaceIndex + 7));
            return resultSb.toString();
        }
        return result;

    }

    /**
     * Runs through current node and all its children to grab all text values and
     *  attributes from nodes
     * @param curr: current node at point of document traversal
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
     * Goes through all attributes found in html document to extract all valid links
     * MalformedURLException if invalid url is found
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
        }
    }

    /**
     * @param htmlString: String value of html document
     * @return String value with only the tags from html document
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
     *
     * @param fileName String value of file to be created
     * @return boolean value on whether successful file generation
     */
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
}
