package com.xiaoniu.asmhelp.bean;

public class ASStepBean {

    public int delay_time;
    public String find_text;
    public String action_type;
    public String click_type;
    public boolean findId;
    public boolean banScrollable;
    public String reality_node_name;
    public String reality_node_id;
    public String reality_scrollable_node_id;
    public boolean checkedRemoveNext;

    public int getDelay_time() {
        return delay_time;
    }

    public void setDelay_time(int delay_time) {
        this.delay_time = delay_time;
    }

    public String getFind_text() {
        return find_text;
    }

    public void setFind_text(String find_text) {
        this.find_text = find_text;
    }

    public String getAction_type() {
        return action_type;
    }

    public void setAction_type(String action_type) {
        this.action_type = action_type;
    }

    public String getClick_type() {
        return click_type;
    }

    public void setClick_type(String click_type) {
        this.click_type = click_type;
    }

    public String getReality_node_name() {
        return reality_node_name;
    }

    public void setReality_node_name(String reality_node_name) {
        this.reality_node_name = reality_node_name;
    }

    public String getReality_node_id() {
        return reality_node_id;
    }

    public void setReality_node_id(String reality_node_id) {
        this.reality_node_id = reality_node_id;
    }

    public boolean isFindId() {
        return findId;
    }

    public void setFindId(boolean findId) {
        this.findId = findId;
    }

    public String getReality_scrollable_node_id() {
        return reality_scrollable_node_id;
    }

    public void setReality_scrollable_node_id(String reality_scrollable_node_id) {
        this.reality_scrollable_node_id = reality_scrollable_node_id;
    }

    public boolean isBanScrollable() {
        return banScrollable;
    }

    public void setBanScrollable(boolean banScrollable) {
        this.banScrollable = banScrollable;
    }

    public boolean isCheckedRemoveNext() {
        return checkedRemoveNext;
    }

    public void setCheckedRemoveNext(boolean checkedRemoveNext) {
        this.checkedRemoveNext = checkedRemoveNext;
    }
}