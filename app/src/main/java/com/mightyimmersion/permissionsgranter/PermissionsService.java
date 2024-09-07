package com.mightyimmersion.permissionsgranter;

import android.app.Activity;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.BufferedReader;
import java.io.InputStreamReader;

public class PermissionsService extends Service {


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String[] packageNames = {"com.mightyimmersion.mightyplatform.adminapp.prod.nohome", "com.mightyimmersion.mightyplatform.adminapp.dev.nohome", "com.mightyimmersion.mightyplatform.adminapp.prod.pico2", "com.mightyimmersion.mightyplatform.adminapp.dev.pico2"};
        String[] permissions = {"REQUEST_INSTALL_PACKAGES", "android:get_usage_stats", "MANAGE_EXTERNAL_STORAGE" };

        for (String packageName : packageNames) {
            for (String permission : permissions) {
                grantPermission(packageName, permission);
            }
        }

        return Service.START_NOT_STICKY;
    }

    private void grantPermission(String packageName, String opString) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            Log.v("MIGHTY_LOG", "PermissionsGranter: Not granting permission. SDK version is less than Oreo.");
            return;
        }

        try {
            Process p = Runtime.getRuntime().exec("appops set " + packageName + " " + opString + " allow");
            int exitCode = p.waitFor();
            if (exitCode != 0) {
                Log.v("MIGHTY_LOG", "PermissionsGranter: Failed to grant permission. Exit code: " + exitCode);
                BufferedReader errorReader = new BufferedReader(new InputStreamReader(p.getErrorStream()));
                String line;
                while ((line = errorReader.readLine()) != null) {
                    Log.v("MIGHTY_LOG", "PermissionsGranter: " + line);
                }
            } else {
                Log.v("MIGHTY_LOG", "PermissionsGranter: Permission " + opString + " granted successfully for package " + packageName + ".");
            }
        } catch (Exception e) {
            Log.e("MIGHTY_LOG", "PermissionsGranter: Failed to set appops permission: ", e);
        }

    }

    // This method is required, but in this case it’s not used for binding
    @Override
    public IBinder onBind(Intent intent) {
        return null; // This is not a bound service, so we return null
    }
}
