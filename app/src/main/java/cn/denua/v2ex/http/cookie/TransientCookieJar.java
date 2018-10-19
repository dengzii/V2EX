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

    public TransientCookieJar(){

//        cookies.put("www.v2ex.com", new ConcurrentHashMap<String, Cookie>(){{
//            put("PB3_SESSION@.v2ex.com", new Cookie.Builder()
//                    .domain("v2ex.com")
//                    .expiresAt(System.currentTimeMillis()+1000000)
//                    .name("PB3_SESSION")
//                    .value("\"2|1:0|10:1539879384|11:PB3_SESSION|40:djJleDoxMTcuMTM2LjExMC4xMzA6MzA5NjM3NzQ=|fd1d0c0f98daf4b1f399ee1cf144998c1fbaa6e70028b1db0d69d067ddb66da0\"")
//                    .path("/")
//                    .build());
//            put("V2EX_LANG@.v2ex.com", new Cookie.Builder()
//                    .domain("v2ex.com")
//                    .expiresAt(System.currentTimeMillis()+1000000)
//                    .name("V2EX_LANG")
//                    .value("zhcn")
//                    .path("/")
//                    .build());
//            put("V2EX_TAB@.v2ex.com", new Cookie.Builder()
//                    .domain("v2ex.com")
//                    .expiresAt(System.currentTimeMillis()+1000000)
//                    .name("V2EX_TAB")
//                    .value("\"2|1:0|10:1539921690|8:V2EX_TAB|12:Y3JlYXRpdmU=|c5b776e07c5762a04f9b5dda719c2a2f6ba2682b6ee28d82e2ff0372af647816\"")
//                    .path("/")
//                    .build());
//            put("A2@.v2ex.com", new Cookie.Builder()
//                    .domain("v2ex.com")
//                    .expiresAt(System.currentTimeMillis()+1000000)
//                    .name("A2")
//                    .value("\"2|1:0|10:1539879602|2:A2|56:ZDFkN2UyZTc1N2VlMmJmOTY5OWViMzE1ZmQzNGVjNGJkMWMwM2M2OQ==|9a808feaff62c0fc7e539205d9f26618dd538d7c96fdef3b8be74a16e56a76ae\"")
//                    .path("/")
//                    .build());
//        }});
    }
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