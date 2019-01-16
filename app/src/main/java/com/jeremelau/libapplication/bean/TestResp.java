package com.jeremelau.libapplication.bean;

import java.io.Serializable;
import java.util.List;

/**
 * Author: Created by jereme on 2019/1/16
 * E-main: liuqx@guoguang.com.cn
 */
public class TestResp implements Serializable {

    public String gid;
    public String gname;
    public List<Apps> apps;

    public static class Apps implements Serializable {
        public String id;
        public String name;
    }
}
