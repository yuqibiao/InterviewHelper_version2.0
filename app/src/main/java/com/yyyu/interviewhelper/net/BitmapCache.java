package com.yyyu.interviewhelper.net;

import android.graphics.Bitmap;
import android.support.v4.util.LruCache;

import com.android.volley.toolbox.ImageLoader;
import com.yyyu.interviewhelper.utils.MyLog;

/**
 * 功能：图片缓存的阈值
 *
 * @author yyyu
 * @date 2016/5/27
 */
public class BitmapCache implements ImageLoader.ImageCache {

    private int maxSize ;
    private LruCache<String, Bitmap> mCache;

    public BitmapCache(){
        int maxMemory = (int) Runtime.getRuntime().maxMemory();
        MyLog.e("=======maxMemory="+maxMemory);
        maxSize = maxMemory/8;
        mCache = new LruCache<String ,Bitmap>(maxSize){
            @Override
            protected int sizeOf(String str, Bitmap bitmap) {
                return bitmap.getRowBytes()*bitmap.getHeight();
            }
        };
    }

    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url , bitmap);
    }
}
