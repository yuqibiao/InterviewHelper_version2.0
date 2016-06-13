package com.yyyu.interviewhelper.net;

import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;
import com.google.gson.internal.$Gson$Types;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.yyyu.interviewhelper.utils.MyLog;
import com.yyyu.interviewhelper.utils.OkHttpClientManager;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.FileNameMap;
import java.net.URLConnection;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * 功能：对OkHttp的封装
 *
 * @author yyyu
 * @date 2016/5/19
 */
public class MyHttpManager {

    private static MyHttpManager mInstance;
    private OkHttpClient okHttpClient;
    private Handler mHandler;
    private Gson gson;

    private MyHttpManager() {
        okHttpClient = new OkHttpClient();
        okHttpClient.setConnectTimeout(5, TimeUnit.SECONDS);
        mHandler = new Handler(Looper.getMainLooper());
        gson = new Gson();
    }

    public static MyHttpManager getInstance() {
        synchronized (MyHttpManager.class) {
            while (mInstance == null) {
                mInstance = new MyHttpManager();
            }
        }
        return mInstance;
    }

    /**
     * 返回Response的同步GET请求
     *
     * @param urlStr
     * @return
     * @throws IOException
     */
    private Response _getSyncAsResponse(String urlStr) throws IOException {
        Request request = new Request.Builder().url(urlStr).build();
        Call call = okHttpClient.newCall(request);
        return call.execute();
    }

    /**
     * 返回String的同步GET请求
     *
     * @param urlStr
     * @return
     * @throws IOException
     */
    private String _getSyncAsString(String urlStr) throws IOException {
        return getSyncAsResponse(urlStr).body().string();
    }

    /**
     * 异步GET请求
     *
     * @param urlStr
     * @param resultCallback
     */
    private void _getAsyn(String urlStr, final ResultCallback resultCallback) {
        Request request = new Request.Builder()
                .url(urlStr)
                .build();
        deliveryResult(resultCallback, request);
    }

    /**
     * 异步的post请求
     */
    private void _postAsyn(String url, ResultCallback resultCallback, Map<String, String> params) {
        Param[] paramsArr = map2Params(params);
        Request request = buildPostRequest(url, paramsArr);
        deliveryResult(resultCallback, request);
    }

    /**
     * 异步基于post的文件上传
     *
     * @param url
     * @param callback
     * @param files    上传的文件
     * @param fileKeys 文件字段名
     * @throws IOException
     */
    private void _postAsynUpload(String url, ResultCallback callback,
                                 File[] files, String[] fileKeys, Param... params)
            throws IOException {
        Request request = buildMultipartFormRequest(url, files, fileKeys, params);
        deliveryResult(callback, request);
    }


    private Request buildMultipartFormRequest(String url, File[] files,
                                              String[] fileKeys, Param[] params) {
        params = validateParam(params);

        MultipartBuilder builder = new MultipartBuilder()
                .type(MultipartBuilder.FORM);

        for (Param param : params) {
            builder.addPart(Headers.of("Content-Disposition", "form-data; name=\"" + param.key + "\""),
                    RequestBody.create(null, param.value));
        }
        if (files != null) {
            RequestBody fileBody = null;
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                String fileName = file.getName();
                fileBody = RequestBody.create(MediaType.parse(guessMimeType(fileName)), file);
                //TODO 根据文件名设置contentType
                builder.addPart(Headers.of("Content-Disposition",
                        "form-data; name=\"" + fileKeys[i] + "\"; filename=\"" + fileName + "\""),
                        fileBody);
            }
        }

        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }

    private String guessMimeType(String path) {
        FileNameMap fileNameMap = URLConnection.getFileNameMap();
        String contentTypeFor = fileNameMap.getContentTypeFor(path);
        if (contentTypeFor == null) {
            contentTypeFor = "application/octet-stream";
        }
        return contentTypeFor;
    }

    private Param[] validateParam(Param[] params) {
        if (params == null)
            return new Param[0];
        else return params;
    }


    /**
     * 请求结果的调度
     *
     * @param resultCallback
     * @param request
     */
    private void deliveryResult(final ResultCallback resultCallback, Request request) {
        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                sendFailedStringCallback(request, e, resultCallback);
            }

            @Override
            public void onResponse(Response response) throws IOException {
                try {
                    final String string = response.body().string();
                    if (resultCallback.mType == String.class) {
                        sendSuccessResultCallback(string, resultCallback);
                    } else {
                        MyLog.e("OKHttp----------" + string);
                        Object o = gson.fromJson(string, resultCallback.mType);
                        sendSuccessResultCallback(o, resultCallback);
                    }
                } catch (IOException e) {
                    sendFailedStringCallback(response.request(), e, resultCallback);
                } catch (com.google.gson.JsonParseException e)//Json解析的错误
                {
                    sendFailedStringCallback(response.request(), e, resultCallback);
                }
            }
        });
    }

    /**
     * 返回结果的回调
     */
    public static abstract class ResultCallback<T> {
        Type mType;

        public ResultCallback() {
            mType = getSuperclassTypeParameter(getClass());
        }

        static Type getSuperclassTypeParameter(Class<?> subclass) {
            Type superclass = subclass.getGenericSuperclass();
            if (superclass instanceof Class) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            return $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
        }

        public abstract void onError(Request request, Exception e);

        public abstract void onResponse(T response);
    }

    /**
     * 网络请求失败返回结果
     */
    private void sendFailedStringCallback(final Request request, final Exception e, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null)
                    callback.onError(request, e);
            }
        });
    }

    /**
     * 网络请求成功的返回结果
     */
    private void sendSuccessResultCallback(final Object object, final ResultCallback callback) {
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onResponse(object);
                }
            }
        });
    }

    public static class Param {
        public Param() {
        }

        public Param(String key, String value) {
            this.key = key;
            this.value = value;
        }

        String key;
        String value;
    }

    private Param[] map2Params(Map<String, String> params) {
        if (params == null) return new Param[0];
        int size = params.size();
        Param[] res = new Param[size];
        Set<Map.Entry<String, String>> entries = params.entrySet();
        int i = 0;
        for (Map.Entry<String, String> entry : entries) {
            res[i++] = new Param(entry.getKey(), entry.getValue());
        }
        return res;
    }

    private Request buildPostRequest(String url, Param[] params) {
        if (params == null) {
            params = new Param[0];
        }
        FormEncodingBuilder builder = new FormEncodingBuilder();
        for (Param param : params) {
            builder.add(param.key, param.value);
        }
        RequestBody requestBody = builder.build();
        return new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();
    }


    //----------------------------*************向外暴露方法**********-----------------------------

    public static Response getSyncAsResponse(String urlStr) throws IOException {
        return getInstance()._getSyncAsResponse(urlStr);
    }

    public static String getSyncAsString(String urlStr) throws IOException {
        return getInstance()._getSyncAsString(urlStr);
    }

    public static void getAsyn(String urlStr, final ResultCallback resultCallback) {
        getInstance()._getAsyn(urlStr, resultCallback);
    }

    public static void postAsyn(String urlStr, ResultCallback resultCallback, Map<String, String> params) {
        getInstance()._postAsyn(urlStr, resultCallback, params);
    }

    public  static void postAsynUpload(String url, ResultCallback callback,
                                       File[] files, String[] fileKeys, Param... params)
            throws IOException {
        getInstance()._postAsynUpload(url, callback, files, fileKeys, params);
    }


}
