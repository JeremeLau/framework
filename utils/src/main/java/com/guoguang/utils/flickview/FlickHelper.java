package com.guoguang.utils.flickview;

import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;

/**
 * Author: Created by jereme on 2018/11/19
 * E-main: liuqx@guoguang.com.cn
 */
public class FlickHelper {

    private static volatile FlickHelper flickHelper;

    public static FlickHelper getFlickHelper() {
        if (flickHelper == null) {
            synchronized (FlickHelper.class) {
                if (flickHelper == null) {
                    flickHelper = new FlickHelper();
                }
            }
        }

        return flickHelper;
    }

    /**
     * 开启View闪烁效果
     *
     * */
    public void startFlick(View view, float fromAlpha, float toAlpha, long durationMill){
        if(null == view){
            return;
        }
        Animation alphaAnimation = new AlphaAnimation(fromAlpha, toAlpha);
        alphaAnimation.setDuration(durationMill);
        alphaAnimation.setInterpolator(new LinearInterpolator());
        alphaAnimation.setRepeatCount(Animation.INFINITE);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(alphaAnimation);
    }

    /**
     * 取消View闪烁效果
     *
     * */
    public void stopFlick(View view){
        if(null == view){
            return;
        }

        view.clearAnimation();
    }
}
