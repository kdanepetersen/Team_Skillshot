package android.skillshot.com.skillshot;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUrlIsValidEndpoint(){
        String url_locations = "https://skill-shot-dev.herokuapp.com/locations.json";
        assertEquals("www.google.com", url_locations);

    }
//
//    @Test
//    public void testIfDataIsExtractedFromTheEndPoint(){
//
//
//    }




}