package com.wizzardo.http.filter;

import com.wizzardo.http.ServerTest;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

/**
 * @author: wizzardo
 * Date: 29.11.14
 */
public class FilterTest extends ServerTest {

    @Test
    public void testBasicAuth() throws IOException {
        handler = (request, response) -> response.setBody("ok");
        String user = "user";
        String password = "password";
        server.getFiltersMapping().addBefore("/*", new BasicAuthFilter().allow(user, password));

        Assert.assertEquals(401, makeRequest("").get().getResponseCode());
        Assert.assertEquals("ok", makeRequest("").setBasicAuthentication(user, password).get().asString());
    }
}
