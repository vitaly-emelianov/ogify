package net.ogify.backend;

import net.ogify.backend.elements.rest.SNRequestUri;
import net.ogify.backend.elements.rest.SocialNetworkParam;
import net.ogify.backend.helper.Callback;
import net.ogify.backend.helper.NetworkHandler;

import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

public class AuthResource {
    private String base = "http://ogify-miptsail.rhcloud.com//rest/auth";

    public void getRequestUri(SocialNetworkParam socialNetwork, Callback<SNRequestUri> callback) {
        NetworkHandler nh = NetworkHandler.getInstance();
        nh.read(base + "/getRequestUri?sn=" + socialNetwork.getValue(), SNRequestUri.class, callback);
    }

    public void auth(String code, SocialNetworkParam socialNetwork) throws MalformedURLException, URISyntaxException {
        NetworkHandler nh = NetworkHandler.getInstance();
        nh.read(base + "/getRequestUri?code=" + code + "&state=" + socialNetwork.getValue(), Response.class, new Callback<Response>() {
            @Override
            public void callback(Response response) {
                System.out.println(response);
            }
        });
    }
}
