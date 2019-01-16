package com.guoguang.framework.network;


import android.content.Context;

import com.guoguang.framework.mvp.RxPresenter;

import retrofit2.Retrofit;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class BaseRequest<T> {
    protected RxPresenter mRxPresenter;
    protected T webRequest;

    public BaseRequest(Context context, String baseUrl, RxPresenter mRxPresenter, Class<T> service) {
        Retrofit mRetrofit = RetrofitService.createRetrofit(context, baseUrl);
        this.mRxPresenter = mRxPresenter;
        webRequest = mRetrofit.create(service);
    }
}
