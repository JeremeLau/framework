package com.guoguang.framework.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.trello.rxlifecycle3.components.support.RxFragment;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public abstract class SimpleFragment extends RxFragment implements BaseView {

    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    protected Unbinder mUnBinder;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    @Override
    public void onAttach(Activity activity) {
        mActivity = activity;
        mContext = activity;
        super.onAttach(activity);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(getLayoutId(), null);
        return mView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mUnBinder = ButterKnife.bind(this, view);
        initEventAndData();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable.size() > 0) {
            mCompositeDisposable.dispose();
        }
        mUnBinder.unbind();
    }
    /**
     * 页面跳转
     */
    protected void jumpToActivity(Intent intent) {
        startActivity(intent);
        mActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    protected void jumpToActivity(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
        mActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    public void showToast(String msg) {
        Toast.makeText(this.getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    protected abstract void initEventAndData();

    protected abstract int getLayoutId();
}
