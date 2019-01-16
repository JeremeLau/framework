package com.guoguang.framework.mvp;

import android.util.Log;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class RxPresenter<T extends BaseView> implements BasePresenter<T> {
    protected T mView;
    protected CompositeDisposable mCompositeDisposable;


    /**
     * RxJava 取消订阅的方法
     */
    protected void unSubscribe() {
        if (mCompositeDisposable != null) {
          mCompositeDisposable.clear();
        }
        Log.d("TAG", "停止所有的请求");
    }

    public void addSubscribe(Disposable disposable) {
        if (mCompositeDisposable == null) {
          mCompositeDisposable = new CompositeDisposable();
        }
        Log.d("TAG", "请求入列");
        mCompositeDisposable.add(disposable);
    }

    @Override
    public void attachView(T view) {
        this.mView = view;
    }

    @Override
    public void detachView() {

      /**
       * 取消订阅关系的方法  将持有的Activity引用置为空
       */
        this.mView = null;
        unSubscribe();
    }
}
