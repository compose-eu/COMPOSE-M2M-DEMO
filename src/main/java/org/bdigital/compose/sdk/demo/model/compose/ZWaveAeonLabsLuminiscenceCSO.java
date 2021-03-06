package org.bdigital.compose.sdk.demo.model.compose;

import java.util.HashMap;

import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannel;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannels;

public class ZWaveAeonLabsLuminiscenceCSO extends ZWaveAeonLabsCSO {

    public static String TYPE_ORIGIN = "Luminiscence";
    public static String TYPE_COMPOSE = "luminiscence";
    
    public ZWaveAeonLabsLuminiscenceCSO(String url) {
	super(url);
	super.setName(super.VENDOR_NAME + " " + TYPE_ORIGIN);
	super.setDescription(super.VENDOR_NAME + " Multi-Sensor: " + TYPE_ORIGIN);
	
	ComposeAbstractSOChannels channel = new ComposeAbstractSOChannels();
	channel.put(TYPE_COMPOSE, new ComposeAbstractSOChannel("number", null, "integer"));
	super.addStream(TYPE_COMPOSE, "sensor", super.VENDOR_NAME+ " " + TYPE_ORIGIN, channel);
	
	super.setiServeData(TYPE_COMPOSE, "http://iserve.kmi.open.ac.uk/ns/mso#IlluminanceSensor", "http://iserve.kmi.open.ac.uk/ns/mso#IlluminanceMeasurement");
	
    }
    
}
