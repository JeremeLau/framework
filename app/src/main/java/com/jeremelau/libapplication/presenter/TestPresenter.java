package com.jeremelau.libapplication.presenter;

import com.guoguang.framework.mvp.RxPresenter;
import com.guoguang.framework.network.CommonResp;
import com.guoguang.framework.network.OnNetRequestListener;
import com.guoguang.rxbus.RxBus;
import com.jeremelau.libapplication.application.MainApp;
import com.jeremelau.libapplication.bean.TestResp;
import com.jeremelau.libapplication.event.TestEvent;
import com.jeremelau.libapplication.net.TestRequest;
import com.jeremelau.libapplication.presenter.contract.TestContract;

import java.util.List;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class TestPresenter extends RxPresenter<TestContract.View> implements TestContract.Presenter {
    private TestRequest testRequest = new TestRequest(MainApp.getInstance(), this);

    @Override
    public void test(String test1, String test2) {
        testRequest.testRequest(test1, test2, new OnNetRequestListener<CommonResp<List<TestResp>>>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onFinish() {

            }

            @Override
            public void onSuccess(CommonResp<List<TestResp>> data) {
                RxBus.getInstance().post(TestEvent.class, new TestEvent(TestEvent.TYPE_TEST_SUCCESS, data.getData()));
            }

            @Override
            public void onFailure(Throwable t) {
                RxBus.getInstance().post(TestEvent.class, new TestEvent(TestEvent.TYPE_TEST_UNSUCCESS));
            }
        });
    }
}
