package net.ogify.backend.helper;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientHandlerException;
import com.sun.jersey.api.client.ClientRequest;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;
import com.sun.jersey.api.client.filter.ClientFilter;

import net.ogify.helper.MyApplication;
import net.ogify.helper.Storage;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.NewCookie;

public class GetTask extends AsyncTask<String, String, String> {

    private final String url;
    private final Callback<String> callback;

    GetTask(String url, Callback<String> callback) {
        this.url = url;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        final Client client = Client.create();

        final WebResource resource = client.resource(url);

        WebResource.Builder builder = resource.getRequestBuilder();
        if (Storage.getPreference("ogifySessionSecret") != null) {
            Log.d("sfc", Storage.getPreference("ogifySessionSecret"));
            builder = builder.cookie(new Cookie("ogifySessionSecret", Storage.getPreference("ogifySessionSecret")));
        }
        if (Storage.getPreference("sId") != null) {
            Log.d("sfc", Storage.getPreference("sId"));
            builder = builder.cookie(new Cookie("sId", Storage.getPreference("sId")));
        }
        WebResource.Builder bibi = resource.accept(MIMETypes.APPLICATION_JSON.getName());
        ClientResponse response = bibi.get(ClientResponse.class);
        return response.getEntity(String.class);
    }

    @Override
    protected void onPostExecute(String result) {
        callback.callback(result);
        super.onPostExecute(result);
    }
}