package com.fast.ilumer.gank;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import com.fast.ilumer.gank.dao.Db;
import com.fast.ilumer.gank.dao.DbOpenHelper;

import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() throws Exception {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getTargetContext();
        DbOpenHelper helper = new DbOpenHelper(appContext);
        helper.getReadableDatabase().query(Db.TYPE_TABLE_NAME,
                new String[]{"Distinct(type)"},
                null,
                null,
                null,
                null,
                null);
    }
}
