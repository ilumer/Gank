package com.fast.ilumer.gank.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.fast.ilumer.gank.App;
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
            if (save(response,fileDir(),Url)) {
                saveFileSuccess();
            }else {
                saveFileFail();
            }
        }catch (IOException ex){
            ex.printStackTrace();
            saveFileFail();
        }finally {
            if (response!=null) {
                response.body().close();
            }
        }
    }

    public void saveFileSuccess(){
        handler.post(() ->
                Toast.makeText(this, getString(R.string.download_success),
                        Toast.LENGTH_SHORT).show()
        );
    }

    public void saveFileFail(){
        handler.post(()->
                Toast.makeText(this,getString(R.string.download_fail),
                        Toast.LENGTH_SHORT).show()
        );
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

    private File fileDir(){
        File pubDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        //私有文件夹并不能被图库添加
        return new File(pubDir,"Gank");
    }

    private boolean save(Response response,File destinationDir,String uri) throws IOException{
        if (!destinationDir.isDirectory() && !destinationDir.mkdir()){
            return false;
        }
        String pathName = initImgName();
        File saveName = new File(destinationDir,pathName);
        if (saveName.exists()){
            return false;
        }
        //生成文件
        response =RetrofitHelper.getClientInstance().newCall(initRequest(uri)).execute();
        BufferedSink sink = Okio.buffer(Okio.sink(saveName));
        sink.writeAll(response.body().source());
        sink.close();
        //下载文件
        Uri contentUri = Uri.fromFile(saveName);
        Intent mediaIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaIntent.setData(contentUri);
        sendBroadcast(mediaIntent);
        //通知图库更新
        return true;
    }
}