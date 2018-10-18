package cn.denua.v2ex.http.cookie;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
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
 * @user denua
 * @date 2018/10/18
 */
public class TransientCookieJar implements CookieJar {

    // cookie key value pairs for each host
    private Map<String, ConcurrentHashMap<String, Cookie>> cookies = new HashMap<>();

    @Override
    public void saveFromResponse(@NonNull HttpUrl httpUrl, @NonNull List<Cookie> list) {

        System.out.println(httpUrl.toString() + "\tTransientCookieJar.saveFromResponse size: " + list.size());
        if (list.size() > 0) {
            for (Cookie item : list) {
                System.out.println("\t" + item.name() + ": " + item.value());
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
        System.out.println( url.toString() + "\tTransientCookieJar.loadForRequest");
        for (Iterator<Cookie> it = ret.iterator(); it.hasNext(); ) {
            Cookie cookieIterator = it.next();
            System.out.println("\t"+cookieIterator.name() + ": "+cookieIterator.value());
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

        Cookie repairedCookie;
        if (name.equals("A2@.v2ex.com")){
            name="A2@www.v2ex.com";
        }
        if (name.equals("V2EX_LANG@www.v2ex.com")){
            repairedCookie = new Cookie.Builder()
                    .domain(cookie.domain())
                    .name(cookie.name())
                    .expiresAt(System.currentTimeMillis() + 1000000)
                    .path(cookie.path())
                    .value(cookie.value())
                    .build();
        }else {
            repairedCookie = cookie;
        }

        // cookie 是否过期, 有效则
        if (repairedCookie.persistent()) {
            // 如果该 cookie 不存在则创建, 有则覆盖
            if (!cookies.containsKey(host)) {
                cookies.put(host, new ConcurrentHashMap<>());
            }
            cookies.get(host).put(name, repairedCookie);

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