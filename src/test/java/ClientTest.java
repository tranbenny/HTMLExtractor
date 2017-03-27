import com.bennytran.Client;
import org.junit.Test;

import java.io.File;
import java.net.MalformedURLException;

import static org.junit.Assert.fail;


public class ClientTest {


    @Test(expected = IllegalArgumentException.class)
    public void testDoesNotRunWithInvalidArgs() throws MalformedURLException {
        String[] args = new String[3];
        Client.main(args);
    }

    @Test(expected = MalformedURLException.class)
    public void testRunWithInvalidURL() throws MalformedURLException {
        String[] args = new String[] {
            "dsfindfn",
            "output.txt"
        };
        Client.main(args);
    }

    @Test
    public void testRunWithValidArguments() {
        String[] validArgs = new String[] {
            "https://www.pitchbook.com",
            "output.txt"
        };
        try {
            Client.main(validArgs);
            File file = new File(System.getProperty("user.dir") + "/output.txt");
            file.delete();
        } catch (MalformedURLException e) {
            e.printStackTrace();
            fail("run should not have failed with valid arguments");
        }
    }

}
