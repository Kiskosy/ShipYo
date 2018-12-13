package kosy.shipyo;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@RunWith(AndroidJUnit4.class)
public class loginTest {

    @Rule
    public ActivityTestRule<loginPage> rule  = new ActivityTestRule<>(loginPage.class);

    @Test
    public void authenticationTest1() throws Exception {
        String username = "test";
        String password = "start123";
        String result = new jsonPost().execute(username,password).get();
        assertEquals(result,"ok\n");
    }

    @Test
    public void authenticationTest2() throws Exception {
        String username = "test";
        String password = "start123";
        String result = new jsonPost().execute(username,password).get();
        assertEquals(result,"ok\n");
    }

}