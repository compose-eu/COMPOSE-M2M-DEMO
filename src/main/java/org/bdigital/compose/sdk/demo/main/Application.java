package org.bdigital.compose.sdk.demo.main;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.bdigital.compose.sdk.client.IDMAPI;
import org.bdigital.compose.sdk.client.IDMAPIClient;
import org.bdigital.compose.sdk.client.SDKAPIClient;
import org.bdigital.compose.sdk.client.ServioticyAPI;
import org.bdigital.compose.sdk.client.ServioticyAPIClient;
import org.bdigital.compose.sdk.config.ComposeAPICredentials;
import org.bdigital.compose.sdk.demo.client.ZWaveClient;
import org.bdigital.compose.sdk.demo.model.compose.ZWaveAeonLabsCSO;
import org.bdigital.compose.sdk.demo.model.compose.ZWaveAeonLabsHumidityCSO;
import org.bdigital.compose.sdk.demo.model.compose.ZWaveAeonLabsLuminiscenceCSO;
import org.bdigital.compose.sdk.demo.model.compose.ZWaveAeonLabsTemperatureCSO;
import org.bdigital.compose.sdk.demo.model.compose.ZWaveEverspringCSO;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorCode;
import org.bdigital.compose.sdk.demo.model.zwave.ZWaveSensorData;
import org.bdigital.compose.sdk.exception.HttpErrorException;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannel;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannels;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeStreamDataUpdate;
import org.bdigital.compose.sdk.model.serviceobject.response.ComposeSORegisteredResponse;
import org.bdigital.compose.sdk.model.user.ComposeUserAccess;
import org.bdigital.compose.sdk.model.user.ComposeUserAccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

@Configuration
@EnableScheduling
@SpringBootApplication
public class Application {

    private static Log logger = LogFactory.getLog(Application.class);

    @Autowired
    private ComposeAPICredentials apiCredentials;

    @Autowired
    private ZWaveClient myZWaveClient;

    @Autowired
    private ServioticyAPI servioticyAPIClient;

    @Autowired
    private SDKAPIClient sdkApiClient;

    private ComposeUserAccessToken token;
    private HashMap<String, HashMap<String, ComposeSORegisteredResponse>> registeredSO = new HashMap<String, HashMap<String, ComposeSORegisteredResponse>>();

    public static void main(String[] args) {
	ApplicationContext ctx = SpringApplication.run(Application.class, args);

	System.out.println("=================================================");
	System.out.println("--> WELCOME TO COMPOSE SDK DEMO                  ");
	System.out.println("=================================================");
	System.out.println("--> Listening Z-Wave raZberry Gateway!");
	System.out.println("--> Looking for Devices ...");

    }

    @Bean
    public ComposeAPICredentials apiCredentials() {
	return new ComposeAPICredentials();
    }

    @Bean
    public ZWaveClient myZWaveClient(@Value("${gateway.razberry.host}") String host) {
	return new ZWaveClient(host);
    }

    @Bean
    public ServioticyAPI servioticyAPIClient(@Value("${compose.idm.user}") String idmUser, @Value("${compose.idm.password}") String idmPassword) throws HttpErrorException {

	System.out.println("=================================================");
	System.out.println("--> SETTING UP: Compose ServIoTicy API           ");

	System.out.println("--> Connecting with IDM ...");

	this.requestToken(idmUser, idmPassword);

	ServioticyAPI servioticyAPI = new ServioticyAPIClient(apiCredentials);

	return servioticyAPI;
    }

