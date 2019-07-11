package com.xiaoniu.asmhelp.server;

import android.accessibilityservice.AccessibilityService;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;

/**
 * Desc:
 * <p>
 *
 * @author: ZhouTao
 * Date: 2019/6/28
 * Copyright: Copyright (c) 2016-2020
 * Company: @小牛科技
 * Update
 */
public class AccessibilityServiceMonitor extends AccessibilityService {
    private static final String TAG = "AccessibilityServiceMon";
    private static AccessibilityServiceMonitor mAccessibilityServiceMonitor;

    public static AccessibilityServiceMonitor getInstance() {
        if (mAccessibilityServiceMonitor == null) {
            throw new NullPointerException("AblService辅助服务未开启");
        }
        return mAccessibilityServiceMonitor;
    }


    @Override
    public void onCreate() {
        mAccessibilityServiceMonitor = this;
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        int typeWindowStateChanged = AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        Log.d(TAG, "onAccessibilityEvent: " + eventType + "typeWindowStateChanged:" + typeWindowStateChanged);

    }


    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        //服务开启时，调用
        //setServiceInfo();这个方法同样可以实现xml中的配置信息
        //可以做一些开启后的操作比如点两下返回
        Log.d(TAG, "onServiceConnected: ");
        mAccessibilityServiceMonitor = this;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.d(TAG, "onUnbind: ");
        //关闭服务时,调用
        //如果有资源记得释放
        return super.onUnbind(intent);
    }

    @Override
    public void onInterrupt() {
        Log.d(TAG, "onInterrupt: ");
    }

}
