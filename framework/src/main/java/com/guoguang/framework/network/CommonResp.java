package com.guoguang.framework.network;

import java.io.Serializable;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class CommonResp<T> implements Serializable {
    private String retCode;
    private String retMsg;
    private T data;

    public T getData() {
      return data;
    }

    public void setData(T data) {
      this.data = data;
    }

    public String getRetCode() {
      return retCode;
    }

    public void setRetCode(String retCode) {
      this.retCode = retCode;
    }

    public String getRetMsg() {
      return retMsg;
    }

    public void setRetMsg(String retMsg) {
    this.retMsg = retMsg;
  }
}
