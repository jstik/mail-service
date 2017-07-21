package com.security;

import core.SpringAware;
import org.apache.commons.net.util.Base64;
import org.junit.Test;

/**
 * Created by Julia on 14.07.2017.
 */
public class JWTTokenTest extends SpringAware {

    @Test
    public void testDecodeJWT(){
        String jwtToken = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzY29wZSI6WyJyZWFkIiwid3JpdGUiXSwiZXhwIjoxNTAwMDcxODk0LCJhdXRob3JpdGllcyI6WyJBZG1pbiJdLCJqdGkiOiJlOTkxNDIyZS1jNjNhLTRjNjItODg5NC1lODcyMmQwYzljM2QiLCJjbGllbnRfaWQiOiJtYWlsX3NlcnZpY2UifQ.gCwuE2KDZNc3GYGVmRKY2X2W4kG7vSURaYkmP3fFKs0";
        System.out.println("------------ Decode JWT ------------");
        String[] split_string = jwtToken.split("\\.");
        String base64EncodedHeader = split_string[0];
        String base64EncodedBody = split_string[1];
        String base64EncodedSignature = split_string[2];

        System.out.println("~~~~~~~~~ JWT Header ~~~~~~~");
        Base64 base64Url = new Base64(true);
        String header = new String(base64Url.decode(base64EncodedHeader));
        System.out.println("JWT Header : " + header);


        System.out.println("~~~~~~~~~ JWT Body ~~~~~~~");
        String body = new String(base64Url.decode(base64EncodedBody));
        System.out.println("JWT Body : "+body);
    }


}
