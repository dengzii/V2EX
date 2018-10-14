package cn.denua.v2ex.http.cookie;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Transient cookie manager
 * Holding cookies until object destroy
 */
class TransientCookieJar implements CookieJar {

    // cookie key value pairs
    private Map<String, ConcurrentHashMap<String, Cookie>> cookies = new HashMap<>();

    @Override
    public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {
        if (list.size() > 0) {
            for (Cookie item : list) {
                add(httpUrl, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {

        ArrayList<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(url.host()))
            ret.addAll(cookies.get(url.host()).values());
        return ret;
    }

    /**
     * add cookie into map
     *
     * @param url url
     * @param cookie cookie
     */
    private void add(HttpUrl url, Cookie cookie){

        String name = cookie.name() + "@" + cookie.domain();
        if (!cookie.persistent()) {
            if (!cookies.containsKey(url.host())) {
                cookies.put(url.host(), new ConcurrentHashMap<String, Cookie>());
            }
            cookies.get(url.host()).put(name, cookie);
        } else {
            if (cookies.containsKey(url.host())) {
                cookies.get(url.host()).remove(name);
            }
        }
    }

    /**
     * clean all
     */
    public void clear(){

        cookies.clear();
    }
}