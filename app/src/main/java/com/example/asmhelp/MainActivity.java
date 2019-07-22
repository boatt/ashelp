package com.example.asmhelp;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.xiaoniu.asmhelp.bean.ASBase;
import com.xiaoniu.asmhelp.bean.ASIntentBean;
import com.xiaoniu.asmhelp.bean.ASStepBean;
import com.xiaoniu.asmhelp.server.AccessibilityServiceMonitor;
import com.xiaoniu.asmhelp.util.ASData;
import com.xiaoniu.asmhelp.util.ASMAutoUtils;
import com.xiaoniu.asmhelp.util.ASMListener;
import com.xiaoniu.asmhelp.util.AccessibilitUtil;
import com.xiaoniu.asmhelp.util.SystemHelper;
import com.xiaoniu.asmhelp.util.SystemProperties;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements ASMListener {
    private static final String TAG = "MainActivity";
    private List<ASBase> asBases;
    private static final int SDK_INT = Build.VERSION.SDK_INT;
    private static String MODEL = android.os.Build.MODEL;
    private static final String KEY_FLYME_ID_FALG_KEY = "ro.build.display.id";
    String MANUFACTURER = Build.MANUFACTURER.toLowerCase();//硬件制造商

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ASMAutoUtils.getInstance().setListener(this);
        String version = SystemProperties.getRom("ro.build.version.opporom", "unkonw");
//        String version = SystemProperties.getRom("ro.vivo.rom.version", "unkonw");
//        String version = SystemProperties.getRom("ro.smartisan.version", "unkonw");
//        String version = SystemProperties.getRom("ro.build.version.emui", "unkonw");
//        String version = SystemProperties.getRom("ro.letv.release.version", "unkonw");

//        String version = SystemProperties.getRom(KEY_FLYME_ID_FALG_KEY, "unkonw");
//        String version = SystemProperties.getRom("ro.build.version.samsung", "unkonw");
//        String version = SystemProperties.getMiuiVersion();
//        String version = SystemProperties.getRom("ro.build.uiversion", "unkonw");
//        String version = SystemProperties.getRom("ro.modversion", "unkonw");

        TextView text = findViewById(R.id.content);
         getSystemInfo();

        Log.d(TAG, "requestPermission: MODEL:  " + MODEL + "  SDK_INT:   " + SDK_INT + "  MANUFACTURER:  " + MANUFACTURER + "  vivoApplistPermissionStatus  " + version);


    }
    private static String m9459c() {
        String str = "/proc/version";
//        if ( Build.VERSION.SDK_INT > 24) {
//            return "";
//        }
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(str), 8192);
            String[] split = bufferedReader.readLine().split("\\s+");
            if (split != null) {
                if (split.length >= 6) {
                    StringBuilder sb = new StringBuilder();
                    int length = split.length - 1;
                    while (length >= split.length - 6 && length >= 0) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append(split[length]);
                        sb2.append(" ");
                        sb.insert(0, sb2.toString());
                        length--;
                    }
                    bufferedReader.close();
                    String trim = sb.toString().trim();
                    new SimpleDateFormat("EEE MMM d HH:mm:ss z yyyy", Locale.US).parse(trim);
                    return trim;
                }
            }
            return "";
        } catch (Exception unused) {
            return "";
        }
    }


    private void getSystemInfo() {
        String board = Build.BOARD;//主板
        String brand = Build.BRAND;//Android系统定制商
        String id = Build.ID;//修订版本列表
        String manufacturer = Build.MANUFACTURER;//硬件制造商
        String model = Build.MODEL;//版本
        String hardware = Build.HARDWARE;//硬件名
        String tags = Build.TAGS;//描述Build的标签
        String type = Build.TYPE;//Builder类型
        String codename = Build.VERSION.CODENAME;//当前开发代码
        String incremental = Build.VERSION.INCREMENTAL;//源码控制版本号
        String BASE_OS = Build.VERSION.BASE_OS;//源码控制版本号
        String SECURITY_PATCH = Build.VERSION.SECURITY_PATCH;//源码控制版本号
        String release = Build.VERSION.RELEASE;//版本字符串
        int sdkInt = Build.VERSION.SDK_INT;//版本号
        String property = System.getProperty("os.version");
        String name = System.getProperty("os.name");
        String arch = System.getProperty("os.arch");
        Log.d(TAG, "getSystemInfo: ");
    }

    /**
     * 打开无障碍功能
     *
     * @param view
     */
    public void openAccessibilityService(View view) {
        if (AccessibilitUtil.isAccessibilitySettingsOn(this, AccessibilityServiceMonitor.class.getCanonicalName())) {
            Toast.makeText(this, "已打开无障碍功能", Toast.LENGTH_SHORT).show();
        } else {
            AccessibilitUtil.showSettingsUI(this);
        }
    }

    public void closeAccessibilityService(View view) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            AccessibilityServiceMonitor.getInstance().disableSelf();
        }
    }

    public void requestPermission(View view) {


        String data = ASData.getData();
        asBases = JSON.parseArray(data, ASBase.class);
        ASMAutoUtils.getInstance().start(asBases.remove(0));

//        ASBase asBases = JSON.parseObject(data, ASBase.class);
//        ASMAutoUtils.getInstance().start(asBases);
//        permissionApplyInternal();
//        permissionApplyInternal();
//        ASIntentBean intentData = new ASIntentBean();
//        intentData.setActionName("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
//        jumpActivity(intentData);
//        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        intent.setData(Uri.parse("package:" + getPackageName()));
//        startActivityForResult(intent, 1);
    }

    public void permissionApplyInternal() {
        try {
            Class clazz = Settings.class;
            Field field = clazz.getDeclaredField("ACTION_MANAGE_OVERLAY_PERMISSION");

            Intent intent = new Intent(field.get(null).toString());
            intent.setData(Uri.parse("package:com.cheetah.cmshow"));
            startActivity(intent);
        } catch (Exception e) {
        }
    }

    @Override
    public void complete(ASBase arg1) {
        if (asBases != null && asBases.size() > 0) {
            ASMAutoUtils.getInstance().start(asBases.remove(0));
        }
    }

    @Override
    public void pause(ASStepBean arg1) {
        String clickFailToast = arg1.getClickFailToast();
        if (!TextUtils.isEmpty(clickFailToast)) {
            Toast.makeText(this, clickFailToast, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void jumpActivity(ASIntentBean intentData) {
        Intent intent = new Intent();
        if (!TextUtils.isEmpty(intentData.getUriData())) {
            String[] uriArr = intentData.getUriData().split("&");
            Uri uri = Uri.fromParts(uriArr[0], uriArr[1], null);
            intent.setData(uri);
        }
        if (!TextUtils.isEmpty(intentData.getUriDataFull())) {
            String uriStr = intentData.getUriDataFull();
            Uri uri = Uri.parse(uriStr);
            intent.setData(uri);
        }
        if (!TextUtils.isEmpty(intentData.getExtra())) {
            String[] uriArr = intentData.getExtra().split("&");
            intent.putExtra(uriArr[0], uriArr[1]);
        }
        if (!TextUtils.isEmpty(intentData.getActionName())) {
            intent.setAction(intentData.getActionName());
        }
        if (!TextUtils.isEmpty(intentData.getComponenPkg()) && !TextUtils.isEmpty(intentData.getComponenCls())) {
            ComponentName comp = new ComponentName(intentData.getComponenPkg(), intentData.getComponenCls());
            intent.setComponent(comp);
        }
        if (!TextUtils.isEmpty(intentData.getCategory())) {
            intent.addCategory(intentData.getCategory());
        }
        try {
            startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void testJump1(View view) {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        intent.setData(Uri.parse("package:com.cheetah.cmshow"));
        intent.setData(Uri.parse("package:com.xiaoniu.zuilaidian"));
        startActivityForResult(intent, 1);
//        ASIntentBean intentData = new ASIntentBean();
//        intentData.setUriData("package&com.xiaoniu.zuilaidian");
//        intentData.setActionName("android.settings.APPLICATION_DETAILS_SETTINGS");
//        jumpActivity(intentData);

//        Intent intent = new Intent("android.settings.ACTION_NOTIFICATION_LISTENER_SETTINGS");
////        intent.setData(Uri.parse("package:com.cheetah.cmshow"));
////        intent.setData(Uri.parse("package:com.xiaoniu.zuilaidian"));
//        startActivityForResult(intent, 1);
    }

    public void testJump2(View view) {
//        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
////        intent.setData(Uri.parse("package:com.cheetah.cmshow"));
//        intent.setData(Uri.parse("package:com.xiaoniu.zuilaidian"));
//        startActivityForResult(intent, 1);

        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
//        intent.setData(Uri.parse("package:com.cheetah.cmshow"));
        startActivityForResult(intent, 1);

    }
}
