import com.bennytran.Client;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import java.io.File;


public class ClientTest {



    @Test(expected = IllegalArgumentException.class)
    public void testDoesNotRunWithInvalidArgs() {
        String[] args = new String[3];
        Client.main(args);
    }

}
