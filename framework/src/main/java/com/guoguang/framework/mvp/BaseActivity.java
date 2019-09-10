package com.guoguang.framework.mvp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import androidx.annotation.CallSuper;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.githang.statusbar.StatusBarCompat;
import com.guoguang.framework.R;
import com.guoguang.rxbus.RxBus;
import com.trello.rxlifecycle3.components.support.RxAppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import mehdi.sakout.dynamicbox.DynamicBox;
import timber.log.Timber;


/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public abstract class BaseActivity<T extends BasePresenter> extends RxAppCompatActivity implements BaseView {
    protected T mPresenter;
    protected Activity mContext;
    protected FragmentManager mFragmentManager;
    private Unbinder mUnbinder;

    protected CompositeDisposable mCompositeDisposable = new CompositeDisposable();

    public final static String DYNAMIC_BOX_AV_BALLPULSE = "_BallPulse";
    public final static String DYNAMIC_BOX_AV_BALLGRIDPULSE = "_BallGridPulse";
    public final static String DYNAMIC_BOX_AV_BALLSPINFADELOADER = "_BallSpinFadeLoader";
    public final static String DYNAMIC_BOX_AV_LINESCALEPARTY = "_LineScaleParty";
    public final static String DYNAMIC_BOX_AV_PACMAN = "_Pacman";

    public final static String DYNAMIC_BOX_LT_BIKING_IS_COOL = "_BikingIsCool";
    public final static String DYNAMIC_BOX_LT_BIKING_IS_HARD = "_BikingIsHard";
    public final static String DYNAMIC_BOX_LT_LOADING_ANIMATION = "_LoadingAnimation";

    public final static String PENDING_TRANSITION_LEFT_RIGHT = "_left_right";
    public final static String SWITCHER_TRANSITION_LEFT_RIGHT = "_left_right";

    private final static Observable<DynamicBoxEvent> dynamicBoxObservable = RxBus.getInstance().register(DynamicBoxEvent.class, DynamicBoxEvent.class);

    private List<DynamicBox> dynamicBoxes = new ArrayList<>();

    private String pendingTransition = PENDING_TRANSITION_LEFT_RIGHT;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayout());

        dynamicBoxObservable.compose(bindToLifecycle()).observeOn(AndroidSchedulers.mainThread()).subscribe(dynamicBoxEvent -> {
            if (!dynamicBoxes.isEmpty()) {
                if (dynamicBoxEvent.context != null && !dynamicBoxEvent.context.equals(this)) {
                    return;
                }
                for (DynamicBox dynamicBox : dynamicBoxes) {
                    if (dynamicBoxEvent.args != null) {
                        if (dynamicBoxEvent.args.length > 1 || dynamicBoxEvent.type != DynamicBoxEvent.TYPE_CUSTOM_VIEW) {
                            if (!Arrays.asList(dynamicBoxEvent.args).contains(dynamicBox)) {
                                continue;
                            }
                        }
                    }
                    switch (dynamicBoxEvent.type) {
                        case DynamicBoxEvent.TYPE_LOADING_LAYOUT:
                            dynamicBox.showLoadingLayout();
                            break;
                        case DynamicBoxEvent.TYPE_INTERNET_OFF_LAYOUT:
                            dynamicBox.showInternetOffLayout();
                            break;
                        case DynamicBoxEvent.TYPE_EXCEPTION_LAYOUT:
                            dynamicBox.showExceptionLayout();
                            break;
                        case DynamicBoxEvent.TYPE_CUSTOM_VIEW:
                            dynamicBox.showCustomView((String) dynamicBoxEvent.args[0]);
                            break;
                        case DynamicBoxEvent.TYPE_HIDE_ALL:
                            dynamicBox.hideAll();
                            break;
                    }
                }
            }
        }, throwable -> Timber.e(throwable, "dynamicBoxDisposable error"));

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

    @Override
    public void startActivity(Intent intent) {
        super.startActivity(intent);
        if (!TextUtils.isEmpty(pendingTransition)) {
            if (pendingTransition.equals(PENDING_TRANSITION_LEFT_RIGHT)) {
                overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
            }
        }
    }

    @Override
    public void finish() {
        super.finish();
        if (!TextUtils.isEmpty(pendingTransition)) {
            if (pendingTransition.equals(PENDING_TRANSITION_LEFT_RIGHT)) {
                overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
            }
        }
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

    protected void setStatusBarColor(int resource) {
        StatusBarCompat.setStatusBarColor(BaseActivity.this, getResources().getColor(resource));
    }

    protected void setStatusBarColor() {
        setStatusBarColor(R.color.colorPrimaryDark);
    }

    protected void translucentStatusBar() {
        StatusBarCompat.setTranslucent(getWindow(), true);
    }

    protected DynamicBox createDynamicBox(View targetView, String[] tags, String switcherTransition) {
        DynamicBox dynamicBox = createDynamicBox(this, targetView, tags, switcherTransition);
        dynamicBoxes.add(dynamicBox);
        return dynamicBox;
    }

    protected DynamicBox createDynamicBox(View targetView, String[] tags) {
        DynamicBox dynamicBox = createDynamicBox(this, targetView, tags);
        dynamicBoxes.add(dynamicBox);
        return dynamicBox;
    }

    protected DynamicBox createDynamicBox(View targetView, String switcherTransition) {
        DynamicBox dynamicBox = createDynamicBox(this, targetView, switcherTransition);
        dynamicBoxes.add(dynamicBox);
        return dynamicBox;
    }

    protected DynamicBox createDynamicBox(View targetView) {
        DynamicBox dynamicBox = createDynamicBox(this, targetView);
        dynamicBoxes.add(dynamicBox);
        return dynamicBox;
    }

    protected DynamicBox createDynamicBox() {
        return createDynamicBox(findViewById(android.R.id.content));
    }

    private static DynamicBox prepareDynamicBox(Context context, View targetView, String switcherTransition) {
        ViewGroup.LayoutParams targetViewLayoutParams = targetView.getLayoutParams();

        DynamicBox dynamicBox = new DynamicBox(context, targetView);

        if (targetView.getParent() instanceof ViewSwitcher) {
            ViewSwitcher switcher = (ViewSwitcher) targetView.getParent();
            ViewGroup.LayoutParams switcherLayoutParams = switcher.getLayoutParams();
            switcherLayoutParams.width = targetViewLayoutParams.width;
            switcherLayoutParams.height = targetViewLayoutParams.height;
            switcher.setLayoutParams(switcherLayoutParams);
            if (!TextUtils.isEmpty(switcherTransition)) {
                if (switcherTransition.equals(SWITCHER_TRANSITION_LEFT_RIGHT)) {
                    switcher.setInAnimation(context, R.anim.in_from_right);
                    switcher.setOutAnimation(context, R.anim.out_to_left);
                }
            }
        }

        return dynamicBox;
    }

    public static DynamicBox createDynamicBox(Context context, View targetView, String[] tags, String switcherTransition) {
        DynamicBox dynamicBox = prepareDynamicBox(context, targetView, switcherTransition);

        if (tags != null) {
            List tagList = Arrays.asList(tags);

            if (tagList.contains(DYNAMIC_BOX_AV_BALLPULSE)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.ballpulse_avloading, null), DYNAMIC_BOX_AV_BALLPULSE);
            }
            if (tagList.contains(DYNAMIC_BOX_AV_BALLGRIDPULSE)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.ballgridpulse_avloading, null), DYNAMIC_BOX_AV_BALLGRIDPULSE);
            }
            if (tagList.contains(DYNAMIC_BOX_AV_BALLSPINFADELOADER)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.ballspinfadeloader_avloading, null), DYNAMIC_BOX_AV_BALLSPINFADELOADER);
            }
            if (tagList.contains(DYNAMIC_BOX_AV_LINESCALEPARTY)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.linescaleparty_avloading, null), DYNAMIC_BOX_AV_LINESCALEPARTY);
            }
            if (tagList.contains(DYNAMIC_BOX_AV_PACMAN)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.pacman_avloading, null), DYNAMIC_BOX_AV_PACMAN);
            }

            if (tagList.contains(DYNAMIC_BOX_LT_BIKING_IS_COOL)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.biking_is_cool_lottie, null), DYNAMIC_BOX_LT_BIKING_IS_COOL);
            }
            if (tagList.contains(DYNAMIC_BOX_LT_BIKING_IS_HARD)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.biking_is_hard_lottie, null), DYNAMIC_BOX_LT_BIKING_IS_HARD);
            }
            if (tagList.contains(DYNAMIC_BOX_LT_LOADING_ANIMATION)) {
                dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.loading_animation_lottie, null), DYNAMIC_BOX_LT_LOADING_ANIMATION);
            }
        }

        return dynamicBox;
    }

    public static DynamicBox createDynamicBox(Context context, View targetView, String switcherTransition) {
        DynamicBox dynamicBox = prepareDynamicBox(context, targetView, switcherTransition);

        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.ballpulse_avloading, null), DYNAMIC_BOX_AV_BALLPULSE);
        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.ballgridpulse_avloading, null), DYNAMIC_BOX_AV_BALLGRIDPULSE);
        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.ballspinfadeloader_avloading, null), DYNAMIC_BOX_AV_BALLSPINFADELOADER);
        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.linescaleparty_avloading, null), DYNAMIC_BOX_AV_LINESCALEPARTY);
        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.pacman_avloading, null), DYNAMIC_BOX_AV_PACMAN);

        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.biking_is_cool_lottie, null), DYNAMIC_BOX_LT_BIKING_IS_COOL);
        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.biking_is_hard_lottie, null), DYNAMIC_BOX_LT_BIKING_IS_HARD);
        dynamicBox.addCustomView(LayoutInflater.from(context).inflate(R.layout.loading_animation_lottie, null), DYNAMIC_BOX_LT_LOADING_ANIMATION);

        return dynamicBox;
    }

    public static DynamicBox createDynamicBox(Context context, View targetView, String[] tags) {
        return createDynamicBox(context, targetView, tags, null);
    }

    public static DynamicBox createDynamicBox(Context context, View targetView) {
        return createDynamicBox(context, targetView, "");
    }

    public static void showDynamicBoxLoadingLayout(Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_LOADING_LAYOUT, context));
    }

    public static void showDynamicBoxLoadingLayout(DynamicBox dynamicBox, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_LOADING_LAYOUT, new Object[]{dynamicBox}, context));
    }

    public static void showDynamicBoxLoadingLayout(DynamicBox[] dynamicBoxes, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_LOADING_LAYOUT, dynamicBoxes, context));
    }

    public static void showDynamicBoxInternetOffLayout(Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_INTERNET_OFF_LAYOUT, context));
    }

    public static void showDynamicBoxInternetOffLayout(DynamicBox dynamicBox, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_INTERNET_OFF_LAYOUT, new Object[]{dynamicBox}, context));
    }

    public static void showDynamicBoxInternetOffLayout(DynamicBox[] dynamicBoxes, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_INTERNET_OFF_LAYOUT, dynamicBoxes, context));
    }

    public static void showDynamicBoxExceptionLayout(Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_EXCEPTION_LAYOUT, context));
    }

    public static void showDynamicBoxExceptionLayout(DynamicBox dynamicBox, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_EXCEPTION_LAYOUT, new Object[]{dynamicBox}, context));
    }

    public static void showDynamicBoxExceptionLayout(DynamicBox[] dynamicBoxes, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_EXCEPTION_LAYOUT, dynamicBoxes, context));
    }

    public static void showDynamicBoxCustomView(String tag, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_CUSTOM_VIEW, new Object[]{tag}, context));
    }

    public static void showDynamicBoxCustomView(DynamicBox dynamicBox, String tag, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_CUSTOM_VIEW, new Object[]{tag, dynamicBox}, context));
    }

    public static void showDynamicBoxCustomView(String tag, DynamicBox[] dynamicBoxes, Context context) {
        List args = new ArrayList();
        args.add(tag);
        args.addAll(Arrays.asList(dynamicBoxes));
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_CUSTOM_VIEW, args.toArray(), context));
    }

    public static void dismissDynamicBox(Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_HIDE_ALL, context));
    }

    public static void dismissDynamicBox(DynamicBox dynamicBox, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_HIDE_ALL, new Object[]{dynamicBox}, context));
    }

    public static void dismissDynamicBox(DynamicBox[] dynamicBoxes, Context context) {
        RxBus.getInstance().post(DynamicBoxEvent.class, new DynamicBoxEvent(DynamicBoxEvent.TYPE_HIDE_ALL, dynamicBoxes, context));
    }

    public void setPendingTransition(String pendingTransition) {
        this.pendingTransition = pendingTransition;
    }

    private static class DynamicBoxEvent {
        final static int TYPE_LOADING_LAYOUT = 0;
        final static int TYPE_INTERNET_OFF_LAYOUT = 1;
        final static int TYPE_EXCEPTION_LAYOUT = 2;
        final static int TYPE_CUSTOM_VIEW = 3;
        final static int TYPE_HIDE_ALL = 4;

        int type;
        Object[] args;
        Context context;

        DynamicBoxEvent(int type) {
            this.type = type;
        }

        DynamicBoxEvent(int type, Object[] args) {
            this.type = type;
            this.args = args;
        }

        DynamicBoxEvent(int type, Context context) {
            this.type = type;
            this.context = context;
        }

        DynamicBoxEvent(int type, Object[] args, Context context) {
            this.type = type;
            this.args = args;
            this.context = context;
        }
    }
}
