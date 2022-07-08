package com.example;


import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.json.JSONObject;

/**
 * Make the auth calls
 *
 */
public class KenshoAuth 
{
    private final String OKTA_TOKEN_ENDPOINT = "https://kensho.okta.com/oauth2/default/v1/token";

    private String clientID, privateKeyFilePath;

    KenshoAuth(String clientID, String privateKeyFilePath) {
        this.clientID = clientID;
        this.privateKeyFilePath = privateKeyFilePath;
    }
    
    /**
     * Creates a private key from pem file given
     */
    private PrivateKey getPrivateKey() {
        try {
        	Security.addProvider(new BouncyCastleProvider());
            File file = new File(this.privateKeyFilePath);
            FileReader keyReader = new FileReader(file);
            PEMParser pemParser = new PEMParser(keyReader);
            JcaPEMKeyConverter converter = new JcaPEMKeyConverter().setProvider("BC");
            KeyPair kp = converter.getKeyPair((PEMKeyPair) pemParser.readObject());
            PrivateKey privateKey = kp.getPrivate();
            pemParser.close();
            return privateKey;
        } catch (FileNotFoundException e) {
            System.out.println("File not found");
        } catch (IOException e) {
            System.out.println("Error reading file");
        }
		throw new RuntimeException("Error making private key");
    }
    
    /**
     * Create JWT using the created private key and proper claims.
     */
    public String makeJWT() {
        String jwt = "";
        PrivateKey privKey = getPrivateKey();
        long iat = System.currentTimeMillis();
        jwt = Jwts.builder()
            .setIssuer(this.clientID)
            .setSubject(this.clientID)
            .setAudience(this.OKTA_TOKEN_ENDPOINT)
            .setExpiration(new Date(iat + 1000 * 60 * 5))
            .setIssuedAt(new Date(iat))
            .signWith(privKey, SignatureAlgorithm.RS256)
            .compact();
        return jwt;
    }

    /**
     * Obtain an access token from Okta for given scopes.
     */
    public String getAccessToken(String scopes) {
        String clientAssertion = makeJWT();
        List<NameValuePair> urlParameters = new ArrayList<>();
        urlParameters.add(new BasicNameValuePair("client_assertion", clientAssertion));
        urlParameters.add(new BasicNameValuePair("client_assertion_type", "urn:ietf:params:oauth:client-assertion-type:jwt-bearer"));
        urlParameters.add(new BasicNameValuePair("scope", scopes));
        urlParameters.add(new BasicNameValuePair("grant_type", "client_credentials"));

        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost request = new HttpPost(this.OKTA_TOKEN_ENDPOINT);
        try {
			request.setEntity(new UrlEncodedFormEntity(urlParameters));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}

		try {
			HttpResponse response = httpClient.execute(request);
			if (response.getStatusLine().getStatusCode() != 200) {
				throw new RuntimeException("Okta error: " + EntityUtils.toString(response.getEntity()));
			}
			JSONObject accessToken = new JSONObject(EntityUtils.toString(response.getEntity()));
			return (String) accessToken.get("access_token");
		} catch (IOException e) {
			e.printStackTrace();
		}
		throw new RuntimeException("Error sending request to Okta");
        
    }
}

