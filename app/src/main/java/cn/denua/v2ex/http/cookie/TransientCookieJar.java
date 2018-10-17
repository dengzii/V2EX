package cn.denua.v2ex.http.cookie;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Transient cookie manager
 * Holding cookies until object destroy
 *
 * by denua
 */
public class TransientCookieJar implements CookieJar {

    // cookie key value pairs for each host
    private HashMap<String, ConcurrentHashMap<String, Cookie>> cookies = new HashMap<>();

    @Override
    public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {

        System.out.println("TransientCookieJar.saveFromResponse  " + list.size());
        if (list.size() > 0) {
            for (Cookie item : list) {
                add(httpUrl, item);
            }
        }
        System.out.println("TransientCookieJar.saveFromResponse  " + cookies.size());
    }

    @Override
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {

        List<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(url.host())) {
            Collection<Cookie> cookie = cookies.get(url.host()).values();
            ret.addAll(cookie);
        }
        System.out.println("TransientCookieJar.loadForRequest  " + ret.size() + "/" + cookies.size());
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
        String host = url.host();
        Cookie repaired;

        if (name.equals("V2EX_LANG@www.v2ex.com")){
            repaired = new Cookie.Builder()
                    .name(cookie.name())
                    .value(cookie.value())
                    .domain(cookie.domain())
                    .path(cookie.path())
                    .expiresAt(System.currentTimeMillis() + 1000000000L)
                    .httpOnly()
                    .secure()
                    .build();
        }else {
            repaired = cookie;
        }

//        System.out.println("cookie=" + name + "\t persistent=" + repaired.persistent() + "\texpired at=" +repaired.expiresAt());
        // cookie 是否过期, 有效则
        if (!repaired.persistent()) {
            // 如果该 cookie 不存在则创建, 有则覆盖
            if (!cookies.containsKey(host)) {
                cookies.put(host, new ConcurrentHashMap<>());
            }
            cookies.get(host).put(name, repaired);

        } else {
            if (cookies.containsKey(host)) {
                cookies.get(host).remove(name);
            }
        }
//        System.out.println("time=" + System.currentTimeMillis() + "\t cookies size=" + cookies.size());
    }

    /**
     * clean all
     */
    public void clear(){

        cookies.clear();
    }
}