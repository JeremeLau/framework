package com.guoguang.framework.network;

import android.util.Log;

import com.guoguang.framework.network.excpetion.ApiExcpetion;

import io.reactivex.Observable;
import io.reactivex.ObservableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class RxHelper {
    /**
     * 统一线程处理
     */
    public static <T> ObservableTransformer<T, T> rxSchedulerHelper() {
        return upstream -> upstream.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 统一结果处理
     */
    public static <T> ObservableTransformer<CommonResp<T>, CommonResp<T>> handleResult() {
        return upstream -> upstream.flatMap((Function<CommonResp<T>, Observable<CommonResp<T>>>) result -> {
            Log.e("TAG", "RetCode: " + result.getRetCode() + "----" + "RetMsg: " + result.getRetMsg());
            if ("0".equals(result.getRetCode())) {
                return createData(result);
            } else {
                throw new ApiExcpetion(result.getRetMsg());
            }
        });
    }

    /**
     * 生成observable
     *
     * @param data
     * @param <T>
     * @return
     */
    private static <T> Observable<T> createData(final T data) {
        return Observable.create(emitter -> {
            try {
                emitter.onNext(data);
                emitter.onComplete();
            } catch (Exception error) {
                emitter.onError(error);
            }
        });
    }
}
