package com.xiaoniu.asmhelp.util;

import com.xiaoniu.asmhelp.bean.ASBase;
import com.xiaoniu.asmhelp.bean.ASIntentBean;
import com.xiaoniu.asmhelp.bean.ASStepBean;

public interface ASMListener {
    void complete(ASBase arg1);

    /**
     * 暂停
     *
     * @param arg1
     */
    void pause(ASStepBean arg1);

    void jumpActivity(ASIntentBean intent);
}