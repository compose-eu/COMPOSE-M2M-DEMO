package org.bdigital.compose.sdk.demo.client;

import org.bdigital.compose.sdk.client.IDMAPI;
import org.bdigital.compose.sdk.client.IDMAPIClient;
import org.bdigital.compose.sdk.client.SDKAPI;
import org.bdigital.compose.sdk.client.SDKAPIClient;
import org.bdigital.compose.sdk.config.ComposeAPICredentials;
import org.bdigital.compose.sdk.demo.model.ZWaveEverspringCSO;
import org.bdigital.compose.sdk.exception.HttpErrorException;
import org.bdigital.compose.sdk.model.serviceobject.response.ComposeSORegisteredResponse;
import org.bdigital.compose.sdk.model.user.ComposeUserAccess;
import org.bdigital.compose.sdk.model.user.ComposeUserAccessToken;

public class ComposeClient {

    private SDKAPI sdkapi;
    private IDMAPI idmapi;
    
    public ComposeClient(){
	
	ComposeAPICredentials apiCredentials = new ComposeAPICredentials();
	idmapi = new IDMAPIClient(apiCredentials);
	
	sdkapi = new SDKAPIClient(apiCredentials);
    }
    
    public ComposeSORegisteredResponse registerDigi(ZWaveEverspringCSO digiComposeSO) throws HttpErrorException{
	
	ComposeUserAccess user = new ComposeUserAccess("test_compose_bdigital", "c6jvUBDV");
	ComposeUserAccessToken token = idmapi.userAuthoritzation(user);
	
	ComposeSORegisteredResponse createdObject = sdkapi.createServiceObjectSDK(token, digiComposeSO);
	
	return createdObject;
	
    }
    
}
