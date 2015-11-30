package org.bdigital.compose.sdk.demo.model.compose;

import java.util.ArrayList;
import java.util.HashMap;

import org.bdigital.compose.sdk.model.serviceobject.ComposeAbstractServiceObject;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannel;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannels;

public class ZWaveEverspringCSO extends ComposeAbstractServiceObject<HashMap<String,String>, ArrayList, ArrayList> {

    public ZWaveEverspringCSO(String url) {
	super();
	super.setName("Z-Wave Sensor");
	super.setDescription("Everspring Multi-Sensor: Temperature, Humidity");
	super.setPublic_("true");
	super.setUrl(url);
	
	ComposeAbstractSOChannels channel1 = new ComposeAbstractSOChannels();
	channel1.put("temperature", new ComposeAbstractSOChannel("number", null, "celcius"));
	super.addStream("temperature", "sensor", "Everspring Multi-Sensor", channel1);
	
	ComposeAbstractSOChannels channel2 = new ComposeAbstractSOChannels();
	channel2.put("humidity", new ComposeAbstractSOChannel("number", null, "integer"));
	super.addStream("humidity", "sensor", "Everspring Multi-Sensor", channel2);
    }

    public void setModel(String model){
	HashMap<String, String> customFields = new HashMap<String, String>();
	customFields.put("vendor", "Gigi");
	customFields.put("model", model);
	super.setCustomFields(customFields);
    }
    
    
}
