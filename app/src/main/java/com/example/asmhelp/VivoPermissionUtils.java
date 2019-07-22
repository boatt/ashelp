package com.example.asmhelp;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;


public class VivoPermissionUtils {

    public static int getvivoBgStartActivityPermissionStatus(Context context) {
        String packageName = context.getPackageName();
//        Uri uri2 = Uri.parse("content://com.iqoo.secure.provider.secureprovider/start_bg_activity");
        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/start_bg_activity");
        String selection = "pkgname = ?";
        String[] selectionArgs = new String[]{packageName};
        try {
            Cursor cursor = context
                    .getContentResolver()
                    .query(uri2, null, selection, selectionArgs, null);
            if (cursor != null) {
                if (cursor.moveToFirst()) {
                    int currentmode = cursor.getInt(cursor.getColumnIndex("currentstate"));
                    cursor.close();
                    return currentmode;
                } else {
                    cursor.close();
                    return 1;
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return 1;
    }

    public static int getVivoApplistPermissionStatus(Context context) {
        Uri uri2 = Uri.parse("content://com.iqoo.secure.provider.secureprovider/read_installed_apps");
//        Uri uri2 = Uri.parse("content://com.vivo.permissionmanager.provider.permission/read_installed_apps");
        try {
            Cursor cursor = context
                    .getContentResolver()
                    .query(uri2, null, null, null, null);
            if (cursor != null) {
                cursor.moveToFirst();
                while (cursor.moveToNext()) {
                    String name = cursor.getString(cursor.getColumnIndex("pkgname"));
                    String newName = CryUtils.akt(name,"iqoo11-14");
                    int currentmode = cursor.getInt(cursor.getColumnIndex("status"));
                    Log.d("liwei", "name------>" + newName + "------status---->" + currentmode);
                }
            }
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        return 1;
    }

}
