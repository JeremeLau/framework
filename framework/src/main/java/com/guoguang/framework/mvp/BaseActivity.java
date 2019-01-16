package com.guoguang.framework.mvp;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.CallSuper;
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
public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView {
    protected T mPresenter;
    protected Activity mContext;
    protected FragmentManager mFragmentManager;
    private Unbinder mUnbinder;

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());
        mFragmentManager = getSupportFragmentManager();
        /**
         * 实例化ButterKnife
         */
        mUnbinder = ButterKnife.bind(this);
        /**
         * 上下文
         */
        mContext = this;
        /**
         * 初始化Presenter
         */
        mPresenter = initPresenter();
        /**
         * Presenter 与 Activity 建立订阅关系 让Presenter  持有Activity引用
         */
        if (mPresenter != null) {
            mPresenter.attachView(this);
        }
        /**
         * 将当前Activity 添加到集合中
         */
//        MainApp.getInstance().addActivity(this);

        instantiateFragments(savedInstanceState);

        /**
         * 调用初始化数据的方法
         */
        initEventAndData();
    }
    @CallSuper
    protected void instantiateFragments(Bundle savedInstanceState){

    }

    /**
     * 设置ToolBar的方法
     *
     * @param toolbar
     * @param title
     */
    protected void setToolBar(Toolbar toolbar, String title) {
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);   //添加返回按钮
            getSupportActionBar().setDisplayShowHomeEnabled(true);   //是否显示左上角的图标
        }
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    /**
     * toast提示
     */
    public void showToast(String str) {
        Toast.makeText(this, str, Toast.LENGTH_SHORT).show();
    }

    /**
     * Fragment  事务管理
     *
     * @param layoutID
     * @param fragment
     * @return
     */
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
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }

    protected void jumpToActivity(Class activity) {
        Intent intent = new Intent(this, activity);
        startActivity(intent);
//        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCompositeDisposable.size() > 0) {
            mCompositeDisposable.dispose();
        }
        /**
         * 将Presenter 持有的Activity引用 置空  解除订阅关系
         */
        if (mPresenter != null) {
            mPresenter.detachView();
        }
        /**
         * ButterKnife
         */
        mUnbinder.unbind();
        /**
         * 将当前Activity 从集合中删除掉
         */
//        MainApp.getInstance().removeActivity(this);
    }

    protected abstract void initEventAndData();

    protected abstract T initPresenter();

    protected abstract int getLayout();
}
