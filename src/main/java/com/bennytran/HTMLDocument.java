package com.bennytran;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;


import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.stream.Collectors;


// string differences


import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;


/**
 * Created by bennytran on 3/16/17.
 */
public class HTMLDocument {

    static Logger log = Logger.getLogger(HTMLDocument.class.getName());



    private Document doc;
    private Element root;

    private ArrayList<String> links;
    private ArrayList<String> tags;
    private ArrayList<String> sequences;

    private String ogHTMLString;
    private String outputString;
    private ArrayList<String> htmlArray;

    private HashMap<String, Integer> tagCounts = new HashMap<>();
    private ArrayList<Element> selfClosingTags = new ArrayList<Element>();



    // TODO: add common constructor initialization here
    private HTMLDocument() {
        this.links = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.sequences = new ArrayList<>();
        // set the parsing settings on jsoup parser
    }


    public HTMLDocument(String url) {
        this();
//        this.url = url;
        log.info("Building HTML Document object for URL: " + url);
        try {
            this.doc = Jsoup.connect(url).get();
            System.out.println(this.doc.outerHtml());
            // TODO: this should always be 0, need to handle the invalid document test case
            // TODO: handle invalid requests
            this.root = this.doc.select("html").get(0);
            this.traverseHTMLDocument(this.root);


        } catch (IOException e) {
            // TODO: Log to file
            log.error(getStackTrace(e));
        }


    }

    public HTMLDocument(File file) {
        this();
        log.info("Building HTML Document object for File: " + file);
        try {
            this.doc = Jsoup.parse(file, "UTF-8", "");

            this.root = this.doc.select("html").get(0);
//            this.traverseHTMLDocument(this.root);
            // this.setup();
            // System.out.println(this.root.outerHtml());
            this.traverseWithTags(this.root);
            //System.out.println(this.root.outerHtml());
        } catch (IOException e) {
            // TODO: handle error
            log.error(getStackTrace(e));
        }
    }

