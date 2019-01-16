package com.jeremelau.libapplication;

import android.widget.Toast;

import com.guoguang.framework.mvp.BaseActivity;
import com.jeremelau.libapplication.event.TestEvent;
import com.jeremelau.libapplication.presenter.TestPresenter;
import com.jeremelau.libapplication.presenter.contract.TestContract;

public class MainActivity extends BaseActivity<TestPresenter> implements TestContract.View {

    @Override
    protected void initEventAndData() {
        initEvent();
        initDate();
    }

    @Override
    protected TestPresenter initPresenter() {
        return new TestPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void lock() {

    }

    @Override
    public void unlock() {

    }

    @Override
    public void showError(String msg) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
    }

    private void initEvent() {
        mCompositeDisposable.add(TestEvent.observable.subscribe(testEvent -> {
            if (testEvent.type == TestEvent.TYPE_TEST_SUCCESS) {

            } else {
                showError("testError");
            }
        }));
    }

    private void initDate() {
        mPresenter.test("test1", "test2");
    }
}
