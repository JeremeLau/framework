package com.guoguang.framework.network;


import android.content.Context;

import com.guoguang.framework.mvp.RxPresenter;

import java.util.List;

import okhttp3.Interceptor;
import retrofit2.Retrofit;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class BaseRequest<T> {
    protected RxPresenter mRxPresenter;
    protected T webRequest;

    public BaseRequest(Context context, String baseUrl, RxPresenter mRxPresenter, List<Interceptor> interceptor, Class<T> service) {
        Retrofit mRetrofit = RetrofitService.createRetrofit(context, baseUrl, interceptor);
        this.mRxPresenter = mRxPresenter;
        webRequest = mRetrofit.create(service);
    }
}
