package com.example;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.example.KenshoAuth;

/**
 * Testing maven java auth
 */
public class AppTest 
{
    @Test
    public void tokenCreated()
    {
        String clientID = System.getProperty("CLIENT_ID"); 
        String privateKeyFilePath = System.getProperty("PRIVATE_KEY_FILE");
        String scopes = System.getProperty("SCOPES");
        KenshoAuth auth = new KenshoAuth(clientID, privateKeyFilePath);
        String accessToken = auth.getAccessToken(String.join(" ", scopes));
        assertTrue( true );
    }
}
