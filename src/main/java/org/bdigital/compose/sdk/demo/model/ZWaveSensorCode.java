package org.bdigital.compose.sdk.demo.model;

public class ZWaveSensorCode {


    public String deviceCode;
    public String instanceCode;
    public String commandClasses;
    public String data;
    
    public ZWaveSensorCode(String deviceCode, String instanceCode, String commandClasses, String data) {
	super();
	this.deviceCode = deviceCode;
	this.instanceCode = instanceCode;
	this.commandClasses = commandClasses;
	this.data = data;
    }
    
    public String getDeviceCode() {
        return deviceCode;
    }
    public void setDeviceCode(String deviceCode) {
        this.deviceCode = deviceCode;
    }
    public String getInstanceCode() {
        return instanceCode;
    }
    public void setInstanceCode(String instanceCode) {
        this.instanceCode = instanceCode;
    }
    public String getCommandClasses() {
        return commandClasses;
    }
    public void setCommandClasses(String commandClasses) {
        this.commandClasses = commandClasses;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
	return "ZWaveSensorCode [deviceCode=" + deviceCode + ", instanceCode=" + instanceCode + ", commandClasses=" + commandClasses + ", data=" + data + "]";
    }
    
}
