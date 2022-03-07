package com.yunpan.achengovo.entity;

public class AutoLogin {
    private Integer autoLoginId;
    private String autoLoginCode;
    private Integer userId;
    private String autLoginLife;

    @Override
    public String toString() {
        return "AutoLogin{" +
                "autoLoginId=" + autoLoginId +
                ", autoLoginCode='" + autoLoginCode + '\'' +
                ", userId=" + userId +
                ", autLoginLife='" + autLoginLife + '\'' +
                '}';
    }

    public Integer getAutoLoginId() {
        return autoLoginId;
    }

    public void setAutoLoginId(Integer autoLoginId) {
        this.autoLoginId = autoLoginId;
    }

    public String getAutoLoginCode() {
        return autoLoginCode;
    }

    public void setAutoLoginCode(String autoLoginCode) {
        this.autoLoginCode = autoLoginCode;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getAutLoginLife() {
        return autLoginLife;
    }

    public void setAutLoginLife(String autLoginLife) {
        this.autLoginLife = autLoginLife;
    }
}
