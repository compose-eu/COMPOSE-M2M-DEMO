package org.bdigital.compose.sdk.demo.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.bdigital.compose.sdk.demo.model.ZWaveSensorCode;
import org.bdigital.compose.sdk.demo.model.ZWaveSensorData;
import org.bdigital.compose.sdk.demo.model.ZWaveSensorRegister;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ZWaveClient {

    private final String ZWAVE_HOST = "http://raspberrypi.local:8083";
    private final String ZWAVE_API = "/ZWaveAPI";
    private final String ZWAVE_API_RUN = "/Run";
    private final String ZWAVE_API_RUN_DEVICES = "devices";
    private final String ZWAVE_API_RUN_INSTANCES = "instances";
    private final String ZWAVE_API_RUN_COMMANDCLASSES = "commandClasses";
    private final String ZWAVE_API_RUN_DATA = "data";
    private final String ZWAVE_API_DATA = "/Data";

    public Set<String> getDevices() {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = ZWAVE_HOST + ZWAVE_API + ZWAVE_API_DATA + "/0";

	ResponseEntity<ZWaveSensorRegister> response = restTemplate.exchange(url, HttpMethod.GET, request, ZWaveSensorRegister.class);

	ZWaveSensorRegister body = response.getBody();

	Set<String> devices = body.getDevices().keySet();
	devices.remove("1");
	return devices;

    }

    public Set<String> getIntances(String deviceCode) {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = ZWAVE_HOST + ZWAVE_API + ZWAVE_API_RUN + "/" + ZWAVE_API_RUN_DEVICES + "[" + deviceCode + "]";

	ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

	HashMap<String, HashMap<String, Object>> body = response.getBody();

	Set<String> instances = body.get("instances").keySet();
	instances.remove("0");

	return instances;

    }

    public String getDataChannel(String deviceCode, String instanceCode) {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = ZWAVE_HOST + ZWAVE_API + ZWAVE_API_RUN + "/" + ZWAVE_API_RUN_DEVICES + "[" + deviceCode + "]" + "." + ZWAVE_API_RUN_INSTANCES + "[" + instanceCode + "]" + "."
		+ ZWAVE_API_RUN_COMMANDCLASSES + "[" + "49" + "]";

	ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

	HashMap<String, HashMap<String, Object>> body = response.getBody();

	if (body == null) {
	    return null;
	}

	Set<Entry<String, Object>> list = body.get("data").entrySet();
	for (Entry<String, Object> entry : list) {
	    if (NumberUtils.isNumber(entry.getKey())) {
		return entry.getKey();
	    }
	}

	return null;

    }

    public ZWaveSensorData getDevicesData(ZWaveSensorCode sensorCode) {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = ZWAVE_HOST + ZWAVE_API + ZWAVE_API_RUN + "/" + ZWAVE_API_RUN_DEVICES + "[" + sensorCode.getDeviceCode() + "]" + "." + ZWAVE_API_RUN_INSTANCES + "[" + sensorCode.getInstanceCode()
		+ "]" + "." + ZWAVE_API_RUN_COMMANDCLASSES + "[" + sensorCode.getCommandClasses() + "]" + "." + ZWAVE_API_RUN_DATA + "[" + sensorCode.getData() + "]";

	ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

	HashMap body = response.getBody();
	ZWaveSensorData sensor = new ZWaveSensorData();

	sensor.setUpdateTime((Number) body.get("updateTime"));
	sensor.setType(((Map<String, String>) body.get("sensorTypeString")).get("value"));
	sensor.setUnit(((Map<String, String>) body.get("scaleString")).get("value"));
	sensor.setValue(((Map<String, Number>) body.get("val")).get("value"));

	return sensor;

    }

    public ResponseEntity<ZWaveSensorRegister> getAllData() {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = ZWAVE_HOST + ZWAVE_API + ZWAVE_API_DATA + "/0";

	ResponseEntity<ZWaveSensorRegister> response = restTemplate.exchange(url, HttpMethod.GET, request, ZWaveSensorRegister.class);

	return response;

    }

    public Set<ZWaveSensorCode> discoverSensors(String deviceCode) {

	HashSet<ZWaveSensorCode> sensors = new HashSet<ZWaveSensorCode>();

	Set<String> instances = this.getIntances(deviceCode);

	for (String instanceCode : instances) {

	    String dataChannel = this.getDataChannel(deviceCode, instanceCode);
	    if (dataChannel != null) {
		ZWaveSensorCode sensor = new ZWaveSensorCode(deviceCode, instanceCode, "49", dataChannel);
		sensors.add(sensor);
	    }
	}

	return sensors;
    }

    private RestTemplate getRestTemplate() {
	RestTemplate restTemplate = new RestTemplate();
	restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());

	return restTemplate;
    }

    private MultiValueMap<String, String> getHeaders() {

	MultiValueMap<String, String> headers = new LinkedMultiValueMap<String, String>();
	headers.add("Accept", MediaType.APPLICATION_JSON_VALUE);

	return headers;

    }
}
