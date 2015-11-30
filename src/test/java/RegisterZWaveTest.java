import static org.junit.Assert.*;

import org.bdigital.compose.sdk.demo.client.ComposeClient;
import org.bdigital.compose.sdk.demo.model.compose.ZWaveEverspringCSO;
import org.bdigital.compose.sdk.exception.HttpErrorException;
import org.bdigital.compose.sdk.model.serviceobject.response.ComposeSORegisteredResponse;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class RegisterZWaveTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testRegisterDigi() throws HttpErrorException {
	
	System.out.println("====================================");
	System.out.println("Registering ZWave...");
	System.out.println("====================================");
	
	ComposeClient register = new ComposeClient();
	
	ZWaveEverspringCSO digiObject = new ZWaveEverspringCSO("http://foo.bar");
	
	ComposeSORegisteredResponse result = register.registerDigi(digiObject);
	
	System.out.println("====================================");
	System.out.println("Result:");
	System.out.println(result);
	
	Assert.assertNotNull(result);
    }

}
