package com.fast.ilumer.gank;

import android.content.res.Resources;
import android.net.Uri;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

/**
 * Created by root on 11/22/16.
 */

public class Util {
    private static final String strSeparator= "_,_";

    private static SimpleDateFormat initFormat(){
        //http://stackoverflow.com/questions/8405087/what-is-this-date-format-2011-08-12t201746-384z
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.CHINA);
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

    public static String convertArrayToString(String[] array){
        StringBuilder builder = new StringBuilder();
        for (int i =0 ; i < array.length; i++){
            builder.append(array[i]).append(strSeparator);
        }
        return builder.toString();
    }

    public static String[] convertStringToArray(String str){
        return str.split(strSeparator);
    }


    public static int[] getDate(Date date){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        //http://stackoverflow.com/questions/344380/why-is-january-month-0-in-java-calendar
        int day = cal.get(Calendar.DAY_OF_MONTH);
        return new int[]{year,month,day};
    }

    public static int getScreenWidth(){
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight(){
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public static Uri commonImageParse(String url){
        int width = Math.round(getScreenWidth()/1.618f);
        String returnUri = url+"?imageView2/0/w/"+width;
        Log.e("TAGComm",returnUri);
        return Uri.parse(returnUri);
    }


    /*
    * 新浪图片不支持压缩
    * */
    public static Uri meiZiImageParse(String url, float aspectRatio){
        int width = getScreenWidth()/3;
        int height = Math.round(width/aspectRatio);
        String returnUri = url + "?imageView2/1/w/" + width + "/h/" + height;
        return Uri.parse(returnUri);
    }
}
