package com.guoguang.update;

import java.util.List;

/**
 * Author: Created by jereme on 2018/9/12
 * E-main: liuqx@guoguang.com.cn
 */
public class UpdateDetail {
    /**
     * latestVersion : 1.0.9
     * latestVersionCode : 10
     * url : https://www.pgyer.com/xUg9
     * releaseNotes : ["- 更新证书"]
     */

    private String latestVersion;
    private int latestVersionCode;
    private String url;
    private List<String> releaseNotes;

    public String getLatestVersion() {
        return latestVersion;
    }

    public void setLatestVersion(String latestVersion) {
        this.latestVersion = latestVersion;
    }

    public int getLatestVersionCode() {
        return latestVersionCode;
    }

    public void setLatestVersionCode(int latestVersionCode) {
        this.latestVersionCode = latestVersionCode;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getReleaseNotes() {
        return releaseNotes;
    }

    public void setReleaseNotes(List<String> releaseNotes) {
        this.releaseNotes = releaseNotes;
    }
}
