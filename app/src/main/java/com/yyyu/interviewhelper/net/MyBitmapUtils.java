package com.yyyu.interviewhelper.net;

import android.content.Context;
import android.view.Display;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.yyyu.interviewhelper.R;
import com.yyyu.interviewhelper.utils.MyLog;

/**
 * 功能：对Volley加载图片功能的封装
 *
 * @author yyyu
 * @date 2016/5/27
 */
public class MyBitmapUtils {

    private static MyBitmapUtils instance;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private MyBitmapUtils(Context ctx) {
        requestQueue = Volley.newRequestQueue(ctx);
        imageLoader = new ImageLoader(requestQueue, new BitmapCache());
    }

    public static MyBitmapUtils getInstance(Context ctx) {
        synchronized (MyBitmapUtils.class) {
            while (instance == null) {
                instance = new MyBitmapUtils(ctx);
            }
            return instance;
        }
    }

    public void display(ImageView iv, String url ) {
        display(iv , url , R.mipmap.img_err);
    }

    public void display(ImageView iv, String url , int defImgRes) {
        iv.measure(0 , 0 );
        ImageLoader.ImageListener listener = imageLoader.getImageListener
                (iv, R.mipmap.img_df, defImgRes);
        imageLoader.get(url, listener, iv.getMeasuredWidth(), iv.getMeasuredHeight());
    }

    public void display(ImageView iv , String url , ImageLoader.ImageListener listener){
        iv.measure(0 , 0 );
        imageLoader.get(url, listener, iv.getMeasuredWidth(), iv.getMeasuredHeight());
    }

    /**
     * 在会调用得到bitmap用
     * @param url
     * @param listener
     */
    public void display(String url , ImageLoader.ImageListener listener){
        imageLoader.get(url, listener);
    }


}
