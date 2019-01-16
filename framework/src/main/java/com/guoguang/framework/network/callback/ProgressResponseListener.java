package com.guoguang.framework.network.callback;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public interface ProgressResponseListener {
    void onResponseProgress(long bytesRead, long contentLength, boolean done);
}
