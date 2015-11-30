package org.bdigital.compose.sdk.demo.model.zwave;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ZWaveSensorData {

    public String type;
    
    public Number value;
    
    public String unit;
    
    public Number updateTime;

    
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Number getValue() {
        return value;
    }

    public void setValue(Number value) {
        this.value = value;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Number getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Number updateTime) {
        this.updateTime = updateTime;
    }

    @Override
    public String toString() {
	String json = "";
	ObjectMapper mapper = new ObjectMapper();
	try {
	    json = mapper.writeValueAsString(this);
	} catch (JsonProcessingException e) {
	    json = super.toString();
	}

	return json;
    }
    
}
