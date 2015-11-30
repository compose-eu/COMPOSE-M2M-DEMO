package org.bdigital.compose.sdk.demo.model.compose;

import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannel;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannels;

public class ZWaveAeonLabsTemperatureCSO extends ZWaveAeonLabsCSO {

    public static String TYPE_NAME = "Temperature";
    
    public ZWaveAeonLabsTemperatureCSO(String url) {
	super(url);
	super.setName(super.VENDOR_NAME + " " + TYPE_NAME);
	super.setDescription(super.VENDOR_NAME + " Multi-Sensor: " + TYPE_NAME);
	
	ComposeAbstractSOChannels channel = new ComposeAbstractSOChannels();
	channel.put("temperature", new ComposeAbstractSOChannel("number", null, "integer"));
	super.addStream("temperature", "sensor", super.VENDOR_NAME, channel);
	
	super.setModel(TYPE_NAME);
    }
    
    
}
