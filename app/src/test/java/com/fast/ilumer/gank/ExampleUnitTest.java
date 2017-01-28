package com.fast.ilumer.gank;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void testUtil() throws Exception{
        List<String> mlist = new ArrayList<>();
        mlist.add("adadada");
        String[] arrays =mlist.toArray(new String[0]);
    }

    @Test
    public void testConvertStringToArray() throws Exception{
        List<String> mlist = new ArrayList<>();
        mlist.add("adadada");
        String[] arrays =mlist.toArray(new String[0]);
        System.out.println(Util.convertArrayToString(arrays));
        String[] array = Util.convertStringToArray(Util.convertArrayToString(arrays));
        for (String str:array){
            System.out.println(str);
        }
    }

}