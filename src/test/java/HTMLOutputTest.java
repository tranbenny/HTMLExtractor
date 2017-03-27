import com.bennytran.HTMLDocument;
import org.apache.commons.lang3.StringUtils;
import org.junit.Before;
import org.junit.Test;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
public class HTMLOutputTest {



    private HTMLDocument htmlDocument;
    private String url = "https://pitchbook.com/about-pitchbook";
//    private String url = "google.com";
    private String outputHTML;

    @Before
    public void setup() throws MalformedURLException {
        htmlDocument = new HTMLDocument(url);
        outputHTML = htmlDocument.getOutputString();
    }

    @Test
    public void testNoContentOutsideTags() {
        String[] nonTagContent = StringUtils.substringsBetween(outputHTML, ">", "<");
        ArrayList<String> nonTagContentFilter = new ArrayList<String>(Arrays.asList(nonTagContent));
        for (String content : nonTagContent) {
            assertThat("should only be white space in between tags", content.trim(), is(""));
        }
    }

    @Test
    public void testHTMLTagsDoNotContainSpaces() {
        for (int i = 0; i < outputHTML.length(); i++) {
            assertThat("character should not be space", outputHTML.charAt(i) == ' ', is(false));
        }
    }

    @Test
    public void testHTMLContainsOnlyOneNewLine() {
        int numNewLines = 0;
        for (int i = 0; i < outputHTML.length(); i++) {
            if (outputHTML.charAt(i) == '\n') {
                numNewLines++;
            }
        }
        assertThat("should only contain one newline", numNewLines, is(1));
    }

    @Test
    public void testNewLineSeperatesHeadAndBody() {
        assertThat("output starts with html tag", outputHTML.startsWith("<html>"), is(true));
        assertThat("output ends with closing html tag", outputHTML.endsWith("</html>"), is(true));
        int newLineIndex = outputHTML.indexOf('\n');
        String bodyTag = outputHTML.substring(newLineIndex + 1, newLineIndex + 7);
        String closingHeadTag = outputHTML.substring(newLineIndex - 7, newLineIndex);
        assertThat("element after newline should be body tag", bodyTag, is("<body>"));
        assertThat("element before newline should be closing head tag", closingHeadTag, is("</head>"));
    }

    @Test
    public void testTagsContainNoAttributes() {
        assertThat("should contain no equal signs for attributes", outputHTML.contains("="), is(false));
        assertThat("should contain no quotes for attributes", outputHTML.contains("\""), is(false));
    }

}
