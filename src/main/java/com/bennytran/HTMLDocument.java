package com.bennytran;

import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.*;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.stream.Collectors;

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


    // TODO: add common constructor initialization here
    private HTMLDocument() {
        this.links = new ArrayList<>();
        this.tags = new ArrayList<>();
        this.sequences = new ArrayList<>();
    }

    public HTMLDocument(String url) {
        this();
//        this.url = url;
        log.info("Building HTML Document object for URL: " + url);
        try {
            this.doc = Jsoup.connect(url).get();
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
            this.traverseHTMLDocument(this.root);
            // this.setup();
        } catch (IOException e) {
            // TODO: handle error
            log.error(getStackTrace(e));
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
        return this.tags;
    }

    /**
     *
     * @return
     */
    public ArrayList<String> getSequences() {
        return this.sequences;
    }


    public void printAllNodes() {
        this.traverseHTMLDocument(this.root);
//        System.out.println(this.attributes.size());
//        System.out.println(this.htmlTags);
        System.out.println(this.sequences);
    }

    // TODO: make sure there is not #root tag
    private void traverseHTMLDocument(Element currentNode) {
        Elements children = currentNode.children();
        this.tags.add("<" + currentNode.nodeName() + ">");
        addLink(currentNode);
        if (children.size() == 0) {
            addContent(currentNode);
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
        String text = node.text();
        // run sequence validation
         ArrayList<String> validSequences = SequenceValidator.isValid(text);
        if (validSequences.size() > 0) {
            this.sequences.addAll(validSequences);
        }
//        this.sequences.add(text);

    }










}
