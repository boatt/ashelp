package com.xiaoniu.asmhelp.util;

import android.content.Intent;
import android.os.Build;

/**
 * Desc:
 * <p>
 *
 * @author: ZhouTao
 * Date: 2019/7/2
 * Copyright: Copyright (c) 2016-2020
 * Company: @小牛科技
 * Update
 */
public class PhoneRomUtils {
    private static final String MANUFACTURER = Build.MANUFACTURER.toLowerCase();
    public static int getPhoneType() {
        Intent intent;
        if (MANUFACTURER.contains("huawei")) {
            return 1;
        } else if (MANUFACTURER.contains("xiaomi")) {
            return 2;
        } else if (MANUFACTURER.contains("oppo")) {
            return 3;
        } else if (MANUFACTURER.contains("vivo")) {
            return 4;
        } else if (MANUFACTURER.contains("meizu")) {
            return 6;
        } else {
            return 7;
        }
//        return 0;
    }
}
