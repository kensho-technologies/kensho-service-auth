package com.example;

import java.util.Arrays;

public class App 
{
    public static void main( String[] args )
    {   
        String[] scopes = Arrays.copyOfRange(args, 2, args.length);
        KenshoAuth auth = new KenshoAuth(args[0], args[1]);
        String accessToken = auth.getAccessToken(String.join(" ", scopes));
        System.out.println(accessToken);
    }
}
