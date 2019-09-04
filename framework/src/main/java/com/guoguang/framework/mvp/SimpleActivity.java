package com.guoguang.framework.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.disposables.CompositeDisposable;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public abstract class SimpleActivity extends AppCompatActivity implements BaseView {
    protected Activity mContext;
    protected Unbinder mUnBinder;
    protected FragmentManager mFragmentManager;
    //private CustomDialog mDialogWaiting;
    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mContext = this;
        mUnBinder = ButterKnife.bind(this);
//        MainApp.getInstance().addActivity(this);
        mFragmentManager = getSupportFragmentManager();
        initEventAndData();
    }

    protected void setToolBar(Toolbar toolBar, String title) {
        toolBar.setTitle(title);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //添加返回按钮
            getSupportActionBar().setDisplayShowHomeEnabled(true);   //是否显示左上角的图标
        }
        toolBar.setNavigationOnClickListener(v -> onBackPressed());
    }

    protected FragmentTransaction fragmentReplace(int layoutID, Fragment fragment) {
        return mFragmentManager.beginTransaction().replace(layoutID, fragment);
    }

    protected FragmentTransaction fragmentAdd(int layoutID, Fragment fragment) {
        return mFragmentManager.beginTransaction().add(layoutID, fragment);
    }

    /**
     * 页面跳转
     */
    protected void jumpToActivity(Intent intent) {
        startActivity(intent);
    }

    protected void jumpToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable.size() > 0) {
            mCompositeDisposable.dispose();
        }
//        MainApp.getInstance().removeActivity(this);
        mUnBinder.unbind();
    }

    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    protected abstract int getLayout();

    protected abstract void initEventAndData();
}
