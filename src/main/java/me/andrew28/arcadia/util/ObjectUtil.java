package me.andrew28.arcadia.util;

import java.util.Random;

/**
 * Created by Andrew Tran on 12/4/2016
 */
public class ObjectUtil {
    public static <T> T getRandom(T... t){
        int rnd = new Random().nextInt(t.length);
        return t[rnd];
    }
}
