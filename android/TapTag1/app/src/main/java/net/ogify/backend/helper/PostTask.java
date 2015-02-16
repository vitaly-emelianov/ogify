package net.ogify.backend.helper;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.WebResource;

import net.ogify.helper.MyApplication;
import net.ogify.helper.Storage;

import javax.ws.rs.core.NewCookie;

public class PostTask extends AsyncTask<String, String, String> {

    private final String url;
    private final String requestBody;
    private final Callback<String> callback;

    PostTask(String url, String requestBody, Callback<String> callback) {
        this.url = url;
        this.requestBody = requestBody;
        this.callback = callback;
    }

    @Override
    protected String doInBackground(String... params) {
        String cookies = Storage.getPreference("cookies");

        final Client client = Client.create();
        final WebResource resource = client.resource(url);
        if (cookies != null) {
            String[] cookieList = cookies.split("; ");
            for (String cookie : cookieList) {
                String name = cookie.substring(0, cookie.indexOf('='));
                String value = cookie.substring(cookie.indexOf('='));
                Log.d("postTask", name + "=" + value);
                resource.cookie(new NewCookie(name, value));
            }
        }

        final ClientResponse response = resource.type(MIMETypes.APPLICATION_JSON.getName())
                .post(ClientResponse.class, requestBody);
        if (response.getStatus() != 201 && response.getStatus() != 200) {
            throw new RuntimeException("failed: http error code = " + response.getStatus());
        }
        final String responseEntity = response.getEntity(String.class).replaceAll("\\\\", "");
        return responseEntity.substring(1, responseEntity.length() - 1);
    }

    @Override
    protected void onPostExecute(String result) {
        callback.callback(result);
        super.onPostExecute(result);
    }
}
