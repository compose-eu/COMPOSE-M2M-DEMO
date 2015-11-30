import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.bdigital.compose.sdk.demo.client.ZWaveClient;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorCode;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorData;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorRegister;
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

	Map<String, String> devices = client.getDevices();
	System.out.println("Devices: " + devices);

	Assert.assertNotNull(devices);

    }

    @Test
    public void testGetInstances() {

	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");

	Map<String, String> devices = client.getDevices();
	for (String id : devices.keySet()) {
	    Set<String> instances = client.getIntances(id);
	    System.out.println("Instances for Device[" + id + "]: " + instances);

	    Assert.assertNotNull(instances);
	}

    }

    @Test
    public void testGetCommandClasses() {

	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");

	Map<String, String> devices = client.getDevices();
	for (String idDevice : devices.keySet()) {
	    Set<String> instances = client.getIntances(idDevice);
	    for (String idInstance : instances) {
		Set<String> dchannels = client.getCommandClasses(idDevice, idInstance);
		System.out.println("DataChannels for Device[" + idDevice + "] and Instance[" + idInstance + "]: " + dchannels);

		Assert.assertNotNull(dchannels);
	    }

	}

    }

    @Test
    public void testGetDataChannels() {

	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");

	Map<String, String> devices = client.getDevices();
	for (String idDevice : devices.keySet()) {
	    Set<String> instances = client.getIntances(idDevice);
	    for (String idInstance : instances) {
		Set<String> dchannels = client.getCommandClasses(idDevice, idInstance);
		for (String idChannel : dchannels) {
		    Set<String> dchannel = client.getDataChannels(idDevice, idInstance, idChannel);
		    System.out.println("DataChannel for Device[" + idDevice + "] and Instance[" + idInstance + "] and Channel[" + idChannel + "]: " + dchannel);

		    Assert.assertNotNull(dchannel);
		}
	    }
	}

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

	Map<String, String> devices = client.getDevices();
	for (String idDevice : devices.keySet()) {
	    Set<ZWaveSensorCode> sensors = client.discoverSensors(idDevice);
	    for (ZWaveSensorCode zWaveSensorCode : sensors) {
		ZWaveSensorData data = client.getDevicesData(zWaveSensorCode);
		System.out.println("Type: " + data.getType());
		System.out.println("Value: " + data.getValue());
		System.out.println("Unit: " + data.getUnit());
		System.out.println("Time: " + data.getUpdateTime());
		Assert.assertNotNull(data);
	    }

	}

    }

    @Test
    public void testDiscover() {

	ZWaveClient client = new ZWaveClient("http://raspberrypi.local:8083");

	Map<String, String> devices = client.getDevices();
	for (String idDevice : devices.keySet()) {

	    Set<ZWaveSensorCode> sensors = client.discoverSensors(idDevice);

	    System.out.println("Discover: " + sensors);
	    Assert.assertTrue(sensors.size() > 0);

	}
    }

}
