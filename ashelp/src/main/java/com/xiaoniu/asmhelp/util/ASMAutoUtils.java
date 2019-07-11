package com.xiaoniu.asmhelp.util;

import android.accessibilityservice.AccessibilityService;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.xiaoniu.asmhelp.bean.ASBase;
import com.xiaoniu.asmhelp.bean.ASStepBean;
import com.xiaoniu.asmhelp.server.AccessibilityServiceMonitor;

import java.util.List;

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
public class ASMAutoUtils {
    private static final String TAG = "ASMAutoUtils";
    static ASMAutoUtils mASMAutoUtils;
    ASMListener mASMListener;
    ASBase mBaseData;
    private final DelayedHandler mHandler;
    public static final int WHAT_JUMP = 1;
    public static final int WHAT_FIND = 2;
    public static final int WHAT_COMPLETE = 3;
    final int TOTAL_COUNT = 800;
    int COUNT = 0;
    boolean isFind = false;
    boolean isScrollableComplete = false;

    public static ASMAutoUtils getInstance() {
        if (mASMAutoUtils == null) {
            mASMAutoUtils = new ASMAutoUtils();
        }
        return mASMAutoUtils;
    }

    public ASMAutoUtils() {
        mHandler = new DelayedHandler();
    }

    public void setListener(ASMListener mASMListener) {
        this.mASMListener = mASMListener;
    }

    public void start(ASBase data) {
        Log.d(TAG, "start: "+data.describe);
        mBaseData = data;
        mHandler.removeMessages(WHAT_JUMP);
        mHandler.sendEmptyMessageDelayed(WHAT_JUMP, mBaseData.delay_time);
    }

