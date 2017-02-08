package com.fast.ilumer.gank;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ${ilumer} on 1/31/17.
 */
public class UtilTest {

    @Test
    public void convertArrayToString() throws Exception {
        String testStr = "adadada";
        String strSeparator= "_,_";
        List<String> mlist = new ArrayList<>();
        mlist.add(testStr);
        String[] arrays =mlist.toArray(new String[0]);
        Assert.assertEquals(testStr+strSeparator,Util.convertArrayToString(arrays));
    }

    @Test
    public void convertStringToArray() throws Exception {
        String str = "adadada_,_";
        String[] array1 = new String[]{"adadada"};
        String[] array2 = Util.convertStringToArray(str);
        Assert.assertArrayEquals(array1,array2);
    }

    @Test
    public void getTime() throws Exception {
        String datestr = "2011-08-12T20:17:46.384Z";
        Date date= Util.dateparse(datestr);
        int[] time = Util.getDate(date);
        Assert.assertArrayEquals(new int[]{2011,7,13},time);
    }

}