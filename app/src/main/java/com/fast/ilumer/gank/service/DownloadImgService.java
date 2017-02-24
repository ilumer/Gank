package com.fast.ilumer.gank.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.os.Handler;
import android.provider.MediaStore;
import android.widget.Toast;

import com.fast.ilumer.gank.R;
import com.fast.ilumer.gank.fragment.PictureFragment;
import com.fast.ilumer.gank.network.RetrofitHelper;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import okhttp3.Request;
import okhttp3.Response;
import okio.BufferedSink;
import okio.Okio;

/**
 *
 */
public class DownloadImgService extends IntentService {

    public Handler handler;

    public DownloadImgService(){
        this("DownloadImgService");
    }

    public DownloadImgService(String name) {
        super(name);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String Url = intent.getStringExtra(PictureFragment.EXTRA_URL);
        Response response = null;
        try{
            response =RetrofitHelper.getClientInstance().newCall(initRequest(Url)).execute();
            File file = new File(this.getFilesDir(),initImgName());
            BufferedSink sink = Okio.buffer(Okio.sink(file));
            sink.writeAll(response.body().source());
            sink.close();
            MediaStore.Images.Media.insertImage(getContentResolver(),file.getPath(),file.getName(),"MEIZI");
            //http://stormzhang.com/android/2014/07/24/android-save-image-to-gallery/
            handler.post(()->{
                Toast.makeText(this,getString(R.string.download_success),
                        Toast.LENGTH_SHORT).show();
            });
        }catch (IOException ex){
            handler.post(()->{
                Toast.makeText(this,getString(R.string.download_fail),
                        Toast.LENGTH_SHORT).show();
            });
        }finally {
            if (response!=null) {
                response.body().close();
            }
        }
    }

    private Request initRequest(String url){
        Request request = new Request.Builder()
                .url(url)
                .build();
        return request;
    }

    private String initImgName(){
        UUID uuid = UUID.randomUUID();
        return uuid.toString()+".jpg";
    }
}