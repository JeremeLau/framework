package com.guoguang.framework.network;

import android.content.Context;
import android.util.Log;

import com.guoguang.framework.BuildConfig;
import com.guoguang.framework.network.callback.ProgressResponseBody;
import com.guoguang.framework.network.callback.ProgressResponseListener;
import com.guoguang.framework.network.utils.NetworkUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import androidx.annotation.NonNull;
import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class RetrofitService {
    private static final int MAX_STALE = 60 * 60 * 24 * 28;
    private static final int CACHE_SIZE = 10 * 1024 * 1024;
    private static OkHttpClient mOkHttpClient;
    private volatile static Retrofit retrofit = null;
    private static ProgressResponseListener mListener;

    public static Retrofit createRetrofit(Context context, String baseUrl) {
        if (retrofit == null) {
            synchronized (RetrofitService.class) {
                if (retrofit == null) {
                    if (mOkHttpClient == null) {
                        initOkHttpClient(context);
                    }
                    retrofit = new Retrofit.Builder()
                            .client(mOkHttpClient)
                            .baseUrl(baseUrl)
                            .addConverterFactory(GsonConverterFactory.create())
                            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                            .build();
                }
            }
        }
        return retrofit;
    }

    private static Interceptor downloadInterceptor = new Interceptor() {
        @Override
        public Response intercept(Chain chain) throws IOException {
            //拦截
            Response originalResponse = chain.proceed(chain.request());

            //包装响应体并返回
            return originalResponse.newBuilder()
                    .body(new ProgressResponseBody(originalResponse.body(), mListener))
                    .build();
        }
    };

    public static Retrofit createRetrofit(Context context, String baseUrl, ProgressResponseListener listener) {
        mListener = listener;
        synchronized (RetrofitService.class) {
            initOkHttpClient(context);
            return new Retrofit.Builder()
                    .client(mOkHttpClient.newBuilder().addInterceptor(downloadInterceptor).build())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .build();
        }
    }

    // 配置OkHttpClient
    private static void initOkHttpClient(Context context) {
        if (mOkHttpClient == null) {
            final HashMap<String, Cookie> cookieStore = new HashMap<>();
            OkHttpClient.Builder httpBuilder = new OkHttpClient.Builder();

            /**
             *
             * 日志拦截器
             */
            Interceptor loggingInterceptor = chain -> {
                Request request = chain.request();

                long t1 = System.nanoTime();
                if (BuildConfig.DEBUG) {
                    Log.e("TAG", String.format("Sending request %s on %s%n%s", request.url(), chain.connection(), request.headers()));
                }
                Response response = chain.proceed(request);
                long t2 = System.nanoTime();
                if (BuildConfig.DEBUG) {
                    Log.e("TAG", String.format("Received response for %s in %.1fms%n%s", response.request().url(), (t2 - t1) / 1e6d, response.headers()));
                }
                return response;
            };

            /**
             * 缓存策略
             */
            Interceptor cacheInterceptor = chain -> {
                Request request = chain.request();
                if (!NetworkUtils.isNetworkConnected(context)) {
                    request = request.newBuilder()
                            .cacheControl(CacheControl.FORCE_CACHE)
                            .build();
                }

                Response response = chain.proceed(request);
                if (NetworkUtils.isNetworkConnected(context)) {
                    String cacheControl = request.cacheControl().toString();
                    response = response.newBuilder()
                            .removeHeader("Pragma")
                            .header("Cache-Control", cacheControl)
                            .build();

                } else {
                    response = response.newBuilder()
                            .header("Cache-Control", "public, only-if-cached, max-stale=" + MAX_STALE)
                            .removeHeader("Pragma")
                            .build();

                }
                return response;
            };


            /**
             * cookie保持
             */
            httpBuilder.cookieJar(new CookieJar() {
                @Override
                public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
                    if (cookies.size() > 0) {
                        for (Cookie cookie : cookies) {
                            if (cookie.name().equals("SESSION")) {
                                cookieStore.put(url.host(), cookie);
                                break;
                            }
                        }
                    }
                }

                @Override
                public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
                    List<Cookie> cookies = new ArrayList<>();
                    Cookie cookie = cookieStore.get(url.host());
                    if (cookie != null) {
                        cookies.add(cookie);
                    }
                    return cookies;
                }
            });

            /**
             * HTTPS验证
             */
            httpBuilder.hostnameVerifier((hostname, session) -> {
                //强行返回true 即验证成功
                return true;
            }).sslSocketFactory(createSSLSocketFactory());

            /**
             * 缓存设置
             */
            Cache cache = new Cache(new File(context.getCacheDir(), "mmspcache"), CACHE_SIZE);
            httpBuilder.cache(cache);
            httpBuilder.addNetworkInterceptor(cacheInterceptor);
            //httpBuilder.addInterceptor(loggingInterceptor);
            httpBuilder.addInterceptor(loggingInterceptor);
            /**
             * 设置超时
             *
             */
            httpBuilder.readTimeout(30, TimeUnit.SECONDS)
                    .connectTimeout(30, TimeUnit.SECONDS)
                    .writeTimeout(30, TimeUnit.SECONDS);
            mOkHttpClient = httpBuilder.build();
        }
    }

    /*public static OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            initOkHttpClient();
        }
        return mOkHttpClient;
    }*/

    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;

        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());

            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
        }

        return ssfFactory;
    }

    /**
     * 绑定证书
     *
     * @param context
     * @param certificates
     * @return
     * @throws CertificateException
     * @throws KeyStoreException
     * @throws IOException
     * @throws NoSuchAlgorithmException
     * @throws KeyManagementException
     */
    private static SSLSocketFactory getSSLSocketFactory(Context context, int[] certificates)
            throws CertificateException, KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException

    {

        if (context == null) {
            throw new NullPointerException("context == null");
        }

        CertificateFactory certificateFactory;
        certificateFactory = CertificateFactory.getInstance("X.509");
        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(null, null);

        for (int i = 0; i < certificates.length; i++) {
            InputStream certificate = context.getResources().openRawResource(certificates[i]);
            keyStore.setCertificateEntry(String.valueOf(i), certificateFactory.generateCertificate(certificate));

            if (certificate != null) {
                certificate.close();
            }
        }
        SSLContext sslContext = SSLContext.getInstance("TLS");
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        //TrustManager[] wrappedTrustManagers =getWrappedTrustManagers(trustManagerFactory.getTrustManagers());
        trustManagerFactory.init(keyStore);
        sslContext.init(null, trustManagerFactory.getTrustManagers(), new SecureRandom());
        return sslContext.getSocketFactory();

    }

    /**
     * 自定义TrustManager类
     *
     * @param trustManagers
     * @return
     */
    private static TrustManager[] getWrappedTrustManagers(TrustManager[] trustManagers) {

        final X509TrustManager originalTrustManager = (X509TrustManager) trustManagers[0];

        return new TrustManager[]{
                new X509TrustManager() {
                    public X509Certificate[] getAcceptedIssuers() {
                        return originalTrustManager.getAcceptedIssuers();
                    }

                    public void checkClientTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkClientTrusted(certs, authType);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }

                    public void checkServerTrusted(X509Certificate[] certs, String authType) {
                        try {
                            originalTrustManager.checkServerTrusted(certs, authType);
                        } catch (CertificateException e) {
                            e.printStackTrace();
                        }
                    }
                }
        };
    }

    /**
     * 构建HostnameVerifier
     *
     * @param hostUrls
     * @return
     */
    private static HostnameVerifier getHostnameVerifier(final String[] hostUrls) {
        HostnameVerifier TRUSTED_VERIFIER = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session) {
                boolean ret = false;
                for (String host : hostUrls) {
                    if (host.equalsIgnoreCase(hostname)) {
                        ret = true;
                    }
                }
                return ret;
            }
        };

        return TRUSTED_VERIFIER;
    }

    public static class TrustAllCerts implements X509TrustManager {
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {

        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            return new X509Certificate[0];
        }
    }
}
