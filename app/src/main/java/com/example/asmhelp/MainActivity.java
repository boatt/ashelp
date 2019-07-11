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
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.xiaoniu.asmhelp.bean.ASBase;
import com.xiaoniu.asmhelp.bean.ASIntentBean;
import com.xiaoniu.asmhelp.server.AccessibilityServiceMonitor;
import com.xiaoniu.asmhelp.util.ASData;
import com.xiaoniu.asmhelp.util.ASMAutoUtils;
import com.xiaoniu.asmhelp.util.ASMListener;
import com.xiaoniu.asmhelp.util.AccessibilitUtil;
import com.xiaoniu.asmhelp.util.SystemProperties;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity implements ASMListener {
    private static final String TAG = "MainActivity";
    private List<ASBase> asBases;
    private static final int SDK_INT = Build.VERSION.SDK_INT;
    private static String MODEL = android.os.Build.MODEL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ASMAutoUtils.getInstance().setListener(this);
//        String oppoRom = SystemProperties.getRom("ro.build.version.opporom", "unkonw");
//        String oppoRom = SystemProperties.getRom("ro.vivo.rom.version", "unkonw");
//        String emuiRom = SystemProperties.getRom("ro.build.version.emui", "unkonw");

        Log.d(TAG, "requestPermission: MODEL " + MODEL + " SDK_INT " + SDK_INT );
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
//        jumpActivity(asBases.intent);

    }


    @Override
    public void complete(ASBase arg1) {
        if (asBases != null && asBases.size() > 0) {
            ASMAutoUtils.getInstance().start(asBases.remove(0));
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
}
