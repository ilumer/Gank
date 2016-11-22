package com.fast.ilumer.gank;

import android.graphics.Point;
import android.view.Display;

/**
 * Created by root on 11/22/16.
 */

public class Util {
    public static int getPhoneWidth(Display display){
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