    class DelayedHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_JUMP:
                    COUNT = 0;
                    if (mASMListener != null) {
                        mASMListener.jumpActivity(mBaseData.intent);

                        mHandler.removeMessages(WHAT_FIND);
                        mHandler.sendEmptyMessageDelayed(WHAT_FIND, mBaseData.delay_time);
                    }
                    break;
                case WHAT_FIND:
                    isFind = false;
                    Log.d(TAG, "handleMessage: getPackageName" + AccessibilityServiceMonitor.getInstance().getPackageName());
                    List<ASStepBean> step = mBaseData.step;
                    if (step.size() > 0) {
                        ASStepBean stepBean = step.get(0);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                            if (stepBean.getClick_type().equals("system")) {
                                AccessibilityServiceMonitor.getInstance().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                                step.remove(stepBean);
                                mHandler.removeMessages(WHAT_FIND);
                                mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time);
                            } else {
                                AccessibilityNodeInfo rootInActiveWindow = AccessibilityServiceMonitor.getInstance().getRootInActiveWindow();
                                findClickView(rootInActiveWindow, stepBean);
                                if (isFind) {
                                    step.remove(stepBean);
                                } else {
                                    isScrollableComplete = false;
                                    findScrollableView(rootInActiveWindow, stepBean);
                                }
                            }
                        }
                    } else {
                        mHandler.removeMessages(WHAT_COMPLETE);
                        mHandler.sendEmptyMessageDelayed(WHAT_COMPLETE, mBaseData.delay_time);
                    }
                    break;
                case WHAT_COMPLETE:
                    if (mASMListener != null) {
                        mASMListener.complete(mBaseData);
                    }
                    break;
            }
        }
    }

    /**
     * 遍历查找节点
     */
    private void findClickView(AccessibilityNodeInfo rootNode, ASStepBean stepBean) {
        String findText = stepBean.getFind_text();
        COUNT++;
        if (rootNode != null) {
            //找到一级节点
            for (int i = rootNode.getChildCount() - 1; i >= 0; i--) {
                AccessibilityNodeInfo node = rootNode.getChild(i);
                if (isFind) {
                    return;
                }
                //如果node为空则跳过该节点
                if (node == null) {
                    continue;
                }
                CharSequence text = node.getText();
                if (text != null && text.toString().contains(findText)) {
                    AccessibilityNodeInfo parent = node.getParent();
                    if (stepBean.getClick_type().equals("parent")) {
                        while (parent != null) {
                            if (parent.isClickable() || (node.isCheckable() && !node.isChecked())) {
                                //模拟点击
                                isFind = true;
                                parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                mHandler.removeMessages(WHAT_FIND);
                                mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time);
                                return;
                            }
                            parent = parent.getParent();
                        }
                    } else if (stepBean.getClick_type().equals("child")) {
                        getAllChild(parent, stepBean);
                    } else if (stepBean.getClick_type().equals("self")) {
                        if (node.isClickable() || (node.isCheckable() && !node.isChecked())) {
                            //模拟点击
                            isFind = true;
                            node.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                            mHandler.removeMessages(WHAT_FIND);
                            mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time);
                            return;
                        }
                    }
                }
                Log.d(TAG, "findClickView: COUNT:" + COUNT);
                //递归需要有出口，如果找500次没找到立即停止,可能由于适配问题权限压根没在这个界面
                if (COUNT >= TOTAL_COUNT) {
                    isFind = true;
                }
                if (isFind) {
                    mHandler.removeMessages(WHAT_FIND);
                    mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time);
                    return;
                } else {
                    findClickView(node, stepBean);
                }
            }
        }
    }


    private void getAllChild(AccessibilityNodeInfo parent, ASStepBean stepBean) {
        if (stepBean.isFindId()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = parent.findAccessibilityNodeInfosByViewId(stepBean.reality_node_id);
                if (accessibilityNodeInfosByViewId.size() > 0) {
                    AccessibilityNodeInfo child = accessibilityNodeInfosByViewId.get(0);
                    Log.e(TAG, "============= findSwitchView 3 " + child.getClassName() + "==" + child.getViewIdResourceName() + "==" + child.isCheckable() + "==" + child.isClickable());
                    clickNodeIsNoChecked(parent, stepBean, child);
                }
            }
        } else {
            for (int j = 0; j < parent.getChildCount(); j++) {
                AccessibilityNodeInfo child = parent.getChild(j);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    Log.e(TAG, "============= findSwitchView 4 " + child.getClassName() + "==" + child.getViewIdResourceName() + "==" + child.isCheckable() + "==" + child.isClickable());
                    if (!TextUtils.isEmpty(child.getViewIdResourceName())) {
                        if (child.getViewIdResourceName().contains(stepBean.getReality_node_id())) {
                            clickNodeIsNoChecked(parent, stepBean, child);
                        }
                    }

                }
            }
        }
    }

    /**
     * 点击一个未选中的节点
     *
     * @param parent
     * @param stepBean
     * @param child
     */
    private void clickNodeIsNoChecked(AccessibilityNodeInfo parent, ASStepBean stepBean, AccessibilityNodeInfo child) {
        boolean checked = child.isChecked();
        if (!checked) {
            boolean flag = child.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            if (!flag) {
                child.performAction(AccessibilityNodeInfo.ACTION_SELECT);
                //补偿点击
                if (!child.isChecked()) {
                    boolean flagParent = parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                }
            }
        } else {
            //兼容小米,移除下一步
            if (stepBean.isCheckedRemoveNext()) {
                mBaseData.step.remove(mBaseData.step.indexOf(stepBean) + 1);
            }
        }
        isFind = true;
        mHandler.removeMessages(WHAT_FIND);
        mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time);
    }

    /**
     * 遍历查找节点
     */
    private void findScrollableView(AccessibilityNodeInfo rootNode, ASStepBean stepBean) {
        Log.d(TAG, "findScrollableView: ==================");
        if (stepBean.isBanScrollable()) {
            return;
        }
        if (rootNode != null) {
            //如果指定了滚动控件则滚动该控件
            if (!TextUtils.isEmpty(stepBean.getReality_scrollable_node_id())) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                    List<AccessibilityNodeInfo> accessibilityNodeInfosByViewId = rootNode.findAccessibilityNodeInfosByViewId(stepBean.reality_scrollable_node_id);
                    if (accessibilityNodeInfosByViewId.size() > 0) {
                        AccessibilityNodeInfo child = accessibilityNodeInfosByViewId.get(0);
                        if (child != null && child.isScrollable()) {
                            //模拟点击
                            child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            child.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                            isScrollableComplete = true;
                            mHandler.removeMessages(WHAT_FIND);
                            mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time / 2);
                        }
                    }
                }
            } else {
                //从最后一行开始找起
                for (int i = rootNode.getChildCount() - 1; i >= 0; i--) {
                    AccessibilityNodeInfo node = rootNode.getChild(i);
                    //如果node为空则跳过该节点
                    if (node == null) {
                        continue;
                    }
                    Log.d(TAG, "findScrollableView: " + node.getText());
                    if (node.isScrollable()) {
                        //模拟点击
                        node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        node.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                        isScrollableComplete = true;
                        mHandler.removeMessages(WHAT_FIND);
                        mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time / 2);

                    } else {
                        AccessibilityNodeInfo parent = node.getParent();
                        while (parent != null) {
                            if (parent.isScrollable()) {
                                //模拟点击
                                parent.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                parent.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
                                isScrollableComplete = true;
                                mHandler.removeMessages(WHAT_FIND);
                                mHandler.sendEmptyMessageDelayed(WHAT_FIND, stepBean.delay_time / 2);
                                break;
                            }
                            parent = parent.getParent();
                        }
                    }
                    if (isScrollableComplete) {
                        break;
                    } else {
                        findScrollableView(node, stepBean);
                    }
                }
            }

        }
    }
}
