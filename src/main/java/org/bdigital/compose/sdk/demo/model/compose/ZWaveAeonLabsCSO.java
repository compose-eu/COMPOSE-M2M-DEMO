package org.bdigital.compose.sdk.demo.model.compose;

import java.util.ArrayList;
import java.util.HashMap;

import org.bdigital.compose.sdk.model.serviceobject.ComposeAbstractServiceObject;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannel;
import org.bdigital.compose.sdk.model.serviceobject.components.ComposeAbstractSOChannels;

public class ZWaveAeonLabsCSO extends ComposeAbstractServiceObject<HashMap<String,String>, ArrayList, ArrayList> {

    public static String VENDOR_NAME = "Aeon Labs";
    
    public ZWaveAeonLabsCSO(String url) {
	super();
	super.setPublic_("true");
	super.setUrl(url);
    }

    public void setModel(String model){
	HashMap<String, String> customFields = new HashMap<String, String>();
	customFields.put("vendor", VENDOR_NAME);
	customFields.put("model", model);
	super.setCustomFields(customFields);
    }
    
}
