package com.jeremelau.libapplication.net;

import android.content.Context;

import com.guoguang.framework.mvp.RxPresenter;
import com.guoguang.framework.network.BaseRequest;
import com.guoguang.framework.network.CommonResp;
import com.guoguang.framework.network.OnNetRequestListener;
import com.guoguang.framework.network.RxHelper;
import com.guoguang.framework.network.Subscribe2Help;
import com.jeremelau.libapplication.api.TestApi;
import com.jeremelau.libapplication.bean.TestResp;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.Interceptor;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class TestRequest extends BaseRequest<TestApi> {
    public TestRequest(Context context, RxPresenter mRxPresenter) {
        super(context.getApplicationContext(), "http://jeremelau.com:8089", mRxPresenter, null, TestApi.class);
    }

    public void testRequest(String test1, String test2, OnNetRequestListener listener) {
        Observable<CommonResp<List<TestResp>>> observable = webRequest.test(test1, test2);
        listener.onStart();
        observable.compose(RxHelper.rxSchedulerHelper())
                .compose(RxHelper.handleResult())
                .subscribe(new Subscribe2Help<CommonResp<List<TestResp>>>(mRxPresenter) {
                    @Override
                    public void onNext(CommonResp<List<TestResp>> value) {
                        listener.onSuccess(value);
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        super.onError(throwable);
                        listener.onFailure(throwable);
                    }

                    @Override
                    public void onComplete() {
                        listener.onFinish();
                    }
                });
    }
}
