package net.ogify;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;

import net.ogify.backend.AuthResource;
import net.ogify.backend.elements.rest.SNRequestUri;
import net.ogify.backend.elements.rest.SocialNetworkParam;
import net.ogify.backend.entities.SocialNetwork;
import net.ogify.backend.helper.Callback;
import net.ogify.custom.CustomActivity;
import net.ogify.helper.Storage;
import net.ogify.helper.Utils;

import javax.ws.rs.core.NewCookie;


public class LoginActivity extends CustomActivity {
    private static final String TAG = "Auth";
    public static String redirect_url = "http://ogify-miptsail.rhcloud.com";
    public SocialNetworkParam socialNetworkParam = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button fb = (Button) findViewById(R.id.login_fb_button);
        fb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socialNetworkParam = new SocialNetworkParam("facebook");
                loginProcess();
            }
        });

        Button vk = (Button) findViewById(R.id.login_vk_button);
        vk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                socialNetworkParam = new SocialNetworkParam("vk");
                loginProcess();
            }
        });
    }

    public void loginProcess() {
        AuthResource ar = new AuthResource();
        ar.getRequestUri(socialNetworkParam, new Callback<SNRequestUri>() {
            @Override
            public void callback(SNRequestUri snRequestUri) {
                WebView webview = (WebView) findViewById(R.id.vkontakteview);
                webview.getSettings().setJavaScriptEnabled(true);
                webview.clearCache(true);
                webview.setWebViewClient(new CustomWebViewClient());

                //otherwise CookieManager will fall with java.lang.IllegalStateException: CookieSyncManager::createInstance() needs to be called before CookieSyncManager::getInstance()
                CookieSyncManager.createInstance(LoginActivity.this);

                CookieManager cookieManager = CookieManager.getInstance();
                cookieManager.removeAllCookie();

                webview.loadUrl(snRequestUri.getRequestUri());
            }
        });
    }

    class CustomWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            String cookies = CookieManager.getInstance().getCookie(url);
            Log.d(TAG, "All the cookies in a start:" + cookies);
        }

        @Override
        public void onPageFinished(WebView view, String url){
            String cookies = CookieManager.getInstance().getCookie(url);
            parseUrl(url);
            Log.d(TAG, "All the cookies in a string:" + cookies);
        }
    }

    private void parseUrl(String url) {
        try {
            if (url == null)
                return;
            Log.i(TAG, "url=" + url);
            if (url.startsWith(redirect_url)) {
                String cookies = CookieManager.getInstance().getCookie(url);
                Storage.setPreference("cookies", cookies);
                if (cookies != null) {
                    String[] cookieList = cookies.split("; ");
                    for (String cookie : cookieList) {
                        String name = cookie.substring(0, cookie.indexOf('='));
                        String value = cookie.substring(cookie.indexOf('=') + 1);
                        Log.d("loginCookie", name + "=" + value);
                        Storage.setPreference(name, value);
                    }
                }
                Log.d("Login!", "Yes!!!" + Storage.getPreference("cookies"));


                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                i.putExtra("flag", "modify");
                startActivity(i);
                finish();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
