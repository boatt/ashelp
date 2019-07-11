package com.xiaoniu.asmhelp.bean;

public class ASIntentBean {

    private String packageName;
    private String activityName;
    private String actionName;
    private String uriData;
    private String componenPkg;
    private String componenCls;
    private String extra;
    private String category;
    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public String getUriData() {
        return uriData;
    }

    public void setUriData(String uriData) {
        this.uriData = uriData;
    }

    public String getComponenPkg() {
        return componenPkg;
    }

    public void setComponenPkg(String componenPkg) {
        this.componenPkg = componenPkg;
    }

    public String getComponenCls() {
        return componenCls;
    }

    public void setComponenCls(String componenCls) {
        this.componenCls = componenCls;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
}