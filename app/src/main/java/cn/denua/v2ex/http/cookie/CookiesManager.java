package cn.denua.v2ex.http.cookie;

import android.content.Context;
import android.support.annotation.NonNull;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Cookies Manager, use for save OkHttp request cookies
 *
 * the cookies will save to Cookies_Prefs.xml
 * this class implemented interface CookieJar, CookieJar is lib OkHttps' custom handle cookies
 * interface, when request, OkHttpClient will return all cookies to the class which implements CookieJar.
 */
public class CookiesManager implements CookieJar {

    // Persistent cookies to sharedPreferences
    private  PersistentCookieStore cookieStore;

    /**
     * constructor, default file name
     *
     * @param context application context
     */
    public CookiesManager(Context context){

        cookieStore = new PersistentCookieStore(context, null);
    }

    @Override
    public synchronized void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {

        if (cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    /**
     * remove all cookies
     */
    public void removeAll(){

        cookieStore.removeAll();
    }

    @Override
    public synchronized List<Cookie> loadForRequest(@NonNull HttpUrl url) {

        return cookieStore.get(url);
    }

}