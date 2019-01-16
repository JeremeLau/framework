package com.jeremelau.libapplication.api;

import com.guoguang.framework.network.CommonResp;
import com.jeremelau.libapplication.bean.TestResp;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public interface TestApi {

    @GET("microapp/apps")
    Observable<CommonResp<List<TestResp>>> test(@Query("test1") String test1,
                                                @Query("test2") String test2);

}
