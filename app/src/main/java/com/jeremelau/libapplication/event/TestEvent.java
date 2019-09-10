package com.jeremelau.libapplication.event;

import com.guoguang.rxbus.RxBus;
import com.jeremelau.libapplication.bean.TestResp;

import java.util.List;

import io.reactivex.Observable;

/**
 * Author: Created by jereme on 2018/8/3
 * E-main: liuqx@guoguang.com.cn
 */
public class TestEvent {
    public final static Observable<TestEvent> observable = RxBus.getInstance().register(TestEvent.class, TestEvent.class);

    public final static int TYPE_TEST_SUCCESS = 0;
    public final static int TYPE_TEST_UNSUCCESS = 1;

    public int type;
    public List<TestResp> testRespList;

    public TestEvent(int type) {
        this.type = type;
    }

    public TestEvent(int type, List<TestResp> testRespList) {
        this.type = type;
        this.testRespList = testRespList;
    }
}
