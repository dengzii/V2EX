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

/*
 * Transient cookie manager
 * Holding cookies until object destroy
 *
 * @author denua
 * @date 2018/10/18
 */
public class TransientCookieJar implements CookieJar {

    // cookie key value pairs for each host
    private Map<String, ConcurrentHashMap<String, Cookie>> cookies = new HashMap<>();

    public TransientCookieJar(){
    }

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

        List<Cookie> ret = new ArrayList<>();
        if (cookies.containsKey(url.host())) {
            Collection<Cookie> cookie = cookies.get(url.host()).values();
            ret.addAll(cookie);
        }
        return ret;
    }

    /**
     * add cookie into map
     *
     * @param url the url
     * @param cookie the cookie
     */
    private void add(HttpUrl url, Cookie cookie){

        String name = cookie.name() + "@" + cookie.domain();
        String host = url.host();

        Cookie fixedCookie = cookie;
        if (name.equals("A2@.v2ex.com")){
            name="A2@www.v2ex.com";
        }

        long maxAge = System.currentTimeMillis() + 2000000L;
        if ((cookie.expiresAt() > maxAge)){
            fixedCookie = new Cookie.Builder()
                    .domain(cookie.domain())
                    .name(cookie.name())
                    .expiresAt(maxAge)
                    .path(cookie.path())
                    .value(cookie.value())
                    .build();
        }

        // cookie 是否过期, 有效则插入
        if (fixedCookie.persistent()) {
            // 如果该 cookie 不存在则创建, 有则覆盖
            if (!cookies.containsKey(host)) {
                cookies.put(host, new ConcurrentHashMap<>());
            }
            cookies.get(host).put(name, fixedCookie);

        } else {
            if (cookies.containsKey(host)) {
                cookies.get(host).remove(name);
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