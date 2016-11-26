package com.fast.ilumer.gank;

import android.graphics.Point;
import android.util.Log;
import android.view.Display;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by root on 11/22/16.
 */

public class Util {
    public static int getPhoneWidth(Display display){
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }

    private static SimpleDateFormat initFormat(){
        //http://stackoverflow.com/questions/8405087/what-is-this-date-format-2011-08-12t201746-384z
        SimpleDateFormat format = new SimpleDateFormat("YYYY-MM-DDTHH:MM:SS", Locale.CHINA);
        format.setTimeZone(TimeZone.getTimeZone("UTC"));
        return format;
    }

    public static Date dateparse(String str){
        Date date = new Date();
      SimpleDateFormat format = initFormat();
        try{
            date = format.parse(str);
        }catch (ParseException ex){
            Log.e(ex.getMessage(),"");
        }
        return date;
    }

    public static String formatDate(Date date){
        String str = null;
        SimpleDateFormat format = initFormat();
        str = format.format(date);
        return str;
    }
}
