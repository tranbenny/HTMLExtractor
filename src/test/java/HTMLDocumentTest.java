import com.bennytran.HTMLDocument;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;

import java.util.ArrayList;
import java.util.Scanner;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.fail;

/**
 *
 */
public class HTMLDocumentTest {

    private final String url = "https://pitchbook.com/about-pitchbook";
    private final String url2 = "https://www.google.com";

    private HTMLDocument htmlDocument;
    private File generatedFile;
    private String fileName;

    @Before
    public void setup() throws MalformedURLException {
        htmlDocument = new HTMLDocument(url);
        this.fileName = "output.txt";
        htmlDocument.generateFile(fileName);
        String fullPath = System.getProperty("user.dir") + "/" + fileName;

        generatedFile = new File(fullPath);
    }

    @Test
    public void testCreatingNewHTMLDocumentObjectsWithUrl() throws MalformedURLException {
        assertThat("Get url should match url that was passed as param", htmlDocument.getUrl(), is(url));
        htmlDocument.setUrl(url2);
        assertThat("set url should match new url that was set", htmlDocument.getUrl(), is(url2));
        htmlDocument.setUrl("amazon.com");
        assertThat("passed url without protocol should still be the same", htmlDocument.getUrl(), is("amazon.com"));
    }

    @Test
    public void testCreatingFile() {
        assertThat("file should have been created", generatedFile.exists(), is(true));
        String generatedFileName = this.generatedFile.getName();
        assertThat("generated file name should match original file name", generatedFileName, is(fileName));
    }

    @Test
    public void testFileContainsRequiredSections() {
        ArrayList<String> tokens = new ArrayList<String>();
        try {
            Scanner fileScanner = new Scanner(generatedFile);
            while (fileScanner.hasNextLine()) {
                tokens.add(fileScanner.nextLine());
            }
            fileScanner.close();
            assertThat("contains links section", tokens.contains("[links]"), is(true));
            assertThat("contains html section", tokens.contains("[HTML]"), is(true));
            assertThat("contains sequences section", tokens.contains("[sequences]"), is(true));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testFileContentMatchesHTMLObjectContent() {
        ArrayList<String> linksTokens = new ArrayList<>();
        ArrayList<String> htmlTokens = new ArrayList<>();
        ArrayList<String> sequenceTokens = new ArrayList<>();

        try {
            Scanner fileScanner = new Scanner(generatedFile);
            String currSection = "";
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine().trim();
                if (line.equals("[links]") || line.equals("[HTML]") || line.equals("[sequences]")) {
                    currSection = line;
                } else if (!line.equals("")) {
                    switch(currSection) {
                        case "[links]": linksTokens.add(line.trim());
                            break;
                        case "[HTML]": htmlTokens.add(line.trim());
                            break;
                        case "[sequences]": sequenceTokens.add(line.trim());
                            break;
                    }
                }
            }

            fileScanner.close();

            StringBuilder htmlString = new StringBuilder();
            htmlTokens.stream().forEach(x -> htmlString.append(x));

            ArrayList<String> links = htmlDocument.getLinks();
            assertThat("generated links should match file links size", linksTokens.size(), is(links.size()));
            for (int i = 0; i < links.size(); i++) {
                assertThat("links should exactly match", linksTokens.get(i), is(links.get(i)));
            }

            ArrayList<String> sequences = htmlDocument.getSequences();
            assertThat("generated sequence size should match sequence token size", sequenceTokens.size(), is(sequences.size()));
            for (int i = 0; i < sequences.size(); i++) {
                assertThat("sequences should exactly match", sequenceTokens.get(i), is(sequences.get(i)));
            }

            String docOutputString = htmlDocument.getOutputString().replaceAll("\\s+", "");
            assertThat("html output without spaces should match", htmlString.toString(), is(docOutputString));

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Test
    public void testHTMLDocumentWorksWithAllURLTypes() {
        String fullURL = "https://www.pitchbook.com";
        String withoutProtocol = "pitchbook.com";
        String halfURL = "www.pitchbook.com";
        try {
            HTMLDocument htmlDocument = new HTMLDocument(fullURL);
            assertThat("url value should match with valid url", htmlDocument.getUrl(), is(fullURL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("object creation failed with valid full url: " + fullURL);
        }
        try {
            HTMLDocument htmlDocument2 = new HTMLDocument(withoutProtocol);
            assertThat("url value should match without protocol in url", htmlDocument2.getUrl(), is(withoutProtocol));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("object creation failed with valid non protocol url: " + withoutProtocol);
        }
        try {
            HTMLDocument htmlDocument3 = new HTMLDocument(halfURL);
            assertThat("url value should match with partly complete valid url", htmlDocument3.getUrl(), is(halfURL));
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("object creation failed with valid partly formed url: " + halfURL);
        }

    }



    @After
    public void cleanup() {
        this.generatedFile.delete();
    }








}
