package com.guoguang.framework.network.excpetion;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class ApiExcpetion extends RuntimeException {
    public ApiExcpetion(String msg) {
    super(msg);
  }
}
