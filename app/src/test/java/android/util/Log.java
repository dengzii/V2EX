/*
 * Copyright (c) 2018 denua.
 */

package android.util;

/*
 * mocked Log printer
 *
 * @author denua
 * @date 2018/11/07 14
 */
public final class Log {

    public static void d(String tag, String s){

        System.out.println((tag == null?"":tag + ": ") + s);
    }

    public static void e(String tag, String s){

        System.err.println((tag == null?"":tag + ": ") + s);
    }
}
