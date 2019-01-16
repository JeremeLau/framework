package com.jeremelau.libapplication.presenter.contract;


import com.guoguang.framework.mvp.BasePresenter;
import com.guoguang.framework.mvp.BaseView;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public interface TestContract {
    interface View extends BaseView {
        void lock();
        void unlock();
    }

    interface Presenter extends BasePresenter<View> {
        void test(String test1, String test2);
    }
}