    // TODO: fix issue with self closing tag not being sensed
    public HTMLDocument(boolean isHTML) {
        this();
        this.htmlArray = new ArrayList<>();
        // read in external file as a string
        StringBuilder sb = new StringBuilder();
        try {
            Scanner in = new Scanner(new File("/Users/bennytran/Documents/Projects/PitchBook/InterviewProject/src/main/resources/HTMLTestFiles/Test3.html"));
            while (in.hasNextLine()) {
                String line = in.nextLine();
                sb.append(line);
                this.htmlArray.add(line.trim());
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        this.ogHTMLString = sb.toString();
        this.outputString = sb.toString();


        this.doc = Jsoup.parse(sb.toString());
        this.doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        // System.out.println(this.doc.outerHtml().replaceAll(" +", "").replaceAll("\n",""));
//        System.out.println(this.doc.outerHtml());
        this.root = this.doc.select("html").get(0);
        this.traverseWithTags(this.root);
        // this.outputString = this.root.outerHtml();
        System.out.println(this.outputString.replaceAll(" +", "").replaceAll("\n", ""));
        // this.doc = Jsoup.parse(html);
        // System.out.println(this.doc.outerHtml());

    }

    public String executeGetRequest(String urlString) {
        HttpURLConnection connection = null;
        StringBuffer response = new StringBuffer();

        try {
            URL obj = new URL(urlString);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();

            con.setRequestMethod("GET");

            int responseCode = con.getResponseCode();
            //System.out.println("Sending GET request to: " + urlString);
            //System.out.println("Response Code : " + responseCode);

            BufferedReader in = new BufferedReader(
                new InputStreamReader(con.getInputStream())
            );
            String inputLine;


            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // System.out.println(response.toString());


        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response.toString();

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
        return this.tags;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getSequences() {
        return this.sequences;
    }


    // TODO: make sure there is not #root tag
    // TODO: ISSUE:
    // traversal is automatically opening and closing tags. Needs to look at exactly what the tag is
    private void traverseHTMLDocument(Element currentNode) {
        Elements children = currentNode.children();
        this.tags.add("<" + currentNode.nodeName() + ">");
        Tag currentTag = currentNode.tag();
        if (currentTag.isSelfClosing()) {
            System.out.print("<" + currentNode.nodeName() + "/>");
        } else {
            System.out.print("<" + currentNode.nodeName() + ">");
        }
        addLink(currentNode);
        if (children.size() == 0) {
            if (currentNode.hasText()) {
                addContent(currentNode);
            }
            if (!currentTag.isSelfClosing()) {
                System.out.print("</" + currentNode.nodeName() + ">");
            }
            this.tags.add("</" + currentNode.nodeName() + ">");
        } else {
            for (Element child: children) {
                traverseHTMLDocument(child);
            }
            this.tags.add("</" + currentNode.nodeName() + ">");
        }
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
//        this.sequences.add(text);

    }

    private void traverseWithTags(Element curr) {
        // System.out.println(curr.outerHtml());
        // remove anything that is not in between <>
        // traverse document
        Elements children = curr.children();
        // remove all attributes here
        // System.out.println(curr.nodeName());
        /**
         * TODO: ISSUE: DOESN'T HANDLE UNSUPPORTED ATTRIBTES
         */
        if (!curr.tag().isSelfClosing()) {
            Attributes attrs = curr.attributes();
            for (Attribute attribute : attrs) {
                // ystem.out.println(attribute.toString());
                this.outputString.replace(attribute.toString(), "");
                curr.removeAttr(attribute.getKey());
            }
        } else {
            this.selfClosingTags.add(curr);
        }
        if (children.size() == 0) {
            // System.out.println(curr.nodeName());
            String innerText = curr.text();
            this.outputString = this.outputString.replace(innerText, "");
            curr.html("");
            // curr.attributes();
        } else {
            for (Element child: children) {
                traverseWithTags(child);
            }
        }
    }

    // TODO: create a method to handle self closing tags. DONE
    // TODO: handle scenario for all tags that are missing closing tags
    // check if the closing tag needs to be self closing or not
    // KNOWN: self-closing tags will have no text in between
    public void handleSelfClosingTags() {
        // og HTML version
//        System.out.println(this.ogHTMLString);
        // get the html version
        String htmlVersion = this.root.outerHtml();
//        System.out.println(htmlVersion);
//        System.out.println(this.selfClosingTags);
//        System.out.println(this.selfClosingTags);
        // HANDLE SELF CLOSING TAGS
        this.selfClosingTags.stream().forEach(x -> {
            // for each of these tags, figure out which is difference compared to the original
            // System.out.println(x.data());

            if (this.ogHTMLString.contains(x.toString())) {
                //System.out.println("MATCH");
                //System.out.println(x.toString());
                this.outputString = this.outputString.replace(x.toString(), "<" + x.nodeName() + ">");
            } else {
                // no match: this means that the original html string has a closing slash that is missing in my output
                //System.out.println("NO MATCH, NEED TO ADD CLOSING TAG");
                //System.out.println(x.toString());
                this.outputString = this.outputString.replace(x.toString(), "<" + x.nodeName() + "/>");
                // remove all the attributes from
            }


        });
        // System.out.println(this.outputString.replaceAll(" +", "").replaceAll("\n", ""));
        // get the xml version
        // this.doc.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        // String xmlVersion = this.root.outerHtml();
        // System.out.println(xmlVersion);

        /**
         * HOW TO READ HTML ONLY AS IS
         */
        // read each line


        // how can i get the self closing tags that need to be checked
        // find all the differences from the original string and the html version
        // Differences I care about:
        // -1 difference, and difference = "/"
//        DiffMatchPatch diffMatchPatch = new DiffMatchPatch();
//        List<DiffMatchPatch.Diff> differences = diffMatchPatch.diffMain(this.ogHTMLString, this.root.outerHtml());
//        for (DiffMatchPatch.Diff diff : differences) {
//            System.out.println(diff);
//        }


        // after i find this difference, it means that I removed the self / closing tag

        /**
         * HOW DO I KNOW WHICH TAG NEEDS TO ADD THE SLASH?
         */



    }

    private void addTagCount(String node) {
        if (this.tagCounts.containsKey(node)) {
            this.tagCounts.put(node, this.tagCounts.get(node) + 1);
        } else {
            this.tagCounts.put(node, 1);
        }
    }










}
