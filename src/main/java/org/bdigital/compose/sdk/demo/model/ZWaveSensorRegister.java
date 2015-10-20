package org.bdigital.compose.sdk.demo.model;

import java.util.HashMap;

import org.springframework.util.MultiValueMap;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ZWaveSensorRegister {

    @JsonProperty("controller")
    public HashMap<String, Object> controller;
    
    @JsonProperty("devices")
    public HashMap<String, HashMap<String, HashMap<String, Object>>> devices;
    
    @JsonProperty("updateTime")
    public Number updateTime;

    
    
    public HashMap<String, Object> getController() {
        return controller;
    }

    public void setController(HashMap<String, Object> controller) {
        this.controller = controller;
    }

    public HashMap<String, HashMap<String, HashMap<String, Object>>> getDevices() {
        return devices;
    }

    public void setDevices(HashMap<String, HashMap<String, HashMap<String, Object>>> devices) {
        this.devices = devices;
    }

    public Number getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Number updateTime) {
        this.updateTime = updateTime;
    }

    

   
}
