package com.bennytran;

import com.bennytran.helpers.GetHTMLService;
import com.bennytran.helpers.StringHelper;
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

        this.outputString = StringHelper.removeSpaces(htmlString);
        this.doc = Jsoup.parse(htmlString);
        // TODO: HANDLE IF HTML SELECTOR IT NOT FOUND
        this.root = this.doc.select("html").get(0);
        this.traverseDoc(this.root);
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
        return StringHelper.removeLineBreaks(this.outputString);
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

    private void traverseDoc(Element curr) {
        // traverse document
        Elements children = curr.children();
        // remove all attributes here
        Attributes attrs = curr.attributes();
        attrs.asList().stream().forEach(x -> {
        // TODO: refactor these combinations by removing all quotes. Then remove sequence
        String possibleCombination = x.getKey() + "=" + x.getValue().replaceAll(" +", "");
        String singleQuoteCombination = x.getKey() + "='" + x.getValue() + "'".replaceAll(" +", "");
        this.outputString = this.outputString.replace(possibleCombination, "");
        this.outputString = this.outputString.replace(singleQuoteCombination, "");
        this.outputString = this.outputString.replace(x.toString().replaceAll(" +", ""), "");

        });


        if (children.size() == 0) {
            if (curr.hasText()) {
                addContent(curr);
                this.sequences.add(curr.text());
                this.outputString = this.outputString.replace(StringHelper.removeSpaces(curr.text()), "");
            }
            curr.html("");
        } else {
            for (Element child: children) {
                this.traverseDoc(child);
            }
        }
    }

}
