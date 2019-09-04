package com.guoguang.utils.rxbus;

import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: Created by jereme on 2018/7/30
 * E-main: liuqx@guoguang.com.cn
 */
public class RxHelper {
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return (upstream) -> upstream.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread());
    }
}
