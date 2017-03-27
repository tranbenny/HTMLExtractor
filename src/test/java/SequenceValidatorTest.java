import com.bennytran.HTMLDocument;
import org.junit.*;

import java.net.MalformedURLException;
import java.util.ArrayList;


import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 *
 */
public class SequenceValidatorTest {

    private final String[] urls = new String[] {
        "https://pitchbook.com/about-pitchbook",
        "https://pitchbook.com",
        "google.com"
    };

    private HTMLDocument htmlDocument;
    private ArrayList<String> sequences;

    @Before
    public void setUp() throws MalformedURLException {
        htmlDocument = new HTMLDocument(urls[0]);
        sequences = htmlDocument.getSequences();
    }

    @Test
    public void testAllCapitalLetters() {
        for (String x: sequences) {
            // check the first character and every character after a space is capitalized
            assertThat("first character of sequence should be capitalized", x.charAt(0), is(Character.toUpperCase(x.charAt(0))));
            for (int i = 1; i < x.length(); i++) {
                if (x.charAt(i - 1) == ' ') {
                    assertThat("character after space should be capitalized", x.charAt(i), is(Character.toUpperCase(x.charAt(i))));
                }
            }
        }
    }

    @Test
    public void testSequenceHasMinTwoWords() {
        sequences.stream().forEach(x -> {
            assertThat("word contains at least once space", x.contains(" "), is(true));
            assertThat("sequence has at least 2 words", x.split(" ").length > 1, is(true));
        });
    }


    @Test
    public void testNoPunctuationShouldBeSame() {
        // remove all punctuation
        for (String seq : sequences) {
            String removedPunc = seq.replaceAll("\\p{P}", "");
            Assert.assertThat(seq, is(removedPunc));
        }
    }

    @Test
    public void testNoCharactersExceptLettersAndNumbers() {
        for (String seq: sequences) {
            assertThat("sequences should not contain non-letters and non-numbers",
                    seq.matches("[^0-9a-zA-Z]"), is(false));
        }
    }


}
