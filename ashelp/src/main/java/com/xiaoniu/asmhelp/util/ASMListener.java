package com.xiaoniu.asmhelp.util;

import com.xiaoniu.asmhelp.bean.ASBase;
import com.xiaoniu.asmhelp.bean.ASIntentBean;

public interface ASMListener {
    void complete(ASBase arg1);

    void jumpActivity(ASIntentBean intent);
}