package com.guoguang.framework.mvp;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public interface BasePresenter<T extends BaseView> {
    /**
     * 订阅
     */
    void attachView(T view);

    /**
     * 注销
     */
    void detachView();
}
