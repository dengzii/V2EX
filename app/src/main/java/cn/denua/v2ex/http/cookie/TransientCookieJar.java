package cn.denua.v2ex.http.cookie;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
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
public class TransientCookieJar implements CookieJar {

    // cookie key value pairs
    private Map<String, ConcurrentHashMap<String, Cookie>> cookies = new HashMap<>();

    @Override
    public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {

        System.out.println("TransientCookieJar.saveFromResponse" + httpUrl.toString() + "\tcookies=" + list.size());
        if (list.size() > 0) {
            for (Cookie item : list) {
                add(httpUrl, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        System.out.println(cookies.size());
        System.out.print("TransientCookieJar.loadForRequest" + url.toString());
        ArrayList<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(url.host())) {
            Collection<Cookie> cookie = cookies.get(url.host()).values();
            for (Cookie c:
                 cookie) {
                System.out.print(c.name() + "="+c.value());
            }
            ret.addAll(cookie);
        }
        System.out.println("\t cookies=" + ret.size());
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
                ConcurrentHashMap<String, Cookie> cookieConcurrentMap = new ConcurrentHashMap<>();
                cookieConcurrentMap.put(url.host(), cookie);
                cookies.put(url.host(), cookieConcurrentMap);
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