    @Bean
    public SDKAPIClient sdkapiClient(@Value("${compose.idm.user}") String idmUser, @Value("${compose.idm.password}") String idmPassword) throws HttpErrorException {

	System.out.println("=================================================");
	System.out.println("--> SETTING UP: Compose SDK API                  ");

	System.out.println("--> Connecting with IDM ...");

	// Setting Up Compose Client
	ComposeAPICredentials apiCredentials = new ComposeAPICredentials();
	IDMAPI idmapi = new IDMAPIClient(apiCredentials);
	ComposeUserAccess user = new ComposeUserAccess(idmUser, idmPassword);
	token = idmapi.userAuthoritzation(user);

	System.out.println("--> Credentials: [User: " + idmUser + ", Token: " + token + "]");
	System.out.println("--> Conecting with Servioticy ...");

	SDKAPIClient client = new SDKAPIClient(apiCredentials);

	try {

	    Set<String> so = client.listServiceObjectSDK(token);
	    System.out.println("--> Removing Existing Service Objects: " + so);

	    for (String idSo : so) {
		client.deleteServiceObjectSDK(token, idSo);
	    }

	} catch (ResourceAccessException | HttpServerErrorException e) {
	    System.out.println(" --> [ERROR] - Unreachable connection: " + e.getCause());
	}

	System.out.println("=================================================");

	return client;
    }

