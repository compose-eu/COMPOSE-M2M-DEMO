package org.bdigital.compose.sdk.demo.client;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.commons.lang3.math.NumberUtils;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorCode;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorData;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorRegister;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

public class ZWaveClient {

    private String host;

    public ZWaveClient(String host) {
	super();
	this.host = host;
    }

    // private final String ZWAVE_HOST = "http://raspberrypi.local:8083";
    private final String ZWAVE_API = "/ZWaveAPI";
    private final String ZWAVE_API_RUN = "/Run";
    private final String ZWAVE_API_RUN_DEVICES = "devices";
    private final String ZWAVE_API_RUN_INSTANCES = "instances";
    private final String ZWAVE_API_RUN_COMMANDCLASSES = "commandClasses";
    private final String ZWAVE_API_RUN_DATA = "data";
    private final String ZWAVE_API_DATA = "/Data";

    public Map<String,String> getDevices() {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = host + ZWAVE_API + ZWAVE_API_DATA + "/0";

	ResponseEntity<ZWaveSensorRegister> response = restTemplate.exchange(url, HttpMethod.GET, request, ZWaveSensorRegister.class);

	ZWaveSensorRegister body = response.getBody();

	Map<String,String> devicesMap = new HashMap<String,String>(0);

	HashMap<String, HashMap<String, HashMap<String, Object>>> devices = body.getDevices();
	for (String id : devices.keySet()) {
	    HashMap<String, String> type = (HashMap<String, String>) devices.get(id).get("data").get("deviceTypeString");
	    if (type.get("value").contains("Sensor")) {
		HashMap<String, String> vendor = (HashMap<String, String>) devices.get(id).get("data").get("vendorString");
		devicesMap.put(id, vendor.get("value"));
	    }
	}
	
	return devicesMap;

    }

    public Set<String> getIntances(String deviceCode) {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = host + ZWAVE_API + ZWAVE_API_RUN + "/" + ZWAVE_API_RUN_DEVICES + "[" + deviceCode + "]";

	ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

	HashMap<String, HashMap<String, Object>> body = response.getBody();

	Set<String> instances = body.get("instances").keySet();

	return instances;

    }

    public Set<String> getCommandClasses(String deviceCode, String instanceCode) {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = host + ZWAVE_API + ZWAVE_API_RUN + "/" + ZWAVE_API_RUN_DEVICES + "[" + deviceCode + "]" + "." + ZWAVE_API_RUN_INSTANCES + "[" + instanceCode + "]";
	ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

	HashMap<String, HashMap<String, HashMap<String, Object>>> body = response.getBody();

	Set<String> commandClassesKeys = new HashSet<String>(0);
	if (body == null) {
	    return commandClassesKeys;
	}

	HashMap<String, HashMap<String, Object>> commandClasses = body.get("commandClasses");

	for (String id : commandClasses.keySet()) {
	    HashMap<String, Object> commandClass = commandClasses.get(id);
	    String name = (String) commandClass.get("name");
	    if (name.contains("Sensor")) {
		commandClassesKeys.add(id);
	    }
	}

	return commandClassesKeys;

    }

    public Set<String> getDataChannels(String deviceCode, String instanceCode, String channelCode) {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = host + ZWAVE_API + ZWAVE_API_RUN + "/" + ZWAVE_API_RUN_DEVICES + "[" + deviceCode + "]" + "." + ZWAVE_API_RUN_INSTANCES + "[" + instanceCode + "]" + "."
		+ ZWAVE_API_RUN_COMMANDCLASSES + "[" + channelCode + "]";

	ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

	HashMap<String, HashMap<String, Object>> body = response.getBody();

	Set<String> dataChannels = new HashSet<String>(0);

	if (body == null) {
	    return dataChannels;
	}

	Set<Entry<String, Object>> list = body.get("data").entrySet();
	for (Entry<String, Object> entry : list) {
	    if (NumberUtils.isNumber(entry.getKey())) {
		dataChannels.add(entry.getKey());
	    }
	}

	return dataChannels;

    }

    public ZWaveSensorData getDevicesData(ZWaveSensorCode sensorCode) {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = host + ZWAVE_API + ZWAVE_API_RUN + "/" + ZWAVE_API_RUN_DEVICES + "[" + sensorCode.getDeviceCode() + "]" + "." + ZWAVE_API_RUN_INSTANCES + "[" + sensorCode.getInstanceCode() + "]"
		+ "." + ZWAVE_API_RUN_COMMANDCLASSES + "[" + sensorCode.getCommandClasses() + "]" + "." + ZWAVE_API_RUN_DATA + "[" + sensorCode.getData() + "]";

	ResponseEntity<HashMap> response = restTemplate.exchange(url, HttpMethod.GET, request, HashMap.class);

	HashMap body = response.getBody();
	ZWaveSensorData sensor = new ZWaveSensorData();

	// Common parts
	sensor.setUpdateTime((Number) body.get("updateTime"));
	sensor.setType(((Map<String, String>) body.get("sensorTypeString")).get("value"));

	// Type of sensor
	if (body.get("level") != null) {
	    sensor.setUnit("Boolean");
	    Boolean value = ((Map<String, Boolean>) body.get("level")).get("value");
	    if(value){
		sensor.setValue(1);
	    }else{
		sensor.setValue(0);
	    }
	    
	} else if (body.get("val") != null) {
	    sensor.setUnit(((Map<String, String>) body.get("scaleString")).get("value"));
	    sensor.setValue(((Map<String, Number>) body.get("val")).get("value"));
	}

	return sensor;

    }

    public ResponseEntity<ZWaveSensorRegister> getAllData() {

	RestTemplate restTemplate = this.getRestTemplate();
	HttpEntity<String> request = new HttpEntity<String>(this.getHeaders());

	String url = host + ZWAVE_API + ZWAVE_API_DATA + "/0";

	ResponseEntity<ZWaveSensorRegister> response = restTemplate.exchange(url, HttpMethod.GET, request, ZWaveSensorRegister.class);

	return response;

    }

    public Set<ZWaveSensorCode> discoverSensors(String deviceCode) {

	HashSet<ZWaveSensorCode> sensors = new HashSet<ZWaveSensorCode>();

	Set<String> instances = this.getIntances(deviceCode);

	for (String instanceCode : instances) {

	    Set<String> channels = this.getCommandClasses(deviceCode, instanceCode);
	    for (String channelsCode : channels) {

		Set<String> dataChannels = this.getDataChannels(deviceCode, instanceCode, channelsCode);
		for (String dataChannel : dataChannels) {
		    
		    ZWaveSensorCode sensor = new ZWaveSensorCode(deviceCode, instanceCode, channelsCode, dataChannel);
		    sensors.add(sensor);
		}

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
