package org.bdigital.compose.sdk.demo.model.compose;

import java.util.ArrayList;
import java.util.HashMap;

import org.bdigital.compose.sdk.model.serviceobject.ComposeAbstractServiceObject;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannel;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannels;

public class ZWaveAeonLabsCSO extends ComposeAbstractServiceObject<HashMap<String,HashMap>, ArrayList, ArrayList> {

    public static String VENDOR_NAME = "Aeon Labs";
    
    public ZWaveAeonLabsCSO(String url) {
	super();
	super.setPublic_("true");
	super.setUrl(url);
    }

    public void setiServeData(String sensorName, String sensorURL, String outURL){
	
	// Custom fields for iServe
	HashMap<String, String> temperature = new HashMap<String, String>();
	temperature.put("sensor", sensorURL);
	temperature.put("in", "http://iserve.kmi.open.ac.uk/ns/mso#TimeMeasurement");
	temperature.put("out", outURL);
	
	HashMap<String, HashMap> channelInfo = new HashMap<String, HashMap>();
	channelInfo.put(sensorName, temperature);
	
	HashMap<String, HashMap> channels = new HashMap<String, HashMap>();
	channels.put("channels", channelInfo);
	
	HashMap<String, HashMap> sensor = new HashMap<String, HashMap>();
	sensor.put(sensorName, channels);
	
	HashMap<String, HashMap> streams = new HashMap<String, HashMap>();
	streams.put("streams", sensor);
	
	HashMap<String, HashMap> iserveAnnotation = new HashMap<String, HashMap>();
	iserveAnnotation.put("iserveAnnotation", streams);
	
	super.setCustomFields(iserveAnnotation);
    }
    
//	 "customFields": {
//	   "iserveAnnotation": {
//	     "streams": {
//	       "wheather": {
//	        "channels": {
//	          "temperature": {
//	            "sensor": "http://iserve.kmi.open.ac.uk/ns/mso#ThermodynamicTemperatureSensor",
//	            "in": "http://iserve.kmi.open.ac.uk/ns/mso#TimeMeasurement",
//	            "out": "http://iserve.kmi.open.ac.uk/ns/mso#DegreeCelsiusMeasurement"
//	          },
//	          "pressure": {
//	            "sensor": "http://iserve.kmi.open.ac.uk/ns/mso#AtmosphericPressureSensor",
//	            "in": "http://iserve.kmi.open.ac.uk/ns/mso#TimeMeasurement",
//	            "out": "http://iserve.kmi.open.ac.uk/ns/mso#PascalMeasurementMeasurement"
//	          }
//	        }
//	       }
//	     }
//	   }
//	 },
//	 "actions": []
//	}
    
}
