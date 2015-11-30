import java.util.HashMap;
import java.util.Set;

import org.bdigital.compose.sdk.demo.client.ZWaveClient;
import org.bdigital.compose.sdk.demo.model.ZWaveSensorCode;
import org.bdigital.compose.sdk.demo.model.ZWaveSensorData;
import org.bdigital.compose.sdk.demo.model.ZWaveSensorRegister;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


public class ZWaveClientTest {

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetDevices() {
	
	
	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");
	
	Set<String> devices = client.getDevices();
	System.out.println("Devices: " + devices);
	
	Assert.assertNotNull(devices);
	
    }
    
    @Test
    public void testGetInstances() {
	
	
	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");
	
	Set<String> instances = client.getIntances("2");
	System.out.println("Instances: " + instances);
	
	Assert.assertNotNull(instances);
	
    }
    
    @Test
    public void testGetDataChannel() {
	
	
	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");
	
	String dchannel = client.getDataChannel("2", "1");
	System.out.println("DataChannel: " + dchannel);
	
	Assert.assertNotNull(dchannel);
	
    }

    @Test
    public void testGetData() {

	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");
	
	ResponseEntity<ZWaveSensorRegister> data = client.getAllData();
	System.out.println(data.getBody());
	
	Assert.assertNotNull(data);
	
    }
    
    @Test
    public void testGetDeviceData() {
	
	
	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");
	
	ZWaveSensorData data = client.getDevicesData(new ZWaveSensorCode("2", "0", "49", "1"));
	System.out.println("Type: " + data.getType());
	System.out.println("Value: " + data.getValue());
	System.out.println("Unit: " + data.getUnit());
	System.out.println("Time: " + data.getUpdateTime());
	
	Assert.assertNotNull(data);
	
	
	data = client.getDevicesData(new ZWaveSensorCode("2", "0", "49", "5"));
	System.out.println("Type: " + data.getType());
	System.out.println("Value: " + data.getValue());
	System.out.println("Unit: " + data.getUnit());
	System.out.println("Time: " + data.getUpdateTime());
	
	Assert.assertNotNull(data);
	
    }
    
    @Test
    public void testDiscover() {
	
	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");
	
	Set<ZWaveSensorCode> sensors = client.discoverSensors("2");
	
	System.out.println("Discover: " + sensors);
    }

}
