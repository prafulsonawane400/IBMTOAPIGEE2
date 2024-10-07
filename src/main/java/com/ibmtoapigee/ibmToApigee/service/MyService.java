package com.ibmtoapigee.ibmToApigee.service;

import com.google.auth.oauth2.AccessToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyService {

    private final AccessToken accessToken;

    @Autowired
    public MyService(AccessToken accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessTokenValue() {
        return accessToken.getTokenValue();
    }

    // Add your methods to interact with Apigee Management API
}