    @Scheduled(cron = "0/30 * * * * ?")
    private void scann() throws HttpServerErrorException, HttpErrorException {

	try {

	    // Detect Device
	    Map<String, String> detectDevices = myZWaveClient.getDevices();

	    // Check if exist

	    for (String id : detectDevices.keySet()) {

		String vendor = detectDevices.get(id);

		System.out.println("--> Sensor Detected from vendor:" + vendor + ", with id: " + id);
		System.out.println("--> ");

		// is New
		if (!registeredSO.containsKey(id)) {

		    registeredSO.put(id, new HashMap<String, ComposeSORegisteredResponse>(0));

		    if (vendor.equals(ZWaveAeonLabsCSO.VENDOR_NAME)) {

			Set<ZWaveSensorCode> sensors = myZWaveClient.discoverSensors(id);
			for (ZWaveSensorCode zWaveSensorCode : sensors) {

			    ZWaveSensorData data = myZWaveClient.getDevicesData(zWaveSensorCode);

			    if (data.getType().equals(ZWaveAeonLabsHumidityCSO.TYPE_NAME)) {
				ZWaveAeonLabsHumidityCSO sensorSO = new ZWaveAeonLabsHumidityCSO("http://raspberry.local");
				System.out.println("--> Registering Service Object: " + sensorSO);
				ComposeSORegisteredResponse registeredDevices = sdkApiClient.createServiceObjectSDK(token, sensorSO);
				System.out.println("--> SeviceObject Registered: " + registeredDevices);
				registeredSO.get(id).put(ZWaveAeonLabsHumidityCSO.TYPE_NAME, registeredDevices);
			    }

			    if (data.getType().equals(ZWaveAeonLabsLuminiscenceCSO.TYPE_NAME)) {
				ZWaveAeonLabsLuminiscenceCSO sensorSO = new ZWaveAeonLabsLuminiscenceCSO("http://raspberry.local");
				System.out.println("--> Registering Service Object: " + sensorSO);
				ComposeSORegisteredResponse registeredDevices = sdkApiClient.createServiceObjectSDK(token, sensorSO);
				System.out.println("--> SeviceObject Registered: " + registeredDevices);
				registeredSO.get(id).put(ZWaveAeonLabsLuminiscenceCSO.TYPE_NAME, registeredDevices);
			    }

			    if (data.getType().equals(ZWaveAeonLabsTemperatureCSO.TYPE_NAME)) {
				ZWaveAeonLabsTemperatureCSO sensorSO = new ZWaveAeonLabsTemperatureCSO("http://raspberry.local");
				System.out.println("--> Registering Service Object: " + sensorSO);
				ComposeSORegisteredResponse registeredDevices = sdkApiClient.createServiceObjectSDK(token, sensorSO);
				System.out.println("--> SeviceObject Registered: " + registeredDevices);
				registeredSO.get(id).put(ZWaveAeonLabsTemperatureCSO.TYPE_NAME, registeredDevices);
			    }
			}
		    }
		}
	    }

	    for (String id : registeredSO.keySet()) {

		Set<ZWaveSensorCode> sensors = myZWaveClient.discoverSensors(id);
		for (ZWaveSensorCode zWaveSensorCode : sensors) {

		    HashMap<String, ComposeSORegisteredResponse> registeredSOs = registeredSO.get(id);
		    ZWaveSensorData sensorData = myZWaveClient.getDevicesData(zWaveSensorCode);

			//System.out.println("--> Upload Data for ServiceObject: " + so.getId());

			ComposeSORegisteredResponse so;
			if (sensorData.getType().equals(ZWaveAeonLabsTemperatureCSO.TYPE_NAME)) {
			    so = registeredSOs.get(ZWaveAeonLabsTemperatureCSO.TYPE_NAME);
			    ComposeAbstractSOChannels channelsData = new ComposeAbstractSOChannels();
			    channelsData.put("temperature", new ComposeAbstractSOChannel("sensor", sensorData.getValue(), "celcius"));
			    ComposeStreamDataUpdate data = new ComposeStreamDataUpdate();
			    data.setChannels(channelsData);
			    data.setLastUpdate(sensorData.getUpdateTime());

			    System.out.println("--> Sending Data: " + data);
			    String rData = servioticyAPIClient.uploadDataOnServiceObjectStreams(so.getApi_token(), so.getId(), "temperature", data);
			    System.out.println("--> Response Data: " + rData);
			}

			if (sensorData.getType().equals(ZWaveAeonLabsHumidityCSO.TYPE_NAME)) {
			    so = registeredSOs.get(ZWaveAeonLabsHumidityCSO.TYPE_NAME);
			    ComposeAbstractSOChannels channelsData = new ComposeAbstractSOChannels();
			    channelsData.put("humidity", new ComposeAbstractSOChannel("sensor", sensorData.getValue(), "integer"));
			    ComposeStreamDataUpdate data = new ComposeStreamDataUpdate();
			    data.setChannels(channelsData);
			    data.setLastUpdate(sensorData.getUpdateTime());

			    System.out.println("--> Sending Data: " + data);
			    String rData = servioticyAPIClient.uploadDataOnServiceObjectStreams(so.getApi_token(), so.getId(), "humidity", data);
			    System.out.println("--> Response Data: " + rData);
			}

			if (sensorData.getType().equals(ZWaveAeonLabsLuminiscenceCSO.TYPE_NAME)) {
			    so = registeredSOs.get(ZWaveAeonLabsLuminiscenceCSO.TYPE_NAME);
			    ComposeAbstractSOChannels channelsData = new ComposeAbstractSOChannels();
			    channelsData.put("luminiscence", new ComposeAbstractSOChannel("sensor", sensorData.getValue(), "lux"));
			    ComposeStreamDataUpdate data = new ComposeStreamDataUpdate();
			    data.setChannels(channelsData);
			    data.setLastUpdate(sensorData.getUpdateTime());

			    System.out.println("--> Sending Data: " + data);
			    String rData = servioticyAPIClient.uploadDataOnServiceObjectStreams(so.getApi_token(), so.getId(), "luminiscence", data);
			    System.out.println("--> Response Data: " + rData);
			}

		    System.out.println("--> ");

		}

	    }

	} catch (ResourceAccessException e) {
	    System.out.println("--> [WARN] - Unreachable connection: " + e.getCause());
	}
    }

    private void requestToken(String idmUser, String idmPassword) throws HttpErrorException {

	// Setting Up Compose Client
	ComposeAPICredentials apiCredentials = new ComposeAPICredentials();
	IDMAPI idmapi = new IDMAPIClient(apiCredentials);
	ComposeUserAccess user = new ComposeUserAccess(idmUser, idmPassword);
	token = idmapi.userAuthoritzation(user);

    }

}
