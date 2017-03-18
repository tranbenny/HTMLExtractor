import org.junit.Test;

import static org.junit.Assert.assertFalse;


import static com.bennytran.LinkValidator.isValidLink;
import static org.junit.Assert.assertTrue;

/**
 * Created by bennytran on 3/15/17.
 */
public class LinkValidatorTest {

    @Test
    public void testLinkValidator() {
        String validURL = "";
        String invalidURL = "";
        assertTrue("Valid URL should return 200 http response", isValidLink(validURL));
        assertFalse("Invalid url should return incorrect http response", isValidLink(invalidURL));
    }

}
