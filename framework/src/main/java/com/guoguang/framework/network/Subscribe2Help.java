package com.guoguang.framework.network;

import com.guoguang.framework.mvp.RxPresenter;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public abstract class Subscribe2Help<T> implements Observer<T> {
    RxPresenter mRxPresenter;

    public Subscribe2Help(RxPresenter rxPresenter) {
        this.mRxPresenter = rxPresenter;
    }

    /**
     * 将请求添加到 CompositeDisposable 对象中 用于管理订阅的生命周期
     *
     * @param d
     */
    @Override
    public void onSubscribe(Disposable d) {
        mRxPresenter.addSubscribe(d);
    }

    /**
     * 异常处理
     *
     * @param throwable
     */
    @Override
    public void onError(Throwable throwable) {
        //Do something with Error
    }

    @Override
    public void onComplete() {

    }
}
