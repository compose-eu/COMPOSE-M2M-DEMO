package org.bdigital.compose.sdk.demo.model.compose;

import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannel;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannels;

public class ZWaveAeonLabsHumidityCSO extends ZWaveAeonLabsCSO {

    public static String TYPE_NAME = "Humidity";
    
    public ZWaveAeonLabsHumidityCSO(String url) {
	super(url);
	super.setName(super.VENDOR_NAME + " " + TYPE_NAME);
	super.setDescription(super.VENDOR_NAME + " Multi-Sensor: " + TYPE_NAME);
	
	ComposeAbstractSOChannels channel = new ComposeAbstractSOChannels();
	channel.put("humidity", new ComposeAbstractSOChannel("number", null, "integer"));
	super.addStream("humidity", "sensor", "Everspring Multi-Sensor", channel);
	
	super.setModel(TYPE_NAME);
    }

    
}
