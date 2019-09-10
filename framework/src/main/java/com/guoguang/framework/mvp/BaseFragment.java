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
public abstract class BaseFragment<T extends BasePresenter> extends RxFragment implements BaseView {
    protected T mPresenter;
    protected View mView;
    protected Activity mActivity;
    protected Context mContext;
    private Unbinder mUnbinder;

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
        mPresenter = initPresenter();

        return mView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        mUnbinder = ButterKnife.bind(this, view);
        initEventAndData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable.size() > 0) {
            mCompositeDisposable.dispose();
        }

        if (mPresenter != null) {
            mPresenter.detachView();
        }
    }

    /**
     * toast提示
     */
    public void showToast(String str) {
      Toast.makeText(mContext, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * 页面跳转
     */
    protected void jumpToActivity(Intent intent) {
      startActivity(intent);
  //   mActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    protected void jumpToActivity(Class activity) {
      Intent intent = new Intent(mContext, activity);
      startActivity(intent);
  //    mActivity.overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }
    protected abstract void initEventAndData();

    protected abstract T initPresenter();

    protected abstract int getLayoutId();
}
