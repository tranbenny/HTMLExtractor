import com.bennytran.HTMLDocument;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * TEST CASES TO ADD:
 * - HANDLE SPACES INSIDE ATTRIBUTES WITHIN THE HTML TAG
 * - HANDLE LINKS INSIDE THE ATTRIBUTE AND TEXT CONTAINED IN HTML TAGS
 * - HANDLE QUOTED AND UNQUOTED ATTRIBUTES
 */
public class HTMLDocumentTest {

    private final String url = "https://pitchbook.com/about-pitchbook";
    private final String url2 = "https://www.google.com";

    @Before
    public void setup() {}

    @Test
    public void testCreatingNewHTMLDocumentObjectsWithUrl() {
        HTMLDocument htmlDocument = new HTMLDocument(url);
        assertThat("Get url should match url that was passed as param", htmlDocument.getUrl(), is(url));
        htmlDocument.setUrl(url2);
        assertThat("set url should match new url that was set", htmlDocument.getUrl(), is(url));
    }

    @Test
    public void testCreateNewHTMLDocumentObjectWithFile() {}




}